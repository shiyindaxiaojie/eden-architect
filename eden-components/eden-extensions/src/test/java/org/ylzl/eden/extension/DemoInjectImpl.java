package org.ylzl.eden.extension;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DemoInjectImpl implements DemoInject {

	@Override
	public void echo() {
		System.out.println("inject");
	}
}
