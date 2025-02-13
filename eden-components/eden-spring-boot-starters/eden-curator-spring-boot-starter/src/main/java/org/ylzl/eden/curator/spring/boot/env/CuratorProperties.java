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

package org.ylzl.eden.curator.spring.boot.env;

import lombok.Data;
import org.apache.curator.RetryPolicy;
import org.apache.curator.connection.ConnectionHandlingPolicy;
import org.apache.curator.ensemble.EnsembleProvider;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.api.CompressionProvider;
import org.apache.curator.framework.schema.SchemaSet;
import org.apache.curator.framework.state.ConnectionStateErrorPolicy;
import org.apache.curator.framework.state.ConnectionStateListenerManagerFactory;
import org.apache.curator.utils.ZookeeperFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * Curator 配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = CuratorProperties.PREFIX)
public class CuratorProperties {

	public static final String PREFIX = "spring.cloud.zookeeper.curator";

	private EnsembleProvider ensembleProvider;

	private int sessionTimeoutMs;

	private int connectionTimeoutMs;

	private int maxCloseWaitMs;

	private RetryPolicy retryPolicy;

	private ThreadFactory threadFactory;

	private String namespace;

	private List<AuthInfo> authInfos;

	private byte[] defaultData;

	private CompressionProvider compressionProvider;

	private ZookeeperFactory zookeeperFactory;

	private ACLProvider aclProvider;

	private boolean canBeReadOnly;

	private boolean useContainerParentsIfAvailable;

	private ConnectionStateErrorPolicy connectionStateErrorPolicy;

	private ConnectionHandlingPolicy connectionHandlingPolicy;

	private SchemaSet schemaSet;

	private boolean zk34CompatibilityMode;

	private int waitForShutdownTimeoutMs;

	private Executor runSafeService;

	private ConnectionStateListenerManagerFactory connectionStateListenerManagerFactory;
}
