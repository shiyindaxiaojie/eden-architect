package org.ylzl.eden.full.link.stress.testing;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.Data;

import java.io.Serializable;

/**
 * 压测标记上下文
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
public class StressContext implements Serializable {

	private static final TransmittableThreadLocal<StressContext> CONTEXT = new TransmittableThreadLocal<>();

	private boolean stress;

	public static StressContext getContext() {
		return CONTEXT.get();
	}

	public static void setContext(StressContext context) {
		CONTEXT.set(context);
	}

	public static void removeContext() {
		CONTEXT.remove();
	}
}
