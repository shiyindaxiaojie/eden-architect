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
