package org.ylzl.eden.event.auditor.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * SpEL表达式解析上下文
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class EventAuditorContext {

	private static final TransmittableThreadLocal<EvaluationContext> VARIABLES =
		new TransmittableThreadLocal<EvaluationContext>() {

			@Override
			protected EvaluationContext initialValue() {
				return new StandardEvaluationContext();
			}
		};

	public static EvaluationContext getContext() {
		return VARIABLES.get();
	}

	public static Object lookupVariable(String key) {
		EvaluationContext context = getContext();
		return context.lookupVariable(key);
	}

	public static void setVariable(String key, Object value) {
		EvaluationContext context = getContext();
		context.setVariable(key, value);
		VARIABLES.set(context);
	}

	public static void remove() {
		VARIABLES.remove();
	}
}
