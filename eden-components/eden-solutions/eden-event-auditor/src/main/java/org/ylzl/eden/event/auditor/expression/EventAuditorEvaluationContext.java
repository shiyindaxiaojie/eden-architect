package org.ylzl.eden.event.auditor.expression;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;
import org.ylzl.eden.commons.collections.MapUtils;
import org.ylzl.eden.event.auditor.context.EventAuditorContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class EventAuditorEvaluationContext extends MethodBasedEvaluationContext {

	public EventAuditorEvaluationContext(Object rootObject, Method method, Object[] arguments,
										 ParameterNameDiscoverer parameterNameDiscoverer,
										 Object result, String errorMsg) {
		super(rootObject, method, arguments, parameterNameDiscoverer);
		Map<String, Object> variables = EventAuditorContext.getVariables();
		if (MapUtils.isEmpty(variables)) {
			setVariables(variables);
		}

		Map<String, Object> globalVariables = EventAuditorContext.getGlobalVariables();
		if (MapUtils.isNotEmpty(globalVariables)) {
			globalVariables.forEach((key, value) -> {
				if (lookupVariable(key) == null) {
					setVariable(key, value);
				}
			});
		}
		setVariable("_result", result);
		setVariable("_errorMsg", errorMsg);
	}
}
