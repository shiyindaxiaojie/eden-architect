package org.ylzl.eden.spring.framework.logging.model;

import lombok.*;

import java.time.Instant;

/**
 * 访问日志模型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class AccessLog {

	private String location;

	private String arguments;

	private String returnValue;

	private Throwable throwable;

	private long duration;

	private transient Instant start;

	private transient Instant end;
}
