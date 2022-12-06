package org.ylzl.eden.spring.boot.condition;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import java.util.Map;

/**
 * key-value 匹配条件
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class KeyAndValueCondition extends SpringBootCondition {

	private static final String NOT_FOUND_ANNOTATION_ATTRIBUTES = "Could not found annotation attributes from @DynamicConditional";

	private static final String NULL_KEY_OR_VALUE_FROM_DYNAMIC_CONDITIONAL = "Null key or value from DynamicConditional";

	private static final String NOT_MATCH_VALUE_FROM_DYNAMIC_CONDITIONAL = "Not match value from DynamicConditional";

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(ConditionalOnKeyAndValue.class.getName());
		AssertUtils.notNull(annotationAttributes, NOT_FOUND_ANNOTATION_ATTRIBUTES);

		Object key = annotationAttributes.get("key");
		Object value = annotationAttributes.get("value");
		if (key == null || value == null) {
			return ConditionOutcome.noMatch(NULL_KEY_OR_VALUE_FROM_DYNAMIC_CONDITIONAL);
		}

		String envValue = context.getEnvironment().getProperty(key.toString());
		if (value.equals(envValue)) {
			return ConditionOutcome.match();
		}
		return ConditionOutcome.noMatch(NOT_MATCH_VALUE_FROM_DYNAMIC_CONDITIONAL);
	}
}
