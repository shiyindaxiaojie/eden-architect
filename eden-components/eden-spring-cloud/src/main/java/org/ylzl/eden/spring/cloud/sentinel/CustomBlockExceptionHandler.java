package org.ylzl.eden.spring.cloud.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.http.MediaType;
import org.ylzl.eden.commons.json.JacksonUtils;
import org.ylzl.eden.spring.framework.cola.dto.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义 Sentinel 异常
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
public class CustomBlockExceptionHandler implements BlockExceptionHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
		Response body = ResponseBuilder.buildResponse(e);
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.getWriter().write(JacksonUtils.toJSONString(body));
	}
}
