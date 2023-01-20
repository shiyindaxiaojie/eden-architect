package org.ylzl.eden.event.auditor.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.event.Level;

/**
 * 事件审计配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@EqualsAndHashCode
@ToString
@Setter
@Getter
public class EventAuditorConfig {

	private final Sender sender = new Sender();

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class Sender {

		private String senderType = "logging";

		private boolean async = true;

		private final Logging logging = new Logging();

		private final Kafka kafka = new Kafka();

		private final RocketMQ rocketMQ = new RocketMQ();

		@EqualsAndHashCode
		@ToString
		@Setter
		@Getter
		public static class Logging {

			private Level level = Level.INFO;
		}

		@EqualsAndHashCode
		@ToString
		@Setter
		@Getter
		public static class Kafka {

		}

		@EqualsAndHashCode
		@ToString
		@Setter
		@Getter
		public static class RocketMQ {

		}
	}
}
