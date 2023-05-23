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

package org.ylzl.eden.spring.test.container.mysql;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * MySQL8 容器包装
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class MySQL8Container extends MySQLContainer<MySQL8Container> {

	/**
	 * 默认镜像名称
	 */
	private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("mysql");

	/**
	 * 默认镜像标签
	 */
	private static final String DEFAULT_TAG = "8.0.32";

	/**
	 * 默认启动超时
	 */
	private static final Duration DEFAULT_STARTUP_TIMEOUT = Duration.ofSeconds(60);

	public MySQL8Container() {
		this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG), DEFAULT_STARTUP_TIMEOUT);
	}

	public MySQL8Container(String dockerImageName) {
		this(DockerImageName.parse(dockerImageName), DEFAULT_STARTUP_TIMEOUT);
	}

	public MySQL8Container(DockerImageName dockerImageName) {
		this(dockerImageName, DEFAULT_STARTUP_TIMEOUT);
	}

	public MySQL8Container(DockerImageName dockerImageName, Duration startUpTimeOut) {
		super(dockerImageName);
		waitingFor(Wait.forListeningPort().withStartupTimeout(startUpTimeOut));
	}
}
