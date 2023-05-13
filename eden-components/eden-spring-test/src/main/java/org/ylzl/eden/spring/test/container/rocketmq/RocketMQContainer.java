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

package org.ylzl.eden.spring.test.container.rocketmq;

import com.github.dockerjava.api.command.InspectContainerResponse;
import lombok.SneakyThrows;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Apache RocketMQ 容器包装
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class RocketMQContainer extends GenericContainer<RocketMQContainer> {

	/**
	 * 默认镜像名称
	 */
	private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("apache/rocketmq");

	/**
	 * 默认镜像标签
	 */
	private static final String DEFAULT_TAG = "4.9.4";

	/**
	 * Name Server 端口
	 */
	private static final int DEFAULT_NAMESRV_PORT = 9876;

	/**
	 * Broker 端口
	 */
	private static final int DEFAULT_BROKER_PORT = 10911;

	/**
	 * Broker VIP 端口
	 */
	private static final int DEFAULT_VIP_PORT = DEFAULT_BROKER_PORT - 2;

	/**
	 * 默认启动超时
	 */
	private static final Duration DEFAULT_STARTUP_TIMEOUT = Duration.ofSeconds(60);

	/**
	 * 读写权限
	 */
	private static final int defaultBrokerPermission = 6;

	public RocketMQContainer() {
		this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
	}

	public RocketMQContainer(String version) {
		this(DEFAULT_IMAGE_NAME.withTag(version));
	}

	public RocketMQContainer(DockerImageName dockerImageName) {
		this(dockerImageName, DEFAULT_STARTUP_TIMEOUT);
	}

	public RocketMQContainer(DockerImageName dockerImageName, Duration startUpTimeOut) {
		super(dockerImageName);
		dockerImageName.assertCompatibleWith(DEFAULT_IMAGE_NAME);
		withExposedPorts(DEFAULT_NAMESRV_PORT, DEFAULT_BROKER_PORT, DEFAULT_VIP_PORT);
		waitingFor(Wait.forListeningPort().withStartupTimeout(startUpTimeOut));
	}

	@Override
	protected void configure() {
		String command = "#!/bin/bash\n";
		command += "./mqnamesrv &\n";
		command += "./mqbroker -n localhost:" + DEFAULT_NAMESRV_PORT;
		withCommand("sh", "-c", command);
	}

	@SneakyThrows
	@Override
	protected void containerIsStarted(InspectContainerResponse containerInfo) {
		List<String> updateBrokerConfigCommands = new ArrayList<>();
		updateBrokerConfigCommands.add(updateBrokerConfig("brokerIP1", getHost()));
		updateBrokerConfigCommands.add(updateBrokerConfig("listenPort", getMappedPort(DEFAULT_BROKER_PORT)));
		updateBrokerConfigCommands.add(updateBrokerConfig("brokerPermission", defaultBrokerPermission));

		final String command = String.join(" && ", updateBrokerConfigCommands);
		ExecResult result = execInContainer(
			"/bin/sh",
			"-c",
			command
		);
		if (result.getExitCode() != 0) {
			throw new IllegalStateException(result.toString());
		}
	}

	private String updateBrokerConfig(final String key, final Object val) {
		return "./mqadmin updateBrokerConfig -b localhost:" + DEFAULT_BROKER_PORT + " -k " + key + " -v " + val;
	}

	public String getNamesrvAddr() {
		return String.format("%s:%s", getHost(), getMappedPort(DEFAULT_NAMESRV_PORT));
	}
}
