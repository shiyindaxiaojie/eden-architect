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

package org.ylzl.eden.spring.integration.leaf.segement.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 号段缓冲区
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@EqualsAndHashCode
@ToString
@Data
public class SegmentBuffer {

	/**
	 * Key
	 */
	private String key;

	/**
	 * 双缓冲
	 */
	private Segment[] segments;

	/**
	 * 当前 Segment 的下标
	 */
	private volatile int currentSegmentPos;

	/**
	 * 下一个 Segment 是否处于可切换状态
	 */
	private volatile boolean nextSegmentReady;

	/**
	 * 是否初始化完成
	 */
	private volatile boolean initialized;

	/**
	 * 线程是否在运行中
	 */
	private final AtomicBoolean isRunning;

	/**
	 * 读写锁
	 */
	private final ReadWriteLock lock;

	/**
	 * 步长
	 */
	private volatile int step;

	/**
	 * 最小步长
	 */
	private volatile int minStep;

	/**
	 * 更新时间戳
	 */
	private volatile long updateTimestamp;

	public SegmentBuffer() {
		segments = new Segment[]{new Segment(this), new Segment(this)};
		currentSegmentPos = 0;
		nextSegmentReady = false;
		initialized = false;
		isRunning = new AtomicBoolean(false);
		lock = new ReentrantReadWriteLock();
	}
}
