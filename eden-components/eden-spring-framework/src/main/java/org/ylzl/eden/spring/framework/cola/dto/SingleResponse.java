/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.framework.cola.dto;

import lombok.*;
import org.jetbrains.annotations.PropertyKey;
import org.slf4j.helpers.MessageFormatter;
import org.ylzl.eden.spring.framework.error.ErrorConfig;

/**
 * 响应（单条记录）
 *
 * @author Frank Zhang
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SingleResponse<T> extends Response {

	private static final long serialVersionUID = 1L;

	private T data;

	public static SingleResponse buildSuccess() {
		SingleResponse response = new SingleResponse();
		response.setSuccess(true);
		return response;
	}

	public static SingleResponse buildFailure(@PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode,
										Object... params) {
		SingleResponse response = new SingleResponse();
		response.setSuccess(false);
		response.setErrCode(errCode);
		response.setErrMessage(ErrorConfig.getErrMessage(errCode, params));
		return response;
	}

	public static SingleResponse buildFailure(@PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode,
											  String errMessage, Object... params) {
		SingleResponse response = new SingleResponse();
		response.setSuccess(false);
		response.setErrCode(errCode);
		response.setErrMessage(MessageFormatter.arrayFormat(errMessage, params).getMessage());
		return response;
	}

	public static <T> SingleResponse<T> of(T data) {
		SingleResponse<T> response = new SingleResponse<>();
		response.setSuccess(true);
		response.setData(data);
		return response;
	}
}
