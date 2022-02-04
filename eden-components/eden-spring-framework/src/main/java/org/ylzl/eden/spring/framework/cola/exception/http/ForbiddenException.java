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

package org.ylzl.eden.spring.framework.cola.exception.http;

import org.springframework.http.HttpStatus;
import org.ylzl.eden.spring.framework.cola.exception.ClientErrorType;
import org.ylzl.eden.spring.framework.cola.exception.BaseException;

/**
 * HTTP 403 错误（非法访问）
 *
 * @author gyl
 * @since 2.4.x
 */
public class ForbiddenException extends BaseException {

	public ForbiddenException(String errMessage) {
		super(ClientErrorType.A0320.getErrCode(), errMessage, HttpStatus.FORBIDDEN.value());
	}
}
