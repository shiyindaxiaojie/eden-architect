package org.ylzl.eden.spring.cloud.sentinel.dubbo;

import com.alibaba.csp.sentinel.adapter.dubbo3.fallback.DubboFallback;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.ylzl.eden.spring.framework.error.ServerException;

/**
 * Dubbo 服务端降级
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SentinelDubboProviderFallback implements DubboFallback {


	@Override
	public Result handle(Invoker<?> invoker, Invocation invocation, BlockException ex) {
		return AsyncRpcResult.newDefaultAsyncResult(
			new ServerException("C0401", ex.toRuntimeException()),
			invocation);
	}
}
