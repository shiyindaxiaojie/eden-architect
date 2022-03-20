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

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.ylzl.eden.spring.framework.core.constant.GlobalConstants;
import org.ylzl.eden.spring.integration.zookeeper.config.ZookeeperConfig;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * ZooKeeper 模板
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class ZookeeperTemplate implements InitializingBean, DisposableBean {

	private final CountDownLatch countDownLatch = new CountDownLatch(1);

	private ZooKeeper zookeeper;

	private final ZookeeperConfig zookeeperConfig;

	public ZookeeperTemplate(ZookeeperConfig zookeeperConfig) {
		this.zookeeperConfig = zookeeperConfig;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.zookeeper = new ZooKeeper(zookeeperConfig.getConnectString(),
			zookeeperConfig.getSessionTimeout().getNano(), new ZookeeperWatcher());
		// 由于 ZooKeeper 是异步创建会话的，通过计数器同步连接状态
		countDownLatch.await();
	}

	@Override
	public void destroy() throws Exception {
		if (zookeeper != null) {
			zookeeper.close();
		}
	}

	public String create(String path, byte[] data, ArrayList<ACL> acl, CreateMode createMode)
		throws KeeperException, InterruptedException {
		return zookeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
	}

	public void delete(String path) throws KeeperException, InterruptedException {
		zookeeper.delete(path, -1);
	}

	public byte[] getData(String path) throws KeeperException, InterruptedException {
		return zookeeper.getData(path, true, null);
	}

	public String getDataString(String path)
		throws KeeperException, InterruptedException, UnsupportedEncodingException {
		byte[] data = getData(path);
		return data == null ? null : new String(data, GlobalConstants.DEFAULT_ENCODING);
	}

	/**
	 * ZooKeeper 监视器
	 */
	private class ZookeeperWatcher implements Watcher {

		public void process(WatchedEvent event) {
			if (Event.KeeperState.SyncConnected == event.getState()) {
				countDownLatch.countDown();
			}
		}
	}
}
