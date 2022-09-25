package org.ylzl.eden.full.link.stress.testing.web.filter;

import brave.Tracer;
import brave.baggage.BaggageField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.full.link.stress.testing.StressContext;
import org.ylzl.eden.full.link.stress.testing.StressTag;

import javax.servlet.*;
import java.io.IOException;

/**
 * 压测标记过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class WebStressTagFilter implements Filter {

	private final Tracer tracer;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		StressContext stressContext = new StressContext();
		String tag = BaggageField.getByName(StressTag.STRESS_TAG).getValue();
		if (StringUtils.isNotBlank(tag)) {
			tracer.currentSpan().tag(StressTag.STRESS_TAG, tag);
			stressContext.setStress(Boolean.parseBoolean(tag));
		}
		StressContext.setContext(stressContext);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		StressContext.removeContext();
		Filter.super.destroy();
	}
}
