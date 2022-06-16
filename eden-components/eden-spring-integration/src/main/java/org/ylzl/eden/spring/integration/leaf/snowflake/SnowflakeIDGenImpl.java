package org.ylzl.eden.spring.integration.leaf.snowflake;

import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.spring.integration.leaf.IDGen;

import java.util.Random;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@Slf4j
public class SnowflakeIDGenImpl implements IDGen {

//	private final long twepoch;
	private final long workerIdBits = 10L;

	private final long maxWorkerId = ~(-1L << workerIdBits);
	private final long sequenceBits = 12L;

	private final long workerIdShift = sequenceBits;

	private final long timestampLeftShift = sequenceBits + workerIdBits;

	private final long sequenceMask = ~(-1L << sequenceBits);

	private long workerId;

	private long sequence = 0L;

	private long lastTimestamp = -1L;

	private static final Random RANDOM = new Random();

	/**
	 * 生成 UID
	 *
	 * @return
	 */
	@Override
	public long generateUID() {
		return 0;
	}

	/**
	 * 设置 Key
	 *
	 * @param key
	 */
	@Override
	public void setKey(String key) {

	}

	private long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}

	private long getWorkerId() {
		return workerId;
	}
}
