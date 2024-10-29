package org.ylzl.eden.dubbo.spring.cloud.local;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.*;
import org.ylzl.eden.commons.net.IpConfigUtils;

/**
 * 本地开发过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class LocalFilter implements Filter {

	public static final String SOURCE_IP = "sourceIp";

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		String sourceIp = invocation.getAttachment(SOURCE_IP);
		if (sourceIp == null) {
			log.debug("Use localhost address due to missing source address.");
			sourceIp = IpConfigUtils.getIpAddress();
		}
		invocation.setAttachment(SOURCE_IP, sourceIp);
		return invoker.invoke(invocation);
	}
}
