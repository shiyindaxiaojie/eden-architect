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

package org.ylzl.eden.cola.catchlog.handler;

import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.cola.dto.Response;
import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 异常处理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class CatchLogHandler {

	/**
	 * 封装响应报文
	 *
	 * @param returnType 返回类型
	 * @param e          异常
	 * @return 返回值
	 */
	public static Object wrap(Class<?> returnType, BaseException e) {
		try {
			Response response = (Response) returnType.newInstance();
			response.setSuccess(false);
			response.setErrCode(e.getErrCode());
			response.setErrMessage(e.getErrMessage());
			return response;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return null;
		}
	}
}
