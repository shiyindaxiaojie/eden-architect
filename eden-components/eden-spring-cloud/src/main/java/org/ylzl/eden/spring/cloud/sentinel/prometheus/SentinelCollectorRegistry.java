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
