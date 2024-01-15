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

package org.ylzl.eden.xxljob.spring.boot.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * XxlJob 配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @link <a href="https://github.com/xuxueli/xxl-job/blob/master/xxl-job-executor-samples/xxl-job-executor-sample-springboot/src/main/resources/application.properties">...</a>
 * @since 2.4.13
 **/
@Data
@ConfigurationProperties(prefix = XxlJobProperties.PREFIX)
public class XxlJobProperties {

	public static final String PREFIX = "xxl.job";

	public static final String ENABLED = PREFIX + ".enabled";

	private final Admin admin = new Admin();

	private final Executor executor = new Executor();

	private boolean enabled;

	private String accessToken;

	@Data
	public static class Admin {

		private String addresses;
	}

	@Data
	public static class Executor {

		/**
		 * 执行器名称，必填项
		 */
		private String title;

		/**
		 * 应用标识，配置为空时自动获取 Spring 应用名称
		 */
		private String appName;

		/**
		 * 显示设置执行器的地址
		 */
		private String address;

		/**
		 * 配置为空时表示自动获取 IP
		 */
		private String ip;

		/**
		 * 小于 0 表示自动获取端口
		 */
		private int port = -1;

		/**
		 * 默认遵循官方的存储路径
		 */
		private String logPath = "/data/applogs/xxl-job/jobhandler";

		/**
		 * 日志默认保留 30 天
		 */
		private int logRetentionDays = 30;
	}
}
