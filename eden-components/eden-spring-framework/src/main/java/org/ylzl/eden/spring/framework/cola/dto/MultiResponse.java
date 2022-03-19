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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.PropertyKey;
import org.slf4j.helpers.MessageFormatter;
import org.ylzl.eden.spring.framework.error.ErrorConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 响应（多条记录）
 *
 * @author Frank Zhang
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MultiResponse<T> extends Response {

	private static final long serialVersionUID = 1L;

	private Collection<T> data;

	public List<T> getData() {
		return null == data ? Collections.emptyList() : new ArrayList<>(data);
	}

	public void setData(Collection<T> data) {
		this.data = data;
	}

	public boolean isEmpty() {
		return data == null || data.isEmpty();
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}

	public static MultiResponse buildSuccess() {
		MultiResponse response = new MultiResponse();
		response.setSuccess(true);
		return response;
	}

	public static MultiResponse buildFailure(@PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode,
											Object... params) {
		MultiResponse response = new MultiResponse();
		response.setSuccess(false);
		response.setErrCode(errCode);
		response.setErrMessage(ErrorConfig.getErrMessage(errCode, params));
		return response;
	}

	public static MultiResponse buildFailure(@PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode,
											String errMessage, Object... params) {
		MultiResponse response = new MultiResponse();
		response.setSuccess(false);
		response.setErrCode(errCode);
		response.setErrMessage(MessageFormatter.arrayFormat(errMessage, params).getMessage());
		return response;
	}

	public static <T> MultiResponse<T> of(Collection<T> data) {
		MultiResponse<T> response = new MultiResponse<>();
		response.setSuccess(true);
		response.setData(data);
		return response;
	}
}
