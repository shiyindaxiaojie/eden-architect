package org.ylzl.eden.spring.boot.cat.annotations;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Transaction 方法拦截
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class CatTransactionAdvice implements MethodInterceptor {

	public static final String INNER_SERVICE = "InnerService";

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		Object target = invocation.getThis();
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(Objects.requireNonNull(target));
		Transaction transaction = Cat.newTransaction(INNER_SERVICE,
			targetClass.getSimpleName() + "." + method.getName());
		try {
			return invocation.proceed();
		} finally {
			if (transaction != null) {
				transaction.complete();
			}
		}
	}
}
