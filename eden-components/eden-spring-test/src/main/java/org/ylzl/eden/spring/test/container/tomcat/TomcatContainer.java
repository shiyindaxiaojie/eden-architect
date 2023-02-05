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

package org.ylzl.eden.spring.test.container.tomcat;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;
import java.time.Duration;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class TomcatContainer extends GenericContainer<TomcatContainer> {

	/** 默认镜像名称 */
	private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("tomcat");

	/** 默认镜像标签 */
	private static final String DEFAULT_TAG = "10.1.5";

	/** 默认端口 */
	private static final int DEFAULT_PORT = 8080;

	/** 默认启动超时 */
	private static final Duration DEFAULT_STARTUP_TIMEOUT = Duration.ofSeconds(60);

	public TomcatContainer(String deployment, String context) {
		this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG), deployment, context);
	}

	public TomcatContainer(String version, String deployment, String context) {
		this(DEFAULT_IMAGE_NAME.withTag(version), deployment, context);
	}

	public TomcatContainer(DockerImageName dockerImageName, String deployment, String context) {
		this(dockerImageName, deployment, context, DEFAULT_STARTUP_TIMEOUT);
	}

	public TomcatContainer(DockerImageName dockerImageName, String deployment, String context, Duration startUpTimeOut) {
		super(dockerImageName);
		dockerImageName.assertCompatibleWith(DEFAULT_IMAGE_NAME);
		withExposedPorts(DEFAULT_PORT);
		withReuse(true);
		MountableFile file = MountableFile.forHostPath(Paths.get(deployment).toAbsolutePath(), 0777);
		withCopyFileToContainer(file, "/usr/local/tomcat/webapps/" + context + ".war");
		waitingFor(Wait.forListeningPort().withStartupTimeout(startUpTimeOut));
	}
}
