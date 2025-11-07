package org.ylzl.eden.spring.framework.expression.function;

import javassist.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.ylzl.eden.commons.lang.reflect.ReflectionUtils;
import org.ylzl.eden.spring.framework.aop.util.AopUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义函数注册器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class CustomFunctionRegistrar implements ApplicationContextAware {

	private static final Map<String, Method> FUNCTION_CACHE = new ConcurrentHashMap<>();

	/**
	 * 初始化
	 *
	 * @param applicationContext ApplicationContext
	 * @throws BeansException 初始化Bean异常
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		initialize(applicationContext);
	}

	/**
	 * 初始化自定义函数
	 *
	 * @param applicationContext ApplicationContext
	 */
	private void initialize(ApplicationContext applicationContext) {
		Map<String, Object> components = applicationContext.getBeansWithAnnotation(Component.class);
		components.values().forEach(component -> {
			// 跳过 Request/Session 作用域的 Bean
			Scope scope = AnnotatedElementUtils.findMergedAnnotation(component.getClass(), Scope.class);
			if (scope != null &&
				(WebApplicationContext.SCOPE_REQUEST.equals(scope.value()) ||
				WebApplicationContext.SCOPE_SESSION.equals(scope.value()))) {
				log.warn("Skipping request/session-scoped bean: {}", component.getClass().getName());
				return;
			}

			Method[] methods = component.getClass().getMethods();
			if (methods.length == 0) {
				return;
			}

			Object targetObject = AopUtils.getDynamicProxyTargetObject(component);
			for (Method method : methods) {
				CustomFunction annotation = AnnotatedElementUtils.findMergedAnnotation(method, CustomFunction.class);
				if (annotation == null) {
					continue;
				}
				if (ReflectionUtils.isStaticMethod(method)) {
					cache(annotation, method);
					continue;
				}
				ClassPool pool = ClassPool.getDefault();
				Class<?> targetClass = targetObject.getClass();
				String staticallyClassName = targetClass.getName() + "_Statically";
				Class<?> delegateClass;
				CtClass ctClass = pool.getOrNull(staticallyClassName);
				try {
					if (ctClass == null) {
						ctClass = constructCtClass(method, pool, targetClass, staticallyClassName);
						delegateClass = ctClass.toClass();
					} else {
						delegateClass = ctClass.getClass().getClassLoader().loadClass(staticallyClassName);
					}
					Object proxy = delegateClass.getConstructor(targetClass).newInstance(component);
					Method[] proxyMethods = proxy.getClass().getDeclaredMethods();
					Arrays.stream(proxyMethods).forEach(proxyMethod -> {
						if (Arrays.equals(method.getParameterTypes(), proxyMethod.getParameterTypes()) &&
							method.getName().equals(proxyMethod.getName())) {
							cache(annotation, proxyMethod);
						}
					});
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	/**
	 * 注册自定义函数到 SpEL解析上下文
	 *
	 * @param context SpEL解析上下文
	 */
	public static void register(StandardEvaluationContext context) {
		FUNCTION_CACHE.forEach(context::registerFunction);
	}

	private static void cache(CustomFunction annotation, Method method) {
		String registerName = StringUtils.hasText(annotation.value()) ? annotation.value() : method.getName();
		FUNCTION_CACHE.put(registerName, method);
		log.info("Register custom function '{}' as name '{}'", method, registerName);
	}

	private CtClass constructCtClass(Method method, ClassPool pool, Class<?> targetClass, String staticallyClassName)
		throws NotFoundException, CannotCompileException {
		CtClass ctClass = pool.makeClass(staticallyClassName);
		ctClass.addInterface(pool.get(Serializable.class.getName()));

		CtField field = new CtField(pool.get(targetClass.getName()), "delegating", ctClass);
		field.setModifiers(javassist.Modifier.STATIC | javassist.Modifier.PROTECTED);
		ctClass.addField(field);

		CtConstructor constructor = new CtConstructor(new CtClass[]{pool.get(targetClass.getName())}, ctClass);
		constructor.setBody("{delegating = $1;}");
		ctClass.addConstructor(constructor);

		CtMethod getterMethod = new CtMethod(pool.get(targetClass.getName()), "getDelegating", new CtClass[]{}, ctClass);
		getterMethod.setModifiers(javassist.Modifier.PUBLIC);
		getterMethod.setBody("{return delegating;}");
		ctClass.addMethod(getterMethod);

		int modifier = method.getModifiers();
		modifier |= javassist.Modifier.STATIC;
		String methodName = method.getName();
		Class<?> returnType = method.getReturnType();
		StringBuilder builder = new StringBuilder();
		builder.append(chooseModifier(modifier)).append(" ")
			.append(returnType.getName()).append(" ")
			.append(methodName).append("(");

		Class<?>[] parameterType = method.getParameterTypes();
		StringBuilder params = null;
		for (int i = 0; i < parameterType.length; i++) {
			builder.append(parameterType[i].getName()).append(" ");
			builder.append("$_").append(i).append(",");
			if (params == null) {
				params = new StringBuilder();
			}
			params.append("$_").append(i).append(",");
		}
		if (params != null) {
			builder.delete(builder.length() - 1, builder.length());
			params.delete(params.length() - 1, params.length());
		}
		builder.append(")");
		builder.append("{");
		if (!returnType.equals(void.class)) {
			builder.append("return").append(" ");
		}
		builder.append("delegating.").append(methodName).append("(");
		if (params != null) {
			builder.append(params);
		}
		builder.append(")").append(";");
		builder.append("}");

		CtMethod ctMethod = CtMethod.make(builder.toString(), ctClass);
		ctClass.addMethod(ctMethod);
		return ctClass;
	}

	private String chooseModifier(int modifier) {
		StringBuilder builder = new StringBuilder();
		if ((modifier & javassist.Modifier.PUBLIC) == javassist.Modifier.PUBLIC) {
			builder.append("public").append(" ");
		}
		if ((modifier & javassist.Modifier.PRIVATE) == javassist.Modifier.PRIVATE) {
			builder.append("private").append(" ");
		}
		if ((modifier & javassist.Modifier.PROTECTED) == javassist.Modifier.PROTECTED) {
			builder.append("protected").append(" ");
		}
		if ((modifier & javassist.Modifier.ABSTRACT) == javassist.Modifier.ABSTRACT) {
			builder.append("abstract").append(" ");
		}
		if ((modifier & javassist.Modifier.STATIC) == javassist.Modifier.STATIC) {
			builder.append("static").append(" ");
		}
		if ((modifier & javassist.Modifier.FINAL) == javassist.Modifier.FINAL) {
			builder.append("final").append(" ");
		}
		return builder.toString();
	}
}
