package org.ylzl.eden.spring.framework.web.extension;

import org.jetbrains.annotations.PropertyKey;
import org.ylzl.eden.commons.lang.MessageFormatUtils;
import org.ylzl.eden.spring.framework.error.ErrorCodeLoader;

/**
 * 默认响应体构建
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class InternalResponseBuilder implements ResponseBuilder<InternalResponse<?>> {

	@Override
	public InternalResponse<?> buildSuccess() {
		InternalResponse<?> internalResponse = new InternalResponse<>();
		internalResponse.setSuccess(true);
		return internalResponse;
	}

	@Override
	public <Body> InternalResponse<?> buildSuccess(Body data) {
		InternalResponse<Body> internalResponse = new InternalResponse<>();
		internalResponse.setSuccess(true);
		internalResponse.setData(data);
		return internalResponse;
	}

	@Override
	public InternalResponse<?> buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
											Object... params) {
		InternalResponse<?> internalResponse = new InternalResponse<>();
		internalResponse.setSuccess(false);
		internalResponse.setCode(errCode);
		internalResponse.setMessage(ErrorCodeLoader.getErrMessage(errCode, params));
		return internalResponse;
	}

	@Override
	public InternalResponse<?> buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
											String errMessage, Object... params) {
		InternalResponse<?> internalResponse = new InternalResponse<>();
		internalResponse.setSuccess(false);
		internalResponse.setCode(errCode);
		internalResponse.setMessage(MessageFormatUtils.format(errMessage, params));
		return internalResponse;
	}
}
