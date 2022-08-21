package org.ylzl.eden.spring.cloud.cat.integration.dubbo.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * Dubbo 消费者集成 CAT 过滤
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Activate(group = {CommonConstants.CONSUMER}, value = "cat")
public class CatDubboConsumerFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		RpcContext.getContext().setAttachment(CommonConstants.APPLICATION_KEY,
			invoker.getUrl().getParameter(CommonConstants.APPLICATION_KEY));
		return invoker.invoke(invocation);
	}
}
