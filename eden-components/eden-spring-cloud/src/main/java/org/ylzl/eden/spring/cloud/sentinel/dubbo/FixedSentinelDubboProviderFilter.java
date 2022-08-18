package org.ylzl.eden.spring.cloud.sentinel.dubbo;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.ResourceTypeConstants;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.adapter.dubbo3.DubboUtils;
import com.alibaba.csp.sentinel.adapter.dubbo3.config.DubboAdapterGlobalConfig;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.*;

/**
 * 修复 SentinelDubboProviderFilter
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 * @see com.alibaba.csp.sentinel.adapter.dubbo3.SentinelDubboProviderFilter
 */
@Activate(group = CommonConstants.PROVIDER, value = "sentinel-dubbo-provider")
public class FixedSentinelDubboProviderFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(FixedSentinelDubboProviderFilter.class);

	public FixedSentinelDubboProviderFilter() {
		logger.info("Sentinel Apache Dubbo3 provider filter initialized");
	}

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		// Get origin caller.
		String origin = DubboAdapterGlobalConfig.getOriginParser().parse(invoker, invocation);
		if (null == origin) {
			origin = "";
		}
		Entry interfaceEntry = null;
		Entry methodEntry = null;
		String prefix = DubboAdapterGlobalConfig.getDubboProviderResNamePrefixKey();
		String interfaceResourceName = getInterfaceName(invoker, prefix);
		String methodResourceName = getMethodName(invoker, invocation, prefix);
		try {
			// Only need to create entrance context at provider side, as context will take effect
			// at entrance of invocation chain only (for inbound traffic).
			ContextUtil.enter(methodResourceName, origin);
			interfaceEntry = SphU.entry(interfaceResourceName, ResourceTypeConstants.COMMON_RPC, EntryType.IN);
			methodEntry = SphU.entry(methodResourceName, ResourceTypeConstants.COMMON_RPC, EntryType.IN,
				invocation.getArguments());
			return invoker.invoke(invocation);
//			if (result.hasException()) {
//				Tracer.traceEntry(result.getException(), interfaceEntry);
//				Tracer.traceEntry(result.getException(), methodEntry);
//			}
//			return result;
		} catch (BlockException e) {
			return DubboAdapterGlobalConfig.getProviderFallback().handle(invoker, invocation, e);
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
			ContextUtil.exit();
		}
	}

	private String getMethodName(Invoker invoker, Invocation invocation, String prefix) {
		return DubboUtils.getMethodResourceName(invoker, invocation, prefix);
	}

	private String getInterfaceName(Invoker invoker, String prefix) {
		return DubboUtils.getInterfaceName(invoker, prefix);
	}
}
