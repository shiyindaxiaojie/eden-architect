package org.ylzl.eden.spring.boot.cat.integration.http;

import com.dianping.cat.support.servlet.CatFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class HttpTraceFilter extends CatFilter {

	public static final String TRACE_ID = "traceId";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		super.doFilter(request, response, chain);
//		MDC.put(CatConstants.TRACE_ID, Context.getRootId());
	}
}
