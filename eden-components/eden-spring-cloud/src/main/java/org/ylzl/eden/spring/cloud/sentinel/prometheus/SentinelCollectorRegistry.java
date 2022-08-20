package org.ylzl.eden.spring.cloud.sentinel.prometheus;

import io.prometheus.client.*;
import lombok.Getter;

/**
 * Sentinel 监控采集注册
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public class SentinelCollectorRegistry {

	private Counter passRequests;

	private Counter blockRequests;

	private Counter successRequests;

	private Counter exceptionRequests;

	private Histogram rtHist;

	private Gauge currentThreads;

	public SentinelCollectorRegistry(CollectorRegistry registry) {
		passRequests = Counter.build()
			.name("sentinel_pass_requests_total")
			.help("total pass requests.")
			.labelNames("resource")
			.register(registry);
		blockRequests = Counter.build()
			.name("sentinel_block_requests_total")
			.help("total block requests.")
			.labelNames("resource", "type", "ruleLimitApp", "limitApp")
			.register(registry);
		successRequests = Counter.build()
			.name("sentinel_success_requests_total")
			.help("total success requests.")
			.labelNames("resource")
			.register(registry);
		exceptionRequests = Counter.build()
			.name("sentinel_exception_requests_total")
			.help("total exception requests.")
			.labelNames("resource")
			.register(registry);
		currentThreads = Gauge.build()
			.name("sentinel_current_threads")
			.help("current thread count.")
			.labelNames("resource")
			.register(registry);
		rtHist = Histogram.build()
			.name("sentinel_requests_latency_seconds")
			.help("request latency in seconds.")
			.labelNames("resource")
			.register(registry);
	}
}
