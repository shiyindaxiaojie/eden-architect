package org.ylzl.eden.spring.integration.cat.integration.dubbo;

import com.dianping.cat.Cat;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DubboCat {

	private static boolean enabled = true;

	public static void enable() {
		enabled = true;
	}

	public static void disable() {
		enabled = false;
	}

	public static boolean isEnable() {
		return Cat.getManager().isCatEnabled() && enabled;
	}
}
