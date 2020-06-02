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

package org.ylzl.eden.spring.boot.data.jdbc.datasource.routing;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.spring.boot.data.core.DataConstants;

/**
 * 动态数据源默认属性
 *
 * @author gyl
 * @since 2.0.0
 */
@UtilityClass
public class RoutingDataSourceDefault {

	public static final String PROP_SPRING_DATA_PROPS_DS_PREFIX =
		DataConstants.PROP_PREFIX + ".routing-datasource";

	public static final Object TYPE = "com.zaxxer.hikari.HikariDataSource";

	public static class Properties {

		public static final String NODES = "nodes";

		public static final String TYPE = "type";

		public static final String DRIVE_CLASS_NAME = "driver-class-name";

		public static final String URL = "url";

		public static final String USERNAME = "username";

		public static final String PASSWORD = "password";

		public static final String JNDI_NAME = "jndi-name";
	}
}
