/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.boot.integration.zookeeper.support.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Curator 工具实现 ZooKeeper 分布式锁
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class CuratorZooKeeperLock extends AbstractZooKeeperLock {

  private CountDownLatch countDownLatch = new CountDownLatch(1);

  private final CuratorFramework curatorFramework;

  private static ThreadLocal<InterProcessMutex> interProcessMutexs = new ThreadLocal();

  public CuratorZooKeeperLock(CuratorFramework curatorFramework) {
    this.curatorFramework = curatorFramework;
  }

  @Override
  public boolean lock(String path, int retryTimes, long sleepMillis) {
    log.debug("Curator client create zooKeeper node lock \"{}\"", path);
    boolean isSuccess = false;
    InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework, path);
    interProcessMutexs.set(interProcessMutex);
    Long waitTime = sleepMillis * retryTimes;
    try {
      isSuccess = interProcessMutex.acquire(waitTime, TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      log.error(
          "Curator client create zooKeeper node lock \"{}\", catch exception: {}",
          path,
          e.getMessage(),
          e);
    }
    log.debug(
        "Curator client create zooKeeper node lock \"{}\" {}",
        path,
        isSuccess ? "success" : "failed");
    return isSuccess;
  }

  @Override
  public boolean unlock(String path) {
    log.debug("Curator client release zooKeeper node lock：{}", path);
    boolean isSuccess = false;
    InterProcessMutex interProcessMutex = interProcessMutexs.get();
    if (interProcessMutex != null && interProcessMutex.isAcquiredInThisProcess()) {
      try {
        interProcessMutex.release();
        interProcessMutexs.remove();
      } catch (Exception e) {
        log.error(
            "Curator client release zooKeeper node lock \"{}\", catch exception: {}",
            path,
            e.getMessage(),
            e);
      }
    }
    log.debug(
        "Curator client release zooKeeper node lock \"{}\" {}",
        path,
        isSuccess ? "success" : "failed");
    return isSuccess;
  }
}
