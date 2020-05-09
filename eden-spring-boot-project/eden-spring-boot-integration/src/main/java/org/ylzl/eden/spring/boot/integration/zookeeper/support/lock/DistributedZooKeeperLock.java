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
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.ylzl.eden.spring.boot.integration.zookeeper.core.ZooKeeperTemplate;

/**
 * Redis 分布式锁
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class DistributedZooKeeperLock extends AbstractZooKeeperLock {

  private static final byte[] empty = new byte[0];

  private final ZooKeeperTemplate zooKeeperTemplate;

  public DistributedZooKeeperLock(ZooKeeperTemplate zooKeeperTemplate) {
    this.zooKeeperTemplate = zooKeeperTemplate;
  }

  @Override
  public boolean lock(String path, int retryTimes, long sleepMillis) {
    log.debug("创建 ZooKeeper 节点锁：{}", path);
    boolean isSuccess = this.create(path);
    while ((!isSuccess) && retryTimes-- > 0) {
      try {
        Thread.sleep(sleepMillis);
      } catch (InterruptedException e) {
        log.error("创建 ZooKeeper 节点锁：{}，发生线程中断异常：{}", path, e.getMessage(), e);
        return false;
      }
      isSuccess = this.create(path);
    }
    log.debug("创建 ZooKeeper 节点锁：{}，执行{}", path, isSuccess ? "成功" : "失败");
    return isSuccess;
  }

  @Override
  public boolean unlock(final String path) {
    log.debug("释放 ZooKeeper 节点锁：{}", path);
    boolean isSuccess = false;
    try {
      zooKeeperTemplate.delete(path);
      isSuccess = true;
    } catch (KeeperException | InterruptedException e) {
      log.error("释放 ZooKeeper 节点锁：{}，抛出异常：{}", path, e.getMessage(), e);
    }
    log.debug("释放 ZooKeeper 节点锁：{}，执行{}", path, isSuccess ? "成功" : "失败");
    return isSuccess;
  }

  private boolean create(String path) {
    try {
      zooKeeperTemplate.create(
          path, empty, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
    } catch (KeeperException | InterruptedException ignored) {
      return false;
    }
    return true;
  }
}
