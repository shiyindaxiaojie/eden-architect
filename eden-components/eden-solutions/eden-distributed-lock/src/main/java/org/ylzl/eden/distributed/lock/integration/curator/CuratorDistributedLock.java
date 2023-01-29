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

package org.ylzl.eden.distributed.lock.integration.curator;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.distributed.lock.DistributedLock;
import org.ylzl.eden.distributed.lock.DistributedLockType;
import org.ylzl.eden.distributed.lock.exception.DistributedLockAcquireException;
import org.ylzl.eden.distributed.lock.exception.DistributedLockReleaseException;
import org.ylzl.eden.distributed.lock.exception.DistributedLockTimeoutException;

import java.util.concurrent.TimeUnit;

/**
 * Curator 分布式锁
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class CuratorDistributedLock implements DistributedLock {

    private static final TransmittableThreadLocal<InterProcessMutex> threadLocal = new TransmittableThreadLocal<>();

    private final CuratorFramework curatorFramework;

    /**
     * 锁类型
     *
     * @return 锁类型
     */
    @Override
    public String lockType() {
        return DistributedLockType.CURATOR.name();
    }

    /**
     * 阻塞加锁
     *
     * @param key 锁对象
     */
    @Override
    public boolean lock(@NonNull String key) {
        log.debug("Curator create lock '{}'", key);
        if (!key.startsWith(Strings.SLASH)) {
            throw new DistributedLockAcquireException("Invalid curator lock: " + key);
        }
        InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework, key);
        try {
            interProcessMutex.acquire();
            threadLocal.set(interProcessMutex);
        } catch (Exception e) {
            log.error("Curator create lock '{}', catch exception: {}", key, e.getMessage(), e);
            throw new DistributedLockAcquireException(e);
        }
        log.debug("Curator create lock '{}' successfully", key);
        return true;
    }

    /**
     * 加锁
     *
     * @param key      锁对象
     * @param waitTime 等待时间
     * @param timeUnit 时间单位
     * @return 加锁是否成功
     */
    @Override
    public boolean lock(@NonNull String key, int waitTime, TimeUnit timeUnit) {
        log.debug("Curator create lock '{}' with waitTime '{}'", key, waitTime);
        if (!key.startsWith(Strings.SLASH)) {
            throw new DistributedLockAcquireException("Invalid curator lock: " + key);
        }
        InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework, key);
        boolean isSuccess;
        try {
            isSuccess = interProcessMutex.acquire(waitTime, timeUnit);
        } catch (Exception e) {
            log.error("Curator create lock '{}' with waitTime '{}', catch exception: {}", key, waitTime, e.getMessage(), e);
            throw new DistributedLockTimeoutException(e);
        }
        if (isSuccess) {
            threadLocal.set(interProcessMutex);
            log.debug("Curator create lock '{}' with waitTime '{} successfully", key, waitTime);
        } else {
            log.warn("Curator create lock '{}' with waitTime '{} failed", key, waitTime);
        }
        return isSuccess;
    }

    /**
     * 释放锁
     *
     * @param key 锁对象
     */
    @Override
    public void unlock(@NonNull String key) {
        log.debug("Curator release lock '{}'", key);
        InterProcessMutex interProcessMutex = threadLocal.get();
        if (interProcessMutex == null) {
			log.warn("Curator release lock '{}' failed due to thread local is null", key);
			return;
        }
        if (!interProcessMutex.isAcquiredInThisProcess()) {
			log.warn("Curator release lock '{}' failed that is not acquired in process", key);
			return;
        }
        try {
            interProcessMutex.release();
            threadLocal.remove();
        } catch (Exception e) {
            log.error("Curator release lock: {}, catch exception: {}", key, e.getMessage(), e);
            throw new DistributedLockReleaseException(e.getMessage());
        }
		log.debug("Curator release lock '{}' successfully", key);
    }
}
