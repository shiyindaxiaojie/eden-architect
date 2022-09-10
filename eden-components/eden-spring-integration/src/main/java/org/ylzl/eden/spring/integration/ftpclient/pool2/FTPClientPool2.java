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

package org.ylzl.eden.spring.integration.ftpclient.pool2;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.ylzl.eden.spring.integration.ftpclient.core.FTPClientPool;

/**
 * FTP 客户端连接池
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class FTPClientPool2 extends GenericObjectPool<FTPClient> implements FTPClientPool {

	public FTPClientPool2(PooledObjectFactory<FTPClient> factory) {
		super(factory);
	}

	public FTPClientPool2(
		PooledObjectFactory<FTPClient> factory, GenericObjectPoolConfig<FTPClient> config) {
		super(factory, config);
	}

	public FTPClientPool2(
		PooledObjectFactory<FTPClient> factory,
		GenericObjectPoolConfig<FTPClient> config,
		AbandonedConfig abandonedConfig) {
		super(factory, config, abandonedConfig);
	}
}
