package org.ylzl.eden.spring.cloud.sentinel.dubbo;

import com.alibaba.csp.sentinel.adapter.dubbo3.fallback.DubboFallback;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.ylzl.eden.spring.framework.error.ThirdServiceException;

/**
 * Dubbo 消费端降级
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
public class SentinelDubboConsumerFallback implements DubboFallback {


	@Override
	public Result handle(Invoker<?> invoker, Invocation invocation, BlockException ex) {
		return AsyncRpcResult.newDefaultAsyncResult(
			new ThirdServiceException("C0401", ex.toRuntimeException()),
			invocation);
	}
}
