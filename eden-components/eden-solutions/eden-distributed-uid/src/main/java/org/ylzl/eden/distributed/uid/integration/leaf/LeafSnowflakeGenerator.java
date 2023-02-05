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

package org.ylzl.eden.distributed.uid.integration.leaf;

import lombok.Synchronized;
import org.ylzl.eden.distributed.uid.SnowflakeGenerator;
import org.ylzl.eden.distributed.uid.SnowflakeGeneratorType;
import org.ylzl.eden.distributed.uid.config.SnowflakeGeneratorConfig;
import org.ylzl.eden.distributed.uid.exception.SnowflakeGeneratorException;
import org.ylzl.eden.distributed.uid.integration.leaf.snowflake.coordinator.SnowflakeCoordinatorBuilder;
import org.ylzl.eden.distributed.uid.integration.leaf.snowflake.model.App;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import java.util.Random;

/**
 * Leaf 雪花算法生成器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class LeafSnowflakeGenerator implements SnowflakeGenerator {

	private static final long workerIdBits = 10L;

	private static final long maxWorkerId = ~(-1L << workerIdBits);

	private static final long sequenceBits = 12L;

	private static final long workerIdShift = sequenceBits;

	private static final long timestampLeftShift = sequenceBits + workerIdBits;

	private static final long sequenceMask = ~(-1L << sequenceBits);

	private static final Random RANDOM = new Random();

	private final long workerId;

	private long sequence = 0L;

	private long lastTimestamp = -1L;

	private final long twepoch;

	public LeafSnowflakeGenerator(SnowflakeGeneratorConfig config, App app) {
		this.twepoch = config.getTwepoch();
		AssertUtils.isTrue(timeGen() > twepoch, "Snowflake not support twepoch gt currentTime");

		this.workerId = SnowflakeCoordinatorBuilder.build(config, app).getWorkerId();
		AssertUtils.isTrue(workerId >= 0 && workerId <= maxWorkerId, "Snowflake worker id must between 0 and 1023");
	}

	/**
	 * 生成器类型
	 *
	 * @return 生成器类型
	 */
	@Override
	public String generatorType() {
		return SnowflakeGeneratorType.LEAF.name();
	}

	/**
	 * 获取ID
	 *
	 * @return ID
	 */
	@Synchronized
	@Override
	public long nextId() {
		long timestamp = timeGen();
		if (timestamp < lastTimestamp) {
			long offset = lastTimestamp - timestamp;
			if (offset <= 5) {
				try {
					wait(offset << 1);
					timestamp = timeGen();
					if (timestamp < lastTimestamp) {
						throw new SnowflakeGeneratorException("Snowflake last timestamp gt currentTime");
					}
				} catch (InterruptedException e) {
					throw new SnowflakeGeneratorException("Snowflake next id wait interrupted");
				}
			} else {
				throw new SnowflakeGeneratorException("Snowflake last timestamp offset gt 5");
			}
		}
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				sequence = RANDOM.nextInt(100);
				timestamp = tillNextMillis(lastTimestamp);
			}
		} else {
			sequence = RANDOM.nextInt(100);
		}
		lastTimestamp = timestamp;
		return ((timestamp - twepoch) << timestampLeftShift) | (workerId << workerIdShift) | sequence;
	}

	private long tillNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}

}
