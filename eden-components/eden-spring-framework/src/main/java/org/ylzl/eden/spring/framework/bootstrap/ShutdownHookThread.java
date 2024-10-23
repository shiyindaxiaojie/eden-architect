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

package org.ylzl.eden.spring.framework.bootstrap;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 关闭回调线程
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class ShutdownHookThread extends Thread {

	private volatile boolean hasShutdown = false;

	private AtomicInteger shutdownTimes = new AtomicInteger(0);

	private final Callable callback;

	public ShutdownHookThread(Callable callback) {
		super("ShutdownHook");
		this.callback = callback;
	}

	@Override
	public void run() {
		synchronized (this) {
			log.info("shutdown hook was invoked {} times.", this.shutdownTimes.incrementAndGet());
			if (!this.hasShutdown) {
				this.hasShutdown = true;
				long beginTime = System.currentTimeMillis();
				try {
					this.callback.call();
				} catch (Exception e) {
					log.error("shutdown hook callback invoked failure.", e);
				}
				long costTime = System.currentTimeMillis() - beginTime;
				log.info("shutdown hook done, cost {} ms.",costTime);
			}
		}
	}
}
