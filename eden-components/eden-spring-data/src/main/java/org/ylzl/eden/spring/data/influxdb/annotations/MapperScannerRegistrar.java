package org.ylzl.eden.spring.data.influxdb.annotations;


import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.ylzl.eden.spring.data.influxdb.mapper.MapperFactoryBean;
import org.ylzl.eden.spring.data.influxdb.mapper.MapperScannerConfigurer;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  MapperScanner 注册器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class MapperScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

	@Override
	public void setResourceLoader(@NotNull ResourceLoader resourceLoader) {
		// NoOp
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, @NotNull BeanDefinitionRegistry registry) {
		AnnotationAttributes mapperScanAttrs = AnnotationAttributes
			.fromMap(importingClassMetadata.getAnnotationAttributes(MapperScan.class.getName()));
		if (mapperScanAttrs != null) {
			registerBeanDefinitions(mapperScanAttrs, registry, generateBaseBeanName(importingClassMetadata, 0));
		}
	}

	/**
	 * 注册 MapperScannerConfigurer
	 *
	 * @param annoAttrs
	 * @param registry
	 * @param beanName
	 */
	void registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry, String beanName) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
		builder.addPropertyValue("processPropertyPlaceHolders", true);

		Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
		if (!Annotation.class.equals(annotationClass)) {
			builder.addPropertyValue("annotationClass", annotationClass);
		}

		Class<?> markerInterface = annoAttrs.getClass("markerInterface");
		if (!Class.class.equals(markerInterface)) {
			builder.addPropertyValue("markerInterface", markerInterface);
		}

		Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
		if (!BeanNameGenerator.class.equals(generatorClass)) {
			builder.addPropertyValue("nameGenerator", BeanUtils.instantiateClass(generatorClass));
		}

		Class<? extends MapperFactoryBean> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
		if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
			builder.addPropertyValue("mapperFactoryBeanClass", mapperFactoryBeanClass);
		}

		List<String> basePackages = new ArrayList<>();
		basePackages.addAll(
			Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));

		basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
			.collect(Collectors.toList()));

		basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName)
			.collect(Collectors.toList()));

		String lazyInitialization = annoAttrs.getString("lazyInitialization");
		if (StringUtils.hasText(lazyInitialization)) {
			builder.addPropertyValue("lazyInitialization", lazyInitialization);
		}

		builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));

		registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
	}

	/**
	 * 生成 Bean 名称
	 *
	 * @param importingClassMetadata
	 * @param index
	 * @return
	 */
	private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
		return importingClassMetadata.getClassName() + "#" + MapperScannerRegistrar.class.getSimpleName() + "#" + index;
	}

	static class RepeatingRegistrar extends MapperScannerRegistrar {

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, @NotNull BeanDefinitionRegistry registry) {
			AnnotationAttributes mapperScansAttrs = AnnotationAttributes
				.fromMap(importingClassMetadata.getAnnotationAttributes(MapperScans.class.getName()));
			if (mapperScansAttrs != null) {
				AnnotationAttributes[] annotations = mapperScansAttrs.getAnnotationArray("value");
				for (int i = 0; i < annotations.length; i++) {
					registerBeanDefinitions(annotations[i], registry, generateBaseBeanName(importingClassMetadata, i));
				}
			}
		}
	}
}
