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

package org.ylzl.eden.dynamic.mail.spring.boot.env;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.dynamic.mail.spring.boot.support.MailBeanNames;

/**
 * 邮件配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = MailProperties.PREFIX)
public class MailProperties {

	public static final String PREFIX = "spring.mail.dynamic";

	private Boolean enabled;

	private MailBeanNames primary;

	private final JavaMail javaMail = new JavaMail();

	@Setter
	@Getter
	public static class JavaMail {

		public static final String PREFIX = MailProperties.PREFIX + ".javamail";

		private boolean enabled;
	}
}
