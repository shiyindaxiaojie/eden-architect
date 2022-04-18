package org.ylzl.eden.spring.integration.sms.core;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.ylzl.eden.spring.framework.error.ClientErrorType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 短信操作模板策略
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class SmsTemplateStrategy {

	private final Map<String, SmsTemplate> beansMap;

	public SmsTemplateStrategy(List<SmsTemplate> strategies) {
		if (CollectionUtils.isEmpty(strategies)) {
			beansMap = Collections.emptyMap();
			return;
		}

		Map<String, SmsTemplate> tempMap = Maps.newHashMapWithExpectedSize(strategies.size());
		strategies.forEach(strategy -> tempMap.put(strategy.getSmsPlatform(), strategy));
		beansMap = Collections.unmodifiableMap(tempMap);
	}

	public SmsTemplate getExecutor(String strategy) {
		SmsTemplate smsTemplate = beansMap.get(strategy);
		ClientErrorType.notEmpty(beansMap,"C0501", "SmsTemplate beanDefinition named '" + strategy + "' not found");
		return smsTemplate;
	}

	public SmsTemplate getDefaultExecutor() {
		ClientErrorType.notEmpty(beansMap,"C0501", "SmsTemplate beanDefinition not found");
		return Objects.requireNonNull(beansMap.entrySet().stream().findFirst().orElse(null)).getValue();
	}
}
