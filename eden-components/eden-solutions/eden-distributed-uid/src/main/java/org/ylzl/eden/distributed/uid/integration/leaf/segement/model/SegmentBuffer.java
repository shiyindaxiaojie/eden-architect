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

package org.ylzl.eden.distributed.uid.integration.leaf.segement.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 号段双缓冲
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@EqualsAndHashCode
@ToString
public class SegmentBuffer {

	/**
	 * Key
	 */
	private String key;

	/**
	 * 当前 segment 的 index
	 */
	private volatile int currentPos; //当前的使用的segment的index

	/**
	 * 下一个 segment 是否处于可切换状态
	 */
	private volatile boolean nextReady;

	/**
	 * 是否初始化完成
	 */
	private volatile boolean initialized;

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

	/**
	 * 双缓冲
	 */
	private final Segment[] segments = new Segment[]{new Segment(this), new Segment(this)};

	/**
	 * 线程是否在运行中
	 */
	private final AtomicBoolean threadRunning = new AtomicBoolean(false);

	/**
	 * 锁
	 */
	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Segment[] getSegments() {
		return segments;
	}

	public Segment getCurrent() {
		return segments[currentPos];
	}

	public int getCurrentPos() {
		return currentPos;
	}

	public int nextPos() {
		return (currentPos + 1) % 2;
	}

	public void switchPos() {
		currentPos = nextPos();
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public boolean isNextReady() {
		return nextReady;
	}

	public void setNextReady(boolean nextReady) {
		this.nextReady = nextReady;
	}

	public AtomicBoolean getThreadRunning() {
		return threadRunning;
	}

	public Lock rLock() {
		return lock.readLock();
	}

	public Lock wLock() {
		return lock.writeLock();
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getMinStep() {
		return minStep;
	}

	public void setMinStep(int minStep) {
		this.minStep = minStep;
	}

	public long getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(long updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
}
