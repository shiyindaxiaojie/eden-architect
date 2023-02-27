package org.ylzl.eden.spring.integration.cat.config;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * CAT 状态，防止容器外部优先初始化
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CatState {

	private static final AtomicBoolean STATE = new AtomicBoolean();

	public static boolean isInitialized() {
		return STATE.get();
	}

	public static void setInitialized() {
		STATE.set(true);
	}
}
