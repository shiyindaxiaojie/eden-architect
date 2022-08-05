package org.ylzl.eden.spring.boot.cat.integration.dubbo;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@Activate(group = {CommonConstants.CONSUMER})
public class ApplicationFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		RpcContext.getContext().setAttachment(CommonConstants.APPLICATION_KEY,
			invoker.getUrl().getParameter(CommonConstants.APPLICATION_KEY));
		return invoker.invoke(invocation);
	}
}
