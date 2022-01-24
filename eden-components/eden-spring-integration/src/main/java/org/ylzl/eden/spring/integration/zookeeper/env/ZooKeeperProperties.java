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

package org.ylzl.eden.spring.integration.zookeeper.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.spring.framework.core.constant.SpringFrameworkConstants;

/**
 * ZooKeeper 配置属性
 *
 * @author gyl
 * @since 2.4.x
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SpringFrameworkConstants.PROP_SPRING_PREFIX + ".zookeeper")
public class ZooKeeperProperties {

	private Boolean enabled = ZooKeeperDefaults.enabled;

	private String connectString = ZooKeeperDefaults.connectString;

	private Integer sessionTimeout = ZooKeeperDefaults.sessionTimeout;

	public static class Curator {
	}
}
