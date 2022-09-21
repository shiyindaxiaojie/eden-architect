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
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class StressTagGatewayFilter implements GlobalFilter {

	private final Tracer tracer;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		StressContext stressContext = new StressContext();
		String tag = exchange.getRequest().getHeaders().getFirst(StressTag.STRESS_TAG);
		if (StringUtils.isNotBlank(tag)) {
			tracer.currentSpan().tag(StressTag.STRESS_TAG, tag);
			ServerHttpRequest request = exchange.getRequest().mutate().header(StressTag.STRESS_TAG, tag).build();
			exchange = exchange.mutate().request(request).build();

			stressContext.setStress(Boolean.parseBoolean(tag));
		}
		StressContext.setContext(stressContext);
		return chain.filter(exchange);
	}
}
