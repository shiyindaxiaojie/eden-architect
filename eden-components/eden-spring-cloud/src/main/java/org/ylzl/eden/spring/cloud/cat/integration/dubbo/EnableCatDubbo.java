package org.ylzl.eden.spring.cloud.cat.integration.dubbo;

import com.dianping.cat.Cat;

/**
 * 开启 CAT 集成 Dubbo
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class EnableCatDubbo {

	private static boolean enabled = true;

	public static void enable() {
		enabled = true;
	}

	public static boolean isEnabled() {
		return Cat.getManager().isCatEnabled() && enabled;
	}
}
