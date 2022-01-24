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

package org.ylzl.eden.spring.framework.web.rest.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 数据列表视图模型
 *
 * @author gyl
 * @since 2.4.x
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@ApiModel(description = "数据列表视图模型")
public class DatasVM<T> implements Serializable {

	private static final long serialVersionUID = -6062447811540513140L;

	/**
	 * 消息
	 */
	@ApiModelProperty(value = "消息")
	private String message;

	/**
	 * 数据
	 */
	@ApiModelProperty(value = "数据")
	private T data;

	/**
	 * 统计
	 */
	@ApiModelProperty(value = "统计")
	private Long count;
}
