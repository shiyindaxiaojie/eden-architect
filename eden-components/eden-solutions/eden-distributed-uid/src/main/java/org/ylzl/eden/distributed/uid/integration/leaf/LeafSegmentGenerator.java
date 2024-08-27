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

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.ylzl.eden.distributed.uid.SegmentGenerator;
import org.ylzl.eden.distributed.uid.config.SegmentGeneratorConfig;
import org.ylzl.eden.distributed.uid.exception.SegmentGeneratorException;
import org.ylzl.eden.distributed.uid.integration.leaf.segement.dao.LeafAllocDAO;
import org.ylzl.eden.distributed.uid.integration.leaf.segement.model.LeafAlloc;
import org.ylzl.eden.distributed.uid.integration.leaf.segement.model.Segment;
import org.ylzl.eden.distributed.uid.integration.leaf.segement.model.SegmentBuffer;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Leaf 号段生成器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class LeafSegmentGenerator implements SegmentGenerator {

	private static final long LIQUIBASE_SLOWNESS_THRESHOLD = 5;

	private final SegmentGeneratorConfig config;

	private final LeafAllocDAO leafAllocDAO;

	private final ExecutorService executorService = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 60L,
		TimeUnit.SECONDS, new SynchronousQueue<>(), new UpdateThreadFactory());

	private final Map<String, SegmentBuffer> cache = new ConcurrentHashMap<>();

	private final boolean initialized;

	public LeafSegmentGenerator(SegmentGeneratorConfig config, DataSource dataSource) {
		this.config = config;
		if (config.getLiquibase().isEnabled()) {
			this.initDb(dataSource);
		}
		this.leafAllocDAO = new LeafAllocDAO(dataSource);
		this.updateCacheFromDb();
		this.initialized = true;
		this.updateCacheFromDbAtEveryMinute();
	}

	/**
	 * 从号段获取ID
	 *
	 * @return 号段
	 */
	@Override
	public long nextId() {
		if (!initialized) {
			throw new SegmentGeneratorException("Database segment generator not initialized");
		}
		String key = config.getTenant();
		if (!cache.containsKey(key)) {
			throw new SegmentGeneratorException("Database segment cache contains key '" + key + "', please check database");
		}
		SegmentBuffer buffer = cache.get(key);
		if (!buffer.isInitialized()) {
			synchronized (buffer) {
				if (!buffer.isInitialized()) {
					try {
						updateSegmentFromDb(key, buffer.getCurrent());
						log.debug("Initialize buffer and update leaf key {} {} from db", key, buffer.getCurrent());
						buffer.setInitialized(true);
					} catch (Exception e) {
						log.warn("Initialize buffer {} catch exception", buffer.getCurrent(), e);
					}
				}
			}
		}
		return getIdFromSegmentBuffer(cache.get(key));
	}

	private void initDb(DataSource dataSource) {
		SpringLiquibase liquibase = buildLiquibase(dataSource);
		StopWatch watch = new StopWatch();
		watch.start();
		try {
			liquibase.afterPropertiesSet();
		} catch (LiquibaseException e) {
			throw new SegmentGeneratorException("Leaf liquibase has initialized your database error", e);
		}
		watch.stop();
		log.debug("Leaf liquibase has initialized your database in {} ms", watch.getTotalTimeMillis());
		if (watch.getTotalTimeMillis() > LIQUIBASE_SLOWNESS_THRESHOLD * 1000L) {
			log.warn("Leaf liquibase took more than {} seconds to initialized your database!", LIQUIBASE_SLOWNESS_THRESHOLD);
		}
	}

	private SpringLiquibase buildLiquibase(DataSource dataSource) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog(this.config.getLiquibase().getChangeLog());
		liquibase.setClearCheckSums(this.config.getLiquibase().isClearChecksums());
		liquibase.setContexts(this.config.getLiquibase().getContexts());
		liquibase.setDefaultSchema(this.config.getLiquibase().getDefaultSchema());
		liquibase.setLiquibaseSchema(this.config.getLiquibase().getLiquibaseSchema());
		liquibase.setLiquibaseTablespace(this.config.getLiquibase().getLiquibaseTablespace());
		liquibase.setDatabaseChangeLogTable(this.config.getLiquibase().getDatabaseChangeLogTable());
		liquibase.setDatabaseChangeLogLockTable(this.config.getLiquibase().getDatabaseChangeLogLockTable());
		liquibase.setDropFirst(this.config.getLiquibase().isDropFirst());
		liquibase.setShouldRun(this.config.getLiquibase().isEnabled());
		liquibase.setLabels(this.config.getLiquibase().getLabels());
		liquibase.setChangeLogParameters(this.config.getLiquibase().getParameters());
		liquibase.setRollbackFile(this.config.getLiquibase().getRollbackFile());
		liquibase.setTestRollbackOnUpdate(this.config.getLiquibase().isTestRollbackOnUpdate());
		liquibase.setTag(this.config.getLiquibase().getTag());
		return liquibase;
	}

	private void updateCacheFromDb() {
		log.debug("Leaf update cache from db");
		try {
			List<String> dbTags = leafAllocDAO.getAllTags();
			if (dbTags == null || dbTags.isEmpty()) {
				return;
			}
			List<String> cacheTags = new ArrayList<String>(cache.keySet());
			Set<String> insertTagsSet = new HashSet<>(dbTags);
			Set<String> removeTagsSet = new HashSet<>(cacheTags);
			for (String tag : cacheTags) {
				insertTagsSet.remove(tag);
			}
			for (String tag : insertTagsSet) {
				SegmentBuffer buffer = new SegmentBuffer();
				buffer.setKey(tag);
				Segment segment = buffer.getCurrent();
				segment.setValue(new AtomicLong(0));
				segment.setMax(0);
				segment.setStep(0);
				cache.put(tag, buffer);
				log.debug("Leaf add tag {} from db to IdCache", tag);
			}
			for (String tag : dbTags) {
				removeTagsSet.remove(tag);
			}
			for (String tag : removeTagsSet) {
				cache.remove(tag);
				log.debug("Leaf remove tag {} from IdCache", tag);
			}
		} catch (Exception e) {
			log.warn("Leaf update cache from db exception", e);
		}
	}

	private void updateCacheFromDbAtEveryMinute() {
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(r -> {
			Thread t = new Thread(r);
			t.setName("leaf-check-id-cache-thread");
			t.setDaemon(true);
			return t;
		});
		service.scheduleWithFixedDelay(this::updateCacheFromDb, 60, 60, TimeUnit.SECONDS);
	}

	private void updateSegmentFromDb(String key, Segment segment) {
		SegmentBuffer buffer = segment.getBuffer();
		LeafAlloc leafAlloc;
		if (!buffer.isInitialized()) {
			leafAlloc = leafAllocDAO.updateMaxIdAndGetLeafAlloc(key);
			buffer.setStep(leafAlloc.getStep());
			buffer.setMinStep(leafAlloc.getStep());
		} else if (buffer.getUpdateTimestamp() == 0) {
			leafAlloc = leafAllocDAO.updateMaxIdAndGetLeafAlloc(key);
			buffer.setUpdateTimestamp(System.currentTimeMillis());
			buffer.setStep(leafAlloc.getStep());
			buffer.setMinStep(leafAlloc.getStep());
		} else {
			long duration = System.currentTimeMillis() - buffer.getUpdateTimestamp();
			int nextStep = buffer.getStep();
			if (duration < config.getSegmentTtl()) {
				if (config.getMaxStep() >= nextStep * 2) {
					nextStep = nextStep * 2;
				}
			} else if (duration >= config.getSegmentTtl() * 2) {
				nextStep = nextStep / 2 >= buffer.getMinStep() ? nextStep / 2 : nextStep;
			}
			log.debug("leafKey[{}], step[{}], duration[{}mins], nextStep[{}]", key, buffer.getStep(), String.format("%.2f", ((double) duration / (1000 * 60))), nextStep);
			LeafAlloc temp = new LeafAlloc();
			temp.setKey(key);
			temp.setStep(nextStep);
			leafAlloc = leafAllocDAO.updateMaxIdByCustomStepAndGetLeafAlloc(temp);
			buffer.setUpdateTimestamp(System.currentTimeMillis());
			buffer.setStep(nextStep);
			buffer.setMinStep(leafAlloc.getStep());
		}
		long value = leafAlloc.getMaxId() - buffer.getStep();
		segment.getValue().set(value);
		segment.setMax(leafAlloc.getMaxId());
		segment.setStep(buffer.getStep());
	}

	private long getIdFromSegmentBuffer(final SegmentBuffer buffer) {
		while (true) {
			buffer.rLock().lock();
			try {
				final Segment segment = buffer.getCurrent();
				if (!buffer.isNextReady() && (segment.getIdle() < 0.9 * segment.getStep())
					&& buffer.getThreadRunning().compareAndSet(false, true)) {
					executorService.execute(() -> {
						Segment next = buffer.getSegments()[buffer.nextPos()];
						boolean updateOk = false;
						try {
							updateSegmentFromDb(buffer.getKey(), next);
							updateOk = true;
							log.info("Leaf update segment {} from db {}", buffer.getKey(), next);
						} catch (Exception e) {
							log.error("Leaf update segment {} from db {} error", buffer.getKey(), e);
						} finally {
							if (updateOk) {
								buffer.wLock().lock();
								buffer.setNextReady(true);
								buffer.getThreadRunning().set(false);
								buffer.wLock().unlock();
							} else {
								buffer.getThreadRunning().set(false);
							}
						}
					});
				}
				long value = segment.getValue().getAndIncrement();
				if (value < segment.getMax()) {
					return value;
				}
			} finally {
				buffer.rLock().unlock();
			}
			waitAndSleep(buffer);
			buffer.wLock().lock();
			try {
				final Segment segment = buffer.getCurrent();
				long value = segment.getValue().getAndIncrement();
				if (value < segment.getMax()) {
					return value;
				}
				if (buffer.isNextReady()) {
					buffer.switchPos();
					buffer.setNextReady(false);
				} else {
					throw new SegmentGeneratorException("Both two segments in '" + buffer + "' are not ready!");
				}
			} finally {
				buffer.wLock().unlock();
			}
		}
	}

	private void waitAndSleep(SegmentBuffer buffer) {
		int roll = 0;
		while (buffer.getThreadRunning().get()) {
			roll += 1;
			if (roll > 10000) {
				try {
					TimeUnit.MILLISECONDS.sleep(10);
					break;
				} catch (InterruptedException e) {
					log.warn("Thread {} Interrupted", Thread.currentThread().getName());
					break;
				}
			}
		}
	}

	public static class UpdateThreadFactory implements ThreadFactory {

		private static int threadInitNumber = 0;

		private static synchronized int nextThreadNum() {
			return threadInitNumber++;
		}

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "leaf-segment-update-" + nextThreadNum());
		}
	}
}
