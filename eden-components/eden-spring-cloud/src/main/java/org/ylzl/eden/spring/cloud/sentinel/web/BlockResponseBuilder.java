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

package org.ylzl.eden.spring.cloud.sentinel.web;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.spring.framework.web.extension.ResponseBuilder;

/**
 * Sentinel 熔断响应构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class BlockResponseBuilder {

	public static Object buildResponse(Throwable e) {
		ResponseBuilder<?> builder = ResponseBuilder.builder();
		if (e instanceof FlowException) {
			return builder.buildFailure("REQ-FLOW-429", e.getMessage());
		}
		if (e instanceof DegradeException) {
			return builder.buildFailure("REQ-DEGRADE-429", e.getMessage());
		}
		if (e instanceof ParamFlowException) {
			return builder.buildFailure("REQ-PARAM-429", e.getMessage());
		}
		if (e instanceof SystemBlockException) {
			return builder.buildFailure("REQ-BLOCK-429", e.getMessage());
		}
		if (e instanceof AuthorityException) {
			return builder.buildFailure("REQ-AUTH-429", e.getMessage());
		}
		return builder.buildFailure("REQ-LIMIT-429", e.getMessage());
	}
}
