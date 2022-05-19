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
import org.ylzl.eden.spring.framework.bootstrap.constant.GlobalConstants;
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

	private static final String CONNECTION_ZOOKEEPER_URL = "Connection zookeeper, url:{}";
	private static final String CONNECTION_ZOOKEEPER_SUCCESS = "Connection zookeeper success";

	private ZooKeeper zookeeper;

	private final CountDownLatch countDownLatch = new CountDownLatch(1);

	private final ZookeeperConfig zookeeperConfig;

	public ZookeeperTemplate(ZookeeperConfig zookeeperConfig) {
		this.zookeeperConfig = zookeeperConfig;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String url = zookeeperConfig.getConnectString();
		log.debug(CONNECTION_ZOOKEEPER_URL, url);
		this.zookeeper = new ZooKeeper(url,
			zookeeperConfig.getSessionTimeout().getNano(), event -> {
			if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
				log.debug(CONNECTION_ZOOKEEPER_SUCCESS);
				countDownLatch.countDown();
			}
		});
		// 由于 ZooKeeper 是异步创建会话的，通过计数器同步连接状态
		countDownLatch.await();
	}

	@Override
	public void destroy() throws Exception {
		if (zookeeper != null) {
			zookeeper.close();
		}
	}

	public String create(String path, byte[] data, ArrayList<ACL> acl, CreateMode createMode) {
		try {
			return zookeeper.create(path, data, acl, createMode);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
			Thread.currentThread().interrupt();
			throw new ZookeeperException("Create path '" + path + "' interrupted");
		} catch (KeeperException e) {
			log.error(e.getMessage(), e);
			throw new ZookeeperException("Create path '" + path + "' failed");
		}
	}

	public String create(String path, byte[] data) {
		return create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
	}

	public void delete(String path) {
		try {
			zookeeper.delete(path, -1);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
			Thread.currentThread().interrupt();
			throw new ZookeeperException("Delete path '" + path + "' interrupted");
		} catch (KeeperException e) {
			log.error(e.getMessage(), e);
			throw new ZookeeperException("Delete path '" + path + "' failed");
		}
	}

	public byte[] getData(String path) {
		try {
			return zookeeper.getData(path, true, null);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
			Thread.currentThread().interrupt();
			throw new ZookeeperException("Get data from path '" + path + "' interrupted");
		} catch (KeeperException e) {
			log.error(e.getMessage(), e);
			throw new ZookeeperException("Get data from path '" + path + "' failed");
		}
	}

	public String getDataString(String path) {
		byte[] data = getData(path);
		if (data == null) {
			return null;
		}
		try {
			return new String(data, GlobalConstants.DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
			throw new ZookeeperException("Get data from path '" + path + "' failed");
		}
	}
}
