package org.ylzl.eden.full.link.stress.testing.filter;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class StressTagServletFilter implements Filter {

	private final Tracer tracer;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		/*StressContext stressContext = new StressContext();
		String tag = exchange.getRequest().getHeaders().getFirst(StressTag.STRESS_TAG);
		BaggageField dunshan = BaggageField.getByName("dunshan");
		if (StringUtils.isNotBlank(tag)) {
			tracer.currentSpan().tag(StressTag.STRESS_TAG, tag);
			ServerHttpRequest request = exchange.getRequest().mutate().header(StressTag.STRESS_TAG, tag).build();
			exchange = exchange.mutate().request(request).build();

			stressContext.setStress(Boolean.parseBoolean(tag));
		}
		StressContext.setContext(stressContext);
		chain.doFilter(request, response);*/
	}

	@Override
	public void destroy() {
		Filter.super.destroy();
	}
}
