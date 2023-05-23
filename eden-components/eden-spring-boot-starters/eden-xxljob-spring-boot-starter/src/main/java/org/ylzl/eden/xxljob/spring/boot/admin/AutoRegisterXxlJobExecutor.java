package org.ylzl.eden.xxljob.spring.boot.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.ylzl.eden.xxljob.spring.boot.core.XxlJobRegister;
import org.ylzl.eden.xxljob.spring.boot.model.XxlJobGroup;
import org.ylzl.eden.xxljob.spring.boot.model.XxlJobInfo;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 自动注册 XxlJobSpringExecutor
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class AutoRegisterXxlJobExecutor extends XxlJobExecutor {

	private final List<XxlJobInfo> xxlJobInfos = Lists.newArrayList();

	private final XxlJobAdminTemplate xxlJobAdminTemplate;

	private static ApplicationContext applicationContext;

	private String appName;

	@Override
	public void setAppName(String appName) {
		super.setAppName(appName);
		this.appName = appName;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		AutoRegisterXxlJobExecutor.applicationContext = applicationContext;
	}

	@Override
	public void start() throws Exception {
		super.start();

		init();
		XxlJobGroup xxlJobGroup = new XxlJobGroup();
		xxlJobGroup.setTitle(appName);
		xxlJobGroup.setAppName(appName);
		xxlJobGroup.setAddressType(0);
		ResponseEntity<String> responseEntity = xxlJobAdminTemplate.saveOrUpdateGroup(xxlJobGroup);
		if (!responseEntity.getStatusCode().is2xxSuccessful()) {
			log.warn("添加执行器 `{}` 失败，原因: 响应状态码为 {}", appName, responseEntity.getStatusCodeValue());
			return;
		}

		ReturnT<String> returnT = JSON.parseObject(responseEntity.getBody(), new TypeReference<ReturnT<String>>() {
		});
		if (returnT == null || returnT.getCode() == ReturnT.FAIL_CODE) {
			log.warn("添加执行器 `{}` 失败，原因: {}", appName, returnT == null ? "接口返回NULL" : returnT.getMsg());
			return;
		}

		log.info("添加执行器 `{}` 成功", appName);
		for (XxlJobInfo xxlJobInfo : xxlJobInfos) {
			xxlJobInfo.setJobGroup(Integer.parseInt(returnT.getContent()));
			ResponseEntity<String> response = xxlJobAdminTemplate.addOrUpdateJob(xxlJobInfo);
			if (!responseEntity.getStatusCode().is2xxSuccessful()) {
				log.warn("添加任务 `{}` 失败，原因: 响应状态码为 {}", xxlJobInfo.getExecutorHandler(),
					responseEntity.getStatusCodeValue());
				return;
			}

			String responseBody = response.getBody();
			ReturnT<String> result = JSON.parseObject(responseBody, new TypeReference<ReturnT<String>>() {
			});
			if (result == null || result.getCode() == ReturnT.FAIL_CODE) {
				log.warn("添加任务 `{}` 失败，原因: {}", xxlJobInfo.getExecutorHandler(), result == null ?
					"接口返回NULL" : result.getMsg());
				return;
			}
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
			if (!(bean instanceof IJobHandler)) {
				continue;
			}

			Class<?> clazz = bean.getClass();
			JobHandler jobHandler = AnnotationUtils.findAnnotation(clazz, JobHandler.class);
			if (jobHandler == null) {
				continue;
			}
			String name = jobHandler.value();
			if (name.trim().length() == 0) {
				throw new RuntimeException("xxl-job jobhandler name invalid, for[" + bean.getClass() + "] .");
			}
			if (loadJobHandler(name) != null) {
				throw new RuntimeException("xxl-job jobhandler[" + name + "] naming conflicts.");
			}

			Map<Method, XxlJobRegister> annotatedMethods = null;
			try {
				annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
					(MethodIntrospector.MetadataLookup<XxlJobRegister>) method ->
						AnnotatedElementUtils.findMergedAnnotation(method, XxlJobRegister.class));
			} catch (Throwable ex) {
				log.error("@XxlJobRegister not found from bean[" + beanDefinitionName + "].", ex);
			}
			if (annotatedMethods == null || annotatedMethods.isEmpty()) {
				continue;
			}

			for (Map.Entry<Method, XxlJobRegister> entry : annotatedMethods.entrySet()) {
				Method method = entry.getKey();
				if (!(method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(String.class))) {
					throw new RuntimeException("xxl-job method-jobhandler param-classtype invalid, for[" + bean.getClass() + "#" + method.getName() + "] , " +
						"The correct method format like \" public ReturnT<String> execute(String param) \" .");
				}
				if (!method.getReturnType().isAssignableFrom(ReturnT.class)) {
					throw new RuntimeException("xxl-job method-jobhandler return-classtype invalid, for[" + bean.getClass() + "#" + method.getName() + "] , " +
						"The correct method format like \" public ReturnT<String> execute(String param) \" .");
				}

				registJobHandler(name, (IJobHandler) bean);
				cacheXxlJobInfo(name, entry.getValue());
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
