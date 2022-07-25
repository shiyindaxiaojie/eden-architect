package org.ylzl.eden.spring.data.influxdb.mapper;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.ylzl.eden.spring.framework.error.util.AssertEnhancer;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
public class MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {

	private String beanName;

	private String basePackage;

	private ApplicationContext applicationContext;

	private boolean processPropertyPlaceHolders;

	private String lazyInitialization;

	private Class<? extends Annotation> annotationClass;

	private Class<?> markerInterface;

	private Class<? extends MapperFactoryBean> mapperFactoryBeanClass;

	private BeanNameGenerator nameGenerator;

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		AssertEnhancer.notNull(this.basePackage, "Property 'basePackage' is required");
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		if (this.processPropertyPlaceHolders) {
			processPropertyPlaceHolders();
		}

		ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
		scanner.setAnnotationClass(this.annotationClass);
		scanner.setMarkerInterface(this.markerInterface);
		scanner.setResourceLoader(this.applicationContext);
		scanner.setBeanNameGenerator(this.nameGenerator);
		scanner.setMapperFactoryBeanClass(this.mapperFactoryBeanClass);
		if (StringUtils.hasText(lazyInitialization)) {
			scanner.setLazyInitialization(Boolean.parseBoolean(lazyInitialization));
		}
		scanner.registerFilters();
		scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// NoOp
	}

	@Override
	public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public void setProcessPropertyPlaceHolders(boolean processPropertyPlaceHolders) {
		this.processPropertyPlaceHolders = processPropertyPlaceHolders;
	}

	public void setLazyInitialization(String lazyInitialization) {
		this.lazyInitialization = lazyInitialization;
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public void setMarkerInterface(Class<?> markerInterface) {
		this.markerInterface = markerInterface;
	}

	public void setMapperFactoryBeanClass(Class<? extends MapperFactoryBean> mapperFactoryBeanClass) {
		this.mapperFactoryBeanClass = mapperFactoryBeanClass;
	}

	public void setNameGenerator(BeanNameGenerator nameGenerator) {
		this.nameGenerator = nameGenerator;
	}

	private void processPropertyPlaceHolders() {
		Map<String, PropertyResourceConfigurer> prcs = applicationContext.getBeansOfType(PropertyResourceConfigurer.class);

		if (!prcs.isEmpty() && applicationContext instanceof ConfigurableApplicationContext) {
			BeanDefinition mapperScannerBean = ((ConfigurableApplicationContext) applicationContext).getBeanFactory()
				.getBeanDefinition(beanName);

			DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
			factory.registerBeanDefinition(beanName, mapperScannerBean);

			for (PropertyResourceConfigurer prc : prcs.values()) {
				prc.postProcessBeanFactory(factory);
			}

			PropertyValues values = mapperScannerBean.getPropertyValues();
			this.basePackage = updatePropertyValue(values);
		}
		this.basePackage = Optional.ofNullable(this.basePackage).map(getEnvironment()::resolvePlaceholders).orElse(null);
	}

	private Environment getEnvironment() {
		return this.applicationContext.getEnvironment();
	}

	private String updatePropertyValue(PropertyValues values) {
		PropertyValue property = values.getPropertyValue("basePackage");
		if (property == null) {
			return null;
		}

		Object value = property.getValue();
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return value.toString();
		}
		if (value instanceof TypedStringValue) {
			return ((TypedStringValue) value).getValue();
		}
		return null;
	}
}
