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
