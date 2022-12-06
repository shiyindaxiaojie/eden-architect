package org.ylzl.eden.dynamic.sms.spring.boot.env;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.dynamic.sms.spring.boot.support.SmsBeanNames;

/**
 * 短信配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = SmsProperties.PREFIX)
public class SmsProperties {

	public static final String PREFIX = "dynamic-sms";

	private Boolean enabled;

	private SmsBeanNames primary;

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
