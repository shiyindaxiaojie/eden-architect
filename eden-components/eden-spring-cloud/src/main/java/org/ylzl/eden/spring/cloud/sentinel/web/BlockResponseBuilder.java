package org.ylzl.eden.spring.cloud.sentinel.web;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.spring.framework.web.extension.ResponseBuilder;

/**
 * Sentinel 熔断响应构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class BlockResponseBuilder {

	public static Object buildResponse(Throwable e) {
		ResponseBuilder<?> builder = ResponseBuilder.builder();
		if (e instanceof FlowException) {
			return builder.buildFailure("REQ-FLOW-429", e.getMessage());
		}
		if (e instanceof DegradeException) {
			return builder.buildFailure("REQ-DEGRADE-429", e.getMessage());
		}
		if (e instanceof ParamFlowException) {
			return builder.buildFailure("REQ-PARAM-429", e.getMessage());
		}
		if (e instanceof SystemBlockException) {
			return builder.buildFailure("REQ-BLOCK-429", e.getMessage());
		}
		if (e instanceof AuthorityException) {
			return builder.buildFailure("REQ-AUTH-429", e.getMessage());
		}
		return builder.buildFailure("REQ-LIMIT-429" , e.getMessage());
	}
}
