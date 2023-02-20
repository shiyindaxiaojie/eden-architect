package org.ylzl.eden.spring.integration.cat.integration.feign;

import com.dianping.cat.Cat;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.ylzl.eden.spring.integration.cat.extension.CatConstants;
import org.ylzl.eden.spring.integration.cat.extension.CatContext;

/**
 * Feign 注入 CAT 拦截
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CatFeignRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		CatContext catContext = new CatContext();
		Cat.logRemoteCallClient(catContext, Cat.getManager().getDomain());
		requestTemplate.header(Cat.Context.ROOT, catContext.getProperty(CatConstants.HTTP_HEADER_ROOT_MESSAGE_ID));
		requestTemplate.header(Cat.Context.PARENT,catContext.getProperty(CatConstants.HTTP_HEADER_PARENT_MESSAGE_ID));
		requestTemplate.header(Cat.Context.CHILD, catContext.getProperty(CatConstants.HTTP_HEADER_CHILD_MESSAGE_ID));
	}
}
