package org.ylzl.eden.spring.cloud.sentinel.dubbo;

import com.alibaba.csp.sentinel.*;
import com.alibaba.csp.sentinel.adapter.dubbo3.DubboUtils;
import com.alibaba.csp.sentinel.adapter.dubbo3.config.DubboAdapterGlobalConfig;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.filter.ClusterFilter;
import org.apache.dubbo.rpc.support.RpcUtils;

import java.util.LinkedList;
import java.util.Optional;

/**
 * 修复 SentinelDubboCustomerFilter
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 * @see com.alibaba.csp.sentinel.adapter.dubbo3.SentinelDubboConsumerFilter
 */
@Slf4j
@Activate(group = CommonConstants.CONSUMER, value = "sentinel-dubbo-consumer")
public class FixedSentinelDubboConsumerFilter implements ClusterFilter {

	public FixedSentinelDubboConsumerFilter() {
		log.info("Sentinel Apache Dubbo3 consumer filter initialized");
	}

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		InvokeMode invokeMode = RpcUtils.getInvokeMode(invoker.getUrl(), invocation);
		if (InvokeMode.SYNC == invokeMode) {
			return syncInvoke(invoker, invocation);
		} else {
			return asyncInvoke(invoker, invocation);
		}
	}

	private String getMethodName(Invoker invoker, Invocation invocation, String prefix) {
		return DubboUtils.getMethodResourceName(invoker, invocation, prefix);
	}

	private String getInterfaceName(Invoker invoker, String prefix) {
		return DubboUtils.getInterfaceName(invoker, prefix);
	}

	private Result syncInvoke(Invoker<?> invoker, Invocation invocation) {
		Entry interfaceEntry = null;
		Entry methodEntry = null;
		String prefix = DubboAdapterGlobalConfig.getDubboConsumerResNamePrefixKey();
		String interfaceResourceName = getInterfaceName(invoker, prefix);
		String methodResourceName = getMethodName(invoker, invocation, prefix);
		try {
			interfaceEntry = SphU.entry(interfaceResourceName, ResourceTypeConstants.COMMON_RPC, EntryType.OUT);
			methodEntry = SphU.entry(methodResourceName, ResourceTypeConstants.COMMON_RPC, EntryType.OUT,
				invocation.getArguments());
			return invoker.invoke(invocation);
//			if (result.hasException()) {
//				Tracer.traceEntry(result.getException(), interfaceEntry);
//				Tracer.traceEntry(result.getException(), methodEntry);
//			}
//			return result;
		} catch (BlockException e) {
			return DubboAdapterGlobalConfig.getConsumerFallback().handle(invoker, invocation, e);
		} catch (RpcException e) {
//			Tracer.traceEntry(e, interfaceEntry);
//			Tracer.traceEntry(e, methodEntry);
			throw e;
		} finally {
			if (methodEntry != null) {
				methodEntry.exit(1, invocation.getArguments());
			}
			if (interfaceEntry != null) {
				interfaceEntry.exit();
			}
		}
	}

	private Result asyncInvoke(Invoker<?> invoker, Invocation invocation) {
		LinkedList<EntryHolder> queue = new LinkedList<>();
		String prefix = DubboAdapterGlobalConfig.getDubboConsumerResNamePrefixKey();
		String interfaceResourceName = getInterfaceName(invoker, prefix);
		String methodResourceName = getMethodName(invoker, invocation, prefix);
		try {
			queue.push(new EntryHolder(
				SphU.asyncEntry(interfaceResourceName, ResourceTypeConstants.COMMON_RPC, EntryType.OUT), null));
			queue.push(new EntryHolder(
				SphU.asyncEntry(methodResourceName, ResourceTypeConstants.COMMON_RPC,
					EntryType.OUT, 1, invocation.getArguments()), invocation.getArguments()));
			Result result = invoker.invoke(invocation);
			result.whenCompleteWithContext((r, throwable) -> {
				Throwable error = throwable;
				if (error == null) {
					error = Optional.ofNullable(r).map(Result::getException).orElse(null);
				}
				while (!queue.isEmpty()) {
					EntryHolder holder = queue.pop();
					Tracer.traceEntry(error, holder.entry);
					exitEntry(holder);
				}
			});
			return result;
		} catch (BlockException e) {
			while (!queue.isEmpty()) {
				exitEntry(queue.pop());
			}
			return DubboAdapterGlobalConfig.getConsumerFallback().handle(invoker, invocation, e);
		}
	}

	static class EntryHolder {

		final private Entry entry;
		final private Object[] params;

		public EntryHolder(Entry entry, Object[] params) {
			this.entry = entry;
			this.params = params;
		}
	}

	private void exitEntry(EntryHolder holder) {
		if (holder.params != null) {
			holder.entry.exit(1, holder.params);
		} else {
			holder.entry.exit();
		}
	}
}
