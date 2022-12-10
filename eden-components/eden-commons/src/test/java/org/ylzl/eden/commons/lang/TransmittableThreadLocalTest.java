/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
