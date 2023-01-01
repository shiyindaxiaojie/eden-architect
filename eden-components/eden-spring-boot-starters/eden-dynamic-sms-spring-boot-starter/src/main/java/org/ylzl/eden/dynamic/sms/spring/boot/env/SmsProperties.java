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

package org.ylzl.eden.dynamic.sms.spring.boot.env;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 短信配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = SmsProperties.PREFIX)
public class SmsProperties {

	public static final String PREFIX = "spring.sms.dynamic";

	private boolean enabled;

	private String primary;

	private final Aliyun aliyun = new Aliyun();

	private final Qcloud qcloud = new Qcloud();

	private final Emay emay = new Emay();

	private final Montnets montnets = new Montnets();

	@Setter
	@Getter
	public static class Aliyun {

		public static final String PREFIX = SmsProperties.PREFIX + ".aliyun";

		private boolean enabled;
	}

	@Setter
	@Getter
	public static class Qcloud {

		public static final String PREFIX = SmsProperties.PREFIX + ".qcloud";

		private boolean enabled;
	}

	@Setter
	@Getter
	public static class Emay {

		public static final String PREFIX = SmsProperties.PREFIX + ".emay";

		private boolean enabled;
	}

	@Setter
	@Getter
	public static class Montnets {

		public static final String PREFIX = SmsProperties.PREFIX + ".montnets";

		private boolean enabled;
	}
}
