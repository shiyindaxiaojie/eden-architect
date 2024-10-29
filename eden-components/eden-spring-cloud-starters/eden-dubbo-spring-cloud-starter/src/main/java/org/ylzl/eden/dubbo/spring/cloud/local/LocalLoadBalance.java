package org.ylzl.eden.dubbo.spring.cloud.local;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.loadbalance.RandomLoadBalance;
import org.ylzl.eden.commons.net.IpConfigUtils;

import java.util.List;

import static org.ylzl.eden.dubbo.spring.cloud.local.LocalFilter.SOURCE_IP;

/**
 * 本地开发负载均衡
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class LocalLoadBalance extends RandomLoadBalance {

	@Override
	public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
		String sourceIp = invocation.getAttachment(SOURCE_IP);
		if (sourceIp == null) {
			sourceIp = IpConfigUtils.getIpAddress();
		}
		for (Invoker<T> invoker: invokers) {
			if (invoker.getUrl().getIp().equals(sourceIp)) {
				return invoker;
			}
		}
		return super.select(invokers, url, invocation);
	}
}
