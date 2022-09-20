package org.ylzl.eden.commons.lang;

/**
 * 父子线程变量传递测试
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class InheritableThreadLocalTest {

	private static final InheritableThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();

	public static void main(String[] args) {
		threadLocal.set(1);
		InheritableThreadLocalTest test = new InheritableThreadLocalTest();
		test.start();
	}

	public void start() {
		System.out.println("主线程获取变量：" + threadLocal.get());
		(new Thread(() -> {
			System.out.println("子线程获取变量：" + threadLocal.get());
		})).start();
	}
}
