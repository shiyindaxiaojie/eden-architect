package org.ylzl.eden.spring.cloud.sentinel.web;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import org.ylzl.eden.spring.framework.cola.dto.Response;

/**
 * 响应构造器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ResponseBuilder {

	public static Response buildResponse(Throwable e) {
		if (e instanceof FlowException) {
			return Response.buildFailure("B0210", "流控限制");
		}
		if (e instanceof DegradeException) {
			return Response.buildFailure("B0220", "系统降级");
		}
		if (e instanceof ParamFlowException) {
			return Response.buildFailure("B0210", "参数流控限制");
		}
		if (e instanceof SystemBlockException) {
			return Response.buildFailure("B0200", "系统保护");
		}
		if (e instanceof AuthorityException) {
			return Response.buildFailure("B0200", "授权限制");
		}
		return Response.buildFailure("B0200" , e.getMessage());
	}
}
