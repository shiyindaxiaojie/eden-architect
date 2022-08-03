package org.ylzl.eden.spring.boot.cat.integration.dubbo;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, order = -9000)
public class DubboTraceFilter implements Filter {

	private final static String DUBBO_BIZ_ERROR="DUBBO_BIZ_ERROR";

	private final static String DUBBO_TIMEOUT_ERROR="DUBBO_TIMEOUT_ERROR";

	private final static String DUBBO_REMOTING_ERROR="DUBBO_REMOTING_ERROR";

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		return null;
	}
}
