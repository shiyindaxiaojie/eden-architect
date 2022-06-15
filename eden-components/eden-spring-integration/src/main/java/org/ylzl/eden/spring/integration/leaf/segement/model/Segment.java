package org.ylzl.eden.spring.integration.leaf.segement.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 号段
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@EqualsAndHashCode
@ToString
@Data
public class Segment {

	/**
	 * 号段值
	 */
	private AtomicLong value = new AtomicLong(0);

	/**
	 * 最大值
	 */
	private volatile long max;

	/**
	 * 步长
	 */
	private volatile int step;

	/**
	 * 号段缓冲区
	 */
	private SegmentBuffer buffer;

	public Segment(SegmentBuffer buffer) {
		this.buffer = buffer;
	}
}
