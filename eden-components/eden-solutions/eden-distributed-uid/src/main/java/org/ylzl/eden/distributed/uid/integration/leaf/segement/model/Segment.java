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

import java.util.concurrent.atomic.AtomicLong;

/**
 * 号段
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@EqualsAndHashCode
@ToString
public class Segment {

	private AtomicLong value = new AtomicLong(0);

	private volatile long max;

	private volatile int step;

	private SegmentBuffer buffer;

	public Segment(SegmentBuffer buffer) {
		this.buffer = buffer;
	}

	public AtomicLong getValue() {
		return value;
	}

	public void setValue(AtomicLong value) {
		this.value = value;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public SegmentBuffer getBuffer() {
		return buffer;
	}

	public long getIdle() {
		return this.getMax() - getValue().get();
	}
}
