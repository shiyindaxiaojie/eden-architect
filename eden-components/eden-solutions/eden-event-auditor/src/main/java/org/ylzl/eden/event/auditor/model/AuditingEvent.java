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

package org.ylzl.eden.event.auditor.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 审计事件
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class AuditingEvent {

	/** 操作对象 */
	private String operator;

	/** 操作角色 */
	private String role;

	/** 操作时间 */
	private LocalDateTime operateDate;

	/** 业务场景 */
	private String bizScenario;

	/** 记录内容 */
	private String content;

	/** 额外信息 */
	private String extra;

	/** 返回值 */
	private String returnValue;

	/** 执行耗时 */
	private Long executionCost;

	/** 执行是否成功 */
	private Boolean success;

	/** 异常信息 */
	private String throwable;
}
