package org.ylzl.eden.cola.extension.register;

import com.google.common.collect.Maps;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.ylzl.eden.cola.extension.BizScenario;
import org.ylzl.eden.cola.extension.Extension;
import org.ylzl.eden.cola.extension.Extensions;
import org.ylzl.eden.cola.extension.exception.ExtensionException;

import java.util.Map;

/**
 * 扩展点注册器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class ExtensionRegister {

	private final Map<ExtensionCoordinate, Object> extensionClasses = Maps.newConcurrentMap();

	@SuppressWarnings("unchecked")
	public <T> T getExtension(Class<T> extensionClass, String uniqueIdentity) {
		return (T) extensionClasses.get(new ExtensionCoordinate(extensionClass.getSimpleName(), uniqueIdentity));
	}

	public void registerExtension(Object extensionInstance) {
		Class<?> extensionClass = extensionInstance.getClass();
		if (AopUtils.isAopProxy(extensionInstance)) {
			extensionClass = ClassUtils.getUserClass(extensionInstance);
		}

		Extension extensionAnnotation = AnnotationUtils.findAnnotation(extensionClass, Extension.class);
		if (extensionAnnotation != null) {
			saveInExtensionClasses(extensionInstance, extensionClass, extensionAnnotation);
		}

		Extensions extensionsAnnotation = AnnotationUtils.findAnnotation(extensionClass, Extensions.class);
		if (extensionsAnnotation != null) {
			Extension[] extensions = extensionsAnnotation.value();
			for (Extension extension : extensions) {
				saveInExtensionClasses(extensionInstance, extensionClass, extension);
			}
		}
	}

	private void saveInExtensionClasses(Object extensionInstance, Class<?> extensionClass, Extension extension) {
		BizScenario bizScenario = BizScenario.valueOf(extension.bizId(), extension.useCase(), extension.scenario());
		ExtensionCoordinate extensionCoordinate = resolveExtensionCoordinate(extensionClass, bizScenario.getUniqueIdentity(), extension);
		Object preVal = extensionClasses.put(extensionCoordinate, extensionInstance);
		if (preVal != null) {
			throw new ExtensionException("Duplicate registration is not allowed for :" + extensionCoordinate);
		}
	}

	private ExtensionCoordinate resolveExtensionCoordinate(Class<?> extensionClass, String uniqueIdentity, Extension extension) {
		Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(extensionClass);
		if (interfaces.length > 0) {
			for (Class<?> extensionInterface : interfaces) {
				if (Void.TYPE.equals(extension.interfaceClass()) ||
					extensionInterface.equals(extension.interfaceClass())) {
					return new ExtensionCoordinate(extensionInterface.getSimpleName(), uniqueIdentity);
				}
			}
		}
		throw new ExtensionException("Please assign a extension point interface for " + extensionClass);
	}
}
