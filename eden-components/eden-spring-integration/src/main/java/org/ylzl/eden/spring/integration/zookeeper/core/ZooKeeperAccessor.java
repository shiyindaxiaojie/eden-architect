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

package org.ylzl.eden.spring.integration.zookeeper.core;

import lombok.Getter;
import lombok.Setter;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.CountDownLatch;

/**
 * ZooKeeper 会话
 *
 * @author gyl
 * @since 2.4.x
 */
public class ZooKeeperAccessor implements InitializingBean, DisposableBean {

  private final String connectString;
  private final int sessionTimeout;
  private CountDownLatch semaphore = new CountDownLatch(1);
  @Getter @Setter private ZooKeeper zookeeper;

  public ZooKeeperAccessor(String connectString, int sessionTimeout) {
    this.connectString = connectString;
    this.sessionTimeout = sessionTimeout;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.zookeeper = new ZooKeeper(connectString, sessionTimeout, new ZooKeeperWatcher());
    semaphore.await(); // 由于 ZooKeeper 是异步创建会话的，通过计数器同步连接状态
  }

  @Override
  public void destroy() throws Exception {
    if (zookeeper != null) {
      zookeeper.close();
    }
  }

  /** ZooKeeper 监视器 */
  private class ZooKeeperWatcher implements Watcher {

    public void process(WatchedEvent event) {
      if (Event.KeeperState.SyncConnected == event.getState()) {
        semaphore.countDown();
      }
    }
  }
}
