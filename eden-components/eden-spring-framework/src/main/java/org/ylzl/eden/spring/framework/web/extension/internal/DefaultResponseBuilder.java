package org.ylzl.eden.spring.framework.web.extension.internal;

import org.jetbrains.annotations.PropertyKey;
import org.ylzl.eden.commons.lang.MessageFormatUtils;
import org.ylzl.eden.spring.framework.error.ErrorCodeLoader;
import org.ylzl.eden.spring.framework.web.extension.ResponseBuilder;
import org.ylzl.eden.spring.framework.web.model.Result;

/**
 * 默认响应体构建
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class DefaultResponseBuilder implements ResponseBuilder<Result<?>> {

	@Override
	public Result<?> buildSuccess() {
		Result<?> result = new Result<>();
		result.setSuccess(true);
		return result;
	}

	@Override
	public <Body> Result<?> buildSuccess(Body data) {
		Result<Body> result = new Result<>();
		result.setSuccess(true);
		result.setData(data);
		return result;
	}

	@Override
	public Result<?> buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
								 Object... params) {
		Result<?> result = new Result<>();
		result.setSuccess(false);
		result.setCode(errCode);
		result.setMessage(ErrorCodeLoader.getErrMessage(errCode, params));
		return result;
	}

	@Override
	public Result<?> buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
								 String errMessage, Object... params) {
		Result<?> result = new Result<>();
		result.setSuccess(false);
		result.setCode(errCode);
		result.setMessage(MessageFormatUtils.format(errMessage, params));
		return result;
	}
}
