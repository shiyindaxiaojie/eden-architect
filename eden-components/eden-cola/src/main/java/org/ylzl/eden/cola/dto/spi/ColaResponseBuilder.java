package org.ylzl.eden.cola.dto.spi;

import org.jetbrains.annotations.PropertyKey;
import org.ylzl.eden.cola.dto.Response;
import org.ylzl.eden.cola.dto.SingleResponse;
import org.ylzl.eden.commons.lang.MessageFormatUtils;
import org.ylzl.eden.spring.framework.error.ErrorCodeLoader;

/**
 * COLA 响应体构建
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ColaResponseBuilder implements org.ylzl.eden.spring.framework.web.extension.ResponseBuilder<Response> {

	@Override
	public Response buildSuccess() {
		Response response = new Response();
		response.setSuccess(true);
		return response;
	}

	@Override
	public <Body> Response buildSuccess(Body data) {
		SingleResponse<Body> response = new SingleResponse<>();
		response.setSuccess(true);
		response.setData(data);
		return response;
	}

	@Override
	public Response buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
								 Object... params) {
		Response response = new Response();
		response.setSuccess(false);
		response.setErrCode(errCode);
		response.setErrMessage(ErrorCodeLoader.getErrMessage(errCode, params));
		return response;
	}

	@Override
	public Response buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
								 String errMessage, Object... params) {
		Response response = new Response();
		response.setSuccess(false);
		response.setErrCode(errCode);
		response.setErrMessage(MessageFormatUtils.format(errMessage, params));
		return response;
	}
}