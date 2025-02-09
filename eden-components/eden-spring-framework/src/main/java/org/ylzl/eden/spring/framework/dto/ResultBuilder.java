/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.framework.dto;

import org.jetbrains.annotations.PropertyKey;
import org.ylzl.eden.commons.lang.MessageFormatUtils;
import org.ylzl.eden.spring.framework.error.ErrorCodeLoader;
import org.ylzl.eden.spring.framework.dto.extension.ResponseBuilder;

/**
 * 默认响应体构建
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ResultBuilder implements ResponseBuilder<Result> {

	@Override
	public Result buildSuccess() {
		Result result = new Result();
		result.setSuccess(true);
		return result;
	}

	@Override
	public <T> Result buildSuccess(T data) {
		if (data == null) {
			return new Result();
		}
		return SingleResult.build(data);
	}

	@Override
	public Result buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
							   Object... params) {
		Result result = new Result();
		result.setSuccess(false);
		result.setCode(errCode);
		result.setMessage(ErrorCodeLoader.getErrMessage(errCode, params));
		return result;
	}

	@Override
	public Result buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
							   String errMessage, Object... params) {
		Result result = new Result();
		result.setSuccess(false);
		result.setCode(errCode);
		result.setMessage(MessageFormatUtils.format(errMessage, params));
		return result;
	}
}
