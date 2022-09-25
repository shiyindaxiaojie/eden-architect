package org.ylzl.eden.full.link.stress.testing.gateway.filter;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.full.link.stress.testing.StressTag;
import reactor.core.publisher.Mono;

/**
 * 压测标记网关过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class GatewayStressTagFilter implements GlobalFilter {

	private final Tracer tracer;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String tag = exchange.getRequest().getHeaders().getFirst(StressTag.STRESS_TAG);
		if (StringUtils.isNotBlank(tag)) {
			tracer.currentSpan().tag(StressTag.STRESS_TAG, tag);
			ServerHttpRequest request = exchange.getRequest().mutate().header(StressTag.STRESS_TAG, tag).build();
			exchange = exchange.mutate().request(request).build();
		}
		return chain.filter(exchange);
	}
}
