package org.ylzl.eden.spring.data.influxdb.core;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class InfluxDBConfig {

	private String url;

	private String username;

	private String password;

	private String database;

	private String retentionPolicy = "autogen";

	private int connectTimeout = 10;

	private int readTimeout = 30;

	private int writeTimeout = 10;

	private boolean gzip = false;

	private boolean enableBatch = false;

	private int batchAction = 2000;

	private int flushDuration = 1000;

	private int jitterDuration = 500;

	private int udpPort = 8089;
}
