/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.integration.xxljob.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.glue.GlueFactory;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.handler.impl.MethodJobHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.ylzl.eden.spring.integration.xxljob.core.XxlJobRegister;
import org.ylzl.eden.spring.integration.xxljob.model.XxlJobGroup;
import org.ylzl.eden.spring.integration.xxljob.model.XxlJobInfo;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 自动注册 XxlJobSpringExecutor
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class AutoRegisterXxlJobSpringExecutor extends XxlJobSpringExecutor {

	private final List<XxlJobInfo> xxlJobInfos = Lists.newArrayList();

	private final XxlJobAdminTemplate xxlJobAdminTemplate;

	private static ApplicationContext applicationContext;

	private String appName;

	@Override
	public void setAppname(String appname) {
		super.setAppname(appname);
		this.appName = appname;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		AutoRegisterXxlJobSpringExecutor.applicationContext = applicationContext;
	}

	@Override
	public void afterSingletonsInstantiated() {
		init();

		GlueFactory.refreshInstance(1);

		try {
			start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void start() throws Exception {
		super.start();

		XxlJobGroup xxlJobGroup = new XxlJobGroup();
		xxlJobGroup.setTitle(appName);
		xxlJobGroup.setAppName(appName);
		xxlJobGroup.setAddressType(0);
		ResponseEntity<String> responseEntity = xxlJobAdminTemplate.saveGroup(xxlJobGroup);
		if (!responseEntity.getStatusCode().is2xxSuccessful()) {
			log.warn("添加执行器 '{}' 失败，原因: 响应状态码为 {}", appName, responseEntity.getStatusCodeValue());
			return;
		}

		ReturnT<String> returnT = JSON.parseObject(responseEntity.getBody(), new TypeReference<ReturnT<String>>() {
		});
		if (returnT == null || returnT.getCode() == ReturnT.FAIL_CODE) {
			log.warn("添加执行器 '{}' 失败，原因: {}", appName, returnT == null? "接口返回NULL" : returnT.getMsg());
			return;
		}

		log.info("添加执行器 '{}' 成功", appName);
		for (XxlJobInfo xxlJobInfo : xxlJobInfos) {
			xxlJobInfo.setJobGroup(Integer.parseInt(returnT.getContent()));
			ResponseEntity<String> response = xxlJobAdminTemplate.addOrUpdateJob(xxlJobInfo);
			if (!responseEntity.getStatusCode().is2xxSuccessful()) {
				log.warn("添加任务 '{}' 失败，原因: 响应状态码为 {}", xxlJobInfo.getExecutorHandler(), responseEntity.getStatusCodeValue());
				return;
			}

			String responseBody = response.getBody();
			ReturnT<String> result = JSON.parseObject(responseBody, new TypeReference<ReturnT<String>>() {
			});
			if (result == null || result.getCode() == ReturnT.FAIL_CODE) {
				log.warn("添加任务 '{}' 失败，原因: {}", xxlJobInfo.getExecutorHandler(), result == null? "接口返回NULL" : result.getMsg());
				return;
			}

			log.info("启动任务 '{}'", xxlJobInfo.getExecutorHandler());
			xxlJobAdminTemplate.startJob(Integer.parseInt(result.getContent()));
			log.info("启动任务 '{}' 完成", xxlJobInfo.getExecutorHandler());
		}
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	private void init() {
		String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
		for (String beanDefinitionName : beanDefinitionNames) {
			Object bean = applicationContext.getBean(beanDefinitionName);
			Map<Method, XxlJob> annotatedMethods = null;
			try {
				annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
					(MethodIntrospector.MetadataLookup<XxlJob>) method -> AnnotatedElementUtils.findMergedAnnotation(method, XxlJob.class));
			} catch (Throwable ex) {
				log.error("xxl-job method-jobhandler resolve error for bean[" + beanDefinitionName + "].", ex);
			}
			if (annotatedMethods == null || annotatedMethods.isEmpty()) {
				continue;
			}

			for (Map.Entry<Method, XxlJob> methodXxlJobEntry : annotatedMethods.entrySet()) {
				Method method = methodXxlJobEntry.getKey();
				XxlJob xxlJob = methodXxlJobEntry.getValue();
				XxlJobRegister xxlJobRegister = AnnotationUtils.findAnnotation(method, XxlJobRegister.class);
				if (xxlJob == null || xxlJobRegister == null) {
					continue;
				}

				String name = xxlJob.value();
				if (name.trim().length() == 0) {
					throw new RuntimeException("xxl-job method-jobhandler name invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
				}
				if (loadJobHandler(name) != null) {
					throw new RuntimeException("xxl-job jobhandler[" + name + "] naming conflicts.");
				}

				if (!(method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(String.class))) {
					throw new RuntimeException("xxl-job method-jobhandler param-classtype invalid, for[" + bean.getClass() + "#" + method.getName() + "] , " +
						"The correct method format like \" public ReturnT<String> execute(String param) \" .");
				}
				if (!method.getReturnType().isAssignableFrom(ReturnT.class)) {
					throw new RuntimeException("xxl-job method-jobhandler return-classtype invalid, for[" + bean.getClass() + "#" + method.getName() + "] , " +
						"The correct method format like \" public ReturnT<String> execute(String param) \" .");
				}
				method.setAccessible(true);

				Method initMethod = null;
				Method destroyMethod = null;
				if (xxlJob.init().trim().length() > 0) {
					try {
						initMethod = bean.getClass().getDeclaredMethod(xxlJob.init());
						initMethod.setAccessible(true);
					} catch (NoSuchMethodException e) {
						throw new RuntimeException("xxl-job method-jobhandler initMethod invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
					}
				}
				if (xxlJob.destroy().trim().length() > 0) {
					try {
						destroyMethod = bean.getClass().getDeclaredMethod(xxlJob.destroy());
						destroyMethod.setAccessible(true);
					} catch (NoSuchMethodException e) {
						throw new RuntimeException("xxl-job method-jobhandler destroyMethod invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
					}
				}

				registJobHandler(name, new MethodJobHandler(bean, method, initMethod, destroyMethod));
				cacheXxlJobInfo(name, xxlJobRegister);
			}
		}
	}

	private void cacheXxlJobInfo(String name, XxlJobRegister xxlJobRegister) {
		XxlJobInfo xxlJobInfo = new XxlJobInfo();
		xxlJobInfo.setAuthor(xxlJobRegister.author());
		xxlJobInfo.setAlarmEmail(xxlJobRegister.alarmEmail());
		xxlJobInfo.setExecutorBlockStrategy(xxlJobRegister.blockStrategy().name());
		xxlJobInfo.setExecutorFailRetryCount(xxlJobRegister.failRetryCount());
		xxlJobInfo.setExecutorHandler(name);
		xxlJobInfo.setExecutorParam(xxlJobRegister.param());
		xxlJobInfo.setExecutorRouteStrategy(xxlJobRegister.routeStrategy().name());
		xxlJobInfo.setExecutorTimeout(xxlJobRegister.timeout());
		xxlJobInfo.setGlueType(GlueTypeEnum.BEAN.name());
		xxlJobInfo.setJobCron(xxlJobRegister.cron());
		xxlJobInfo.setJobDesc(xxlJobRegister.desc());
		xxlJobInfos.add(xxlJobInfo);
	}
}
