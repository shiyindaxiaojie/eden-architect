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
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.ylzl.eden.spring.framework.core.constant.SpringFrameworkConstants;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * ZooKeeper 模板
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class ZooKeeperTemplate extends ZooKeeperAccessor {

	public ZooKeeperTemplate(String connectString, int sessionTimeout) {
		super(connectString, sessionTimeout);
	}

	public String create(String path, byte[] data, ArrayList<ACL> acl, CreateMode createMode)
		throws KeeperException, InterruptedException {
		return getZookeeper()
			.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
	}

	public void delete(String path) throws KeeperException, InterruptedException {
		getZookeeper().delete(path, -1);
	}

	public byte[] getData(String path) throws KeeperException, InterruptedException {
		return getZookeeper().getData(path, true, null);
	}

	public String getDataString(String path)
		throws KeeperException, InterruptedException, UnsupportedEncodingException {
		byte[] data = getData(path);
		return data == null ? null : new String(data, SpringFrameworkConstants.DEFAULT_ENCODING);
	}
}
