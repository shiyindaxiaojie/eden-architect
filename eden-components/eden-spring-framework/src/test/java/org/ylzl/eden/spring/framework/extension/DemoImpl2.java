package org.ylzl.eden.spring.framework.extension;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DemoImpl2 implements Demo {

//	private DemoInject demoInject;
//
//	@Inject
//	public void setDemoInject(DemoInject demoInject) {
//		this.demoInject = demoInject;
//	}

	@Override
	public void echo() {
		System.out.println("demo2");
	}
}
