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

package org.ylzl.eden.distributed.uid.integration.leaf.snowflake.coordinator.spi;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.ylzl.eden.commons.io.FileUtils;
import org.ylzl.eden.commons.lang.MessageFormatUtils;
import org.ylzl.eden.distributed.uid.config.SnowflakeGeneratorConfig;
import org.ylzl.eden.distributed.uid.integration.leaf.snowflake.model.App;
import org.ylzl.eden.distributed.uid.integration.leaf.snowflake.model.Endpoint;
import org.ylzl.eden.distributed.uid.integration.leaf.snowflake.coordinator.SnowflakeCoordinator;
import org.ylzl.eden.distributed.uid.exception.SnowflakeGeneratorException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Zookeeper 雪花算法协调器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class ZookeeperSnowflakeCoordinator implements SnowflakeCoordinator {

	private static final String LEAF_SCHEDULE_NAME = "leaf-zookeeper-schedule";

	private static final String ZK_PATH_PATTERN = "/leaf/snowflake/{}/node";

	private static final String NODE_PREFIX_PATTERN = ZK_PATH_PATTERN + "/{}:{}-";

	private static final String CONF_PATH_PATTERN = System.getProperty("java.io.tmpdir") + "/leaf/{}/{}/worker-id.properties";

	private volatile CuratorFramework curatorFramework;

	private SnowflakeGeneratorConfig config;

	private App app;

	private long lastUpdateTime;

	/**
	 * 获取 workerId
	 *
	 * @return workerId
	 */
	@Override
	public long getWorkerId() {
		this.startCurator();
		int workerId = 0;
		String zkPath = MessageFormatUtils.format(ZK_PATH_PATTERN, config.getName());
		String zkNode;
		try {
			Stat stat = curatorFramework.checkExists().forPath(zkPath);
			if (stat == null) {
				zkNode = createNode();
			} else {
				Map<String, Integer> nodeIds = Maps.newHashMap();
				Map<String, String> nodes = Maps.newHashMap();
				List<String> keys = curatorFramework.getChildren().forPath(zkPath);
				for (String key : keys) {
					String[] nodeKey = key.split("-");
					nodeIds.put(nodeKey[0], Integer.parseInt(nodeKey[1]));
					nodes.put(nodeKey[0], key);
				}
				String listenAddress = app.getIp() + ":" + app.getPort();
				Integer nodeWorkerId = nodeIds.get(listenAddress);
				if (nodeWorkerId != null) {
					zkNode = zkPath + "/" + nodes.get(listenAddress);
					workerId = nodeWorkerId;
					checkEndpointTimeStamp(zkNode);
				} else {
					zkNode = createNode();
					String[] nodeKey = zkNode.split("-");
					workerId = Integer.parseInt(nodeKey[1]);
				}
			}
			this.updateLocalWorkerId(workerId);
			this.scheduledEndpoint(zkNode);
		} catch (Exception e) {
			throw new SnowflakeGeneratorException(e.getMessage(), e);
		}
		return workerId;
	}

	/**
	 * 雪花算法协调器配置
	 *
	 * @param config 雪花算法协调器配置
	 * @return this
	 */
	@Override
	public SnowflakeCoordinator config(SnowflakeGeneratorConfig config) {
		this.config = config;
		return this;
	}

	/**
	 * 设置应用信息
	 *
	 * @param app 应用信息
	 */
	@Override
	public SnowflakeCoordinator app(App app) {
		this.app = app;
		return this;
	}

	/**
	 * 启动 CuratorFramework
	 */
	private void startCurator() {
		if (curatorFramework == null) {
			synchronized (this) {
				if (curatorFramework == null) {
					curatorFramework = CuratorFrameworkFactory.builder()
						.connectString(config.getCoordinator().getZookeeper().getConnectString())
						.retryPolicy(new RetryUntilElapsed(1000, 4))
						.connectionTimeoutMs(10000)
						.sessionTimeoutMs(6000)
						.build();
				}
			}
		}
		if (curatorFramework.getState() != CuratorFrameworkState.STARTED) {
			curatorFramework.start();
		}
	}

	/**
	 * 创建 Zookeeper 顺序节点
	 *
	 * @return 创建成功后返回的 Zookeeper 的顺序节点
	 */
	private String createNode() {
		String prefix = MessageFormatUtils.format(NODE_PREFIX_PATTERN, config.getName(),
			app.getIp(), app.getPort());
		String endpoint = Endpoint.build(app.getIp(), app.getPort());
		try {
			return curatorFramework.create()
				.creatingParentsIfNeeded()
				.withMode(CreateMode.PERSISTENT_SEQUENTIAL)
				.forPath(prefix, endpoint.getBytes());
		} catch (Exception e) {
			throw new SnowflakeGeneratorException("Create zookeeper node '" + prefix + "' failed", e);
		}
	}

	/**
	 * 更新本地 workerId，确保机器重启时能够正常启动
	 *
	 * @param workerId
	 * @throws IOException
	 */
	private void updateLocalWorkerId(int workerId) throws IOException {
		String pathname = MessageFormatUtils.format(CONF_PATH_PATTERN, config.getName(), workerId);
		File leafConfFile = new File(pathname);
		if (leafConfFile.exists()) {
			log.info("Update local config file '{}' with worker id is {}", pathname, workerId);
			FileUtils.writeStringToFile(leafConfFile, "workerId=" + workerId, Charset.defaultCharset(), false);
		} else {
			if (leafConfFile.getParentFile().isDirectory() && !leafConfFile.getParentFile().exists()) {
				leafConfFile.getParentFile().mkdirs();
			}
			log.info("Initialize local config file '{}' with worker id is {}", pathname, workerId);
			if (leafConfFile.createNewFile()) {
				FileUtils.writeStringToFile(leafConfFile, "workerId=" + workerId, Charset.defaultCharset(), false);
				log.info("Write local file cache worker id is  {}", workerId);
			}
		}
	}

	/**
	 * 检查节点上报时间
	 * <br/> 该节点的时间不能小于最后一次上报的时间
	 *
	 * @param zkNode Zookeeper 节点
	 */
	private void checkEndpointTimeStamp(String zkNode) {
		byte[] bytes;
		try {
			bytes = curatorFramework.getData().forPath(zkNode);
		} catch (Exception e) {
			throw new SnowflakeGeneratorException("Get zookeeper node '" + zkNode + "' data error");
		}
		Endpoint endPoint = Endpoint.parse(new String(bytes));
		if (endPoint.getTimestamp() > System.currentTimeMillis()) {
			throw new SnowflakeGeneratorException("Check endpoint timestamp invalid");
		}
	}

	/**
	 * 定时上报节点时间
	 *
	 * @param zkNode Zookeeper 节点
	 */
	private void scheduledEndpoint(String zkNode) {
		Executors.newSingleThreadScheduledExecutor(r -> {
			Thread thread = new Thread(r, LEAF_SCHEDULE_NAME);
			thread.setDaemon(true);
			return thread;
		}).scheduleWithFixedDelay(() -> {
			if (System.currentTimeMillis() < lastUpdateTime) {
				return;
			}
			try {
				curatorFramework.setData().forPath(zkNode, Endpoint.build(app.getIp(), app.getPort()).getBytes());
			} catch (Exception e) {
				throw new SnowflakeGeneratorException("Scheduled endpoint timestamp error", e);
			}
			lastUpdateTime = System.currentTimeMillis();
		}, 1L, 3L, TimeUnit.SECONDS); // 每 3 秒上报一次数据

	}
}
