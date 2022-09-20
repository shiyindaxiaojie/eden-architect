package org.ylzl.eden.commons.lang;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 父子线程基于线程池变量传递测试
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class TransmittableThreadLocalTest {

	private static final TransmittableThreadLocal<Integer> threadLocal = new TransmittableThreadLocal<>();

	private static final ExecutorService threadPool = Executors.newFixedThreadPool(5);

	public static void main(String[] args) throws InterruptedException {
		for (int i = 1; i <= 10; i++) {
			new ParentThread(i).start();
		}
		Thread.sleep(3000);
		threadPool.shutdown();
	}

	static class ParentThread extends Thread {
		private final int index;

		public ParentThread(int index) {
			this.index = index;
		}

		@Override
		public void run() {
			String parentThreadName = Thread.currentThread().getName();
			System.out.println("主线程获取变量：" + parentThreadName + ":" + index);
			threadLocal.set(index);
			threadPool.submit(TtlRunnable.get(new ChildThread(parentThreadName)));
		}
	}

	static class ChildThread implements Runnable{
		private final String parentThreadName;

		public ChildThread(String parentThreadName) {
			this.parentThreadName = parentThreadName;
		}

		@Override
		public void run() {
			System.out.println("子线程获取变量：" + parentThreadName + ":" + threadLocal.get());
		}
	}
}
