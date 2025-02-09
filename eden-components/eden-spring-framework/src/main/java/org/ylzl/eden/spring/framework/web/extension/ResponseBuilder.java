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

package org.ylzl.eden.spring.framework.web.extension;

import org.jetbrains.annotations.PropertyKey;
import org.ylzl.eden.extension.ExtensionLoader;
import org.ylzl.eden.extension.SPI;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.ErrorCodeLoader;

/**
 * 响应结果构建
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@SPI("response")
public interface ResponseBuilder<T> {

	static ResponseBuilder<?> builder() {
		ResponseBuilder<?> builder = ApplicationContextHelper.getBean(ResponseBuilder.class);
		if (builder != null) {
			return builder;
		}
		return ExtensionLoader.getExtensionLoader(ResponseBuilder.class).getDefaultExtension();
	}

	T buildSuccess();

	<Body> T buildSuccess(Body data);

	T buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
				   Object... params);

	T buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
				   String errMessage, Object... params);
}
