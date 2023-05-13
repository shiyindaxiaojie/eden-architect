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

package org.ylzl.eden.spring.test.container.cat;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * Cat Home 容器包装
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class CatHomeContainer extends GenericContainer<CatHomeContainer> {

	/**
	 * 默认镜像名称
	 */
	private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("shiyindaxiaojie/cat-home");

	/**
	 * 默认镜像标签
	 */
	private static final String DEFAULT_TAG = "3.1.0";

	/**
	 * 默认服务端口
	 */
	public static final int DEFAULT_PORT = 8080;

	/**
	 * 默认启动超时
	 */
	public static final Duration DEFAULT_STARTUP_TIMEOUT = Duration.ofSeconds(60);

	public CatHomeContainer() {
		this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG), DEFAULT_STARTUP_TIMEOUT);
	}

	public CatHomeContainer(String version) {
		this(DEFAULT_IMAGE_NAME.withTag(version), DEFAULT_STARTUP_TIMEOUT);
	}

	public CatHomeContainer(DockerImageName dockerImageName, Duration startUpTimeOut) {
		super(dockerImageName);
		withExposedPorts(DEFAULT_PORT);
		waitingFor(Wait.forListeningPort().withStartupTimeout(startUpTimeOut));

		addEnv("JVM_XMS", "256m");
		addEnv("JVM_XMX", "512m");
		addEnv("JVM_XMN", "256m");
	}
}
