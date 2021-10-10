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

package org.ylzl.eden.spring.boot.support.dao.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页器查询对象
 *
 * @author gyl
 * @since 1.0.0
 */
@ApiModel(description = "分页器查询对象")
@Data
public class PageableQuery implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "当前页码", required = true, example = "1")
	@Builder.Default
	private Long pageNumber = 1L;

  @ApiModelProperty(value = "每页记录数", required = true, example = "10")
	@Builder.Default
	private Long pageSize = 10L;

  public Long getOffset() {
    return (this.pageNumber - 1) * pageSize;
  }
}
