package org.ylzl.eden.spring.integration.cat.integration.feign;

import com.dianping.cat.Cat;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.ylzl.eden.spring.integration.cat.CatConstants;
import org.ylzl.eden.spring.integration.cat.tracing.TraceContext;

/**
 * Feign 注入 CAT 拦截
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class FeignCatRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		Cat.Context context = TraceContext.getContext();
		Cat.logRemoteCallClient(context, Cat.getManager().getDomain());
		requestTemplate.header(Cat.Context.ROOT, context.getProperty(CatConstants.X_CAT_ROOT_ID));
		requestTemplate.header(Cat.Context.PARENT,context.getProperty(CatConstants.X_CAT_PARENT_ID));
		requestTemplate.header(Cat.Context.CHILD, context.getProperty(CatConstants.X_CAT_ID));
	}
}
