package org.ylzl.eden.full.link.stress.testing.filter;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.ylzl.eden.commons.lang.StringUtils;
import reactor.core.publisher.Mono;

/**
 * 压测标记过滤器
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class StressTagFilter implements GlobalFilter {

	private final Tracer tracer;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String tag = exchange.getRequest().getHeaders().getFirst(StressTag.STRESS_TAG);
		if (StringUtils.isNotBlank(tag)) {
			// 目的：通过`BaggageField.getByName("");`获取压测标记
			tracer.currentSpan().tag(StressTag.STRESS_TAG, tag);
			// 目的：通过`HttpServletRequest`获取压测标记
			ServerHttpRequest request = exchange.getRequest().mutate().header(StressTag.STRESS_TAG, tag).build();
			exchange = exchange.mutate().request(request).build();
		}
		return chain.filter(exchange);
	}
}
