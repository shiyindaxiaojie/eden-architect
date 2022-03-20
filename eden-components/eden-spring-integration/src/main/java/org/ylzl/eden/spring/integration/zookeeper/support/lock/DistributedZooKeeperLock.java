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

package org.ylzl.eden.spring.integration.zookeeper.support.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.ylzl.eden.spring.integration.zookeeper.core.ZookeeperTemplate;

/**
 * Redis 分布式锁
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class DistributedZooKeeperLock extends AbstractZooKeeperLock {

	private static final byte[] empty = new byte[0];

	private final ZookeeperTemplate zooKeeperTemplate;

	public DistributedZooKeeperLock(ZookeeperTemplate zooKeeperTemplate) {
		this.zooKeeperTemplate = zooKeeperTemplate;
	}

	@Override
	public boolean lock(String path, int retryTimes, long sleepMillis) {
		log.debug("Curator client create zooKeeper node lock \"{}\"", path);
		boolean isSuccess = this.create(path);
		while ((!isSuccess) && retryTimes-- > 0) {
			try {
				Thread.sleep(sleepMillis);
			} catch (InterruptedException e) {
				log.error(
					"Curator client create zooKeeper node lock \"{}\", catch InterruptedException: {}",
					path,
					e.getMessage(),
					e);
				return false;
			}
			isSuccess = this.create(path);
		}
		log.debug(
			"Curator client create zooKeeper node lock \"{}\" {}",
			path,
			isSuccess ? "success" : "failed");
		return isSuccess;
	}

	@Override
	public boolean unlock(final String path) {
		log.debug("Curator client remove zooKeeper node lock \"{}\"", path);
		boolean isSuccess = false;
		try {
			zooKeeperTemplate.delete(path);
			isSuccess = true;
		} catch (KeeperException | InterruptedException e) {
			log.error(
				"Curator client remove zooKeeper node lock \"{}\", catch Exception: {}",
				path,
				e.getMessage(),
				e);
		}
		log.debug(
			"Curator client remove zooKeeper node lock \"{}\" {}",
			path,
			isSuccess ? "success" : "failed");
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
