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

package org.ylzl.eden.spring.boot.framework.web.rest.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 错误视图模型
 *
 * @author gyl
 * @since 0.0.1
 */
@Builder(toBuilder = true)
@Data
@EqualsAndHashCode
@ToString
@ApiModel(description = "错误视图模型")
public class ErrorVM implements Serializable {

  private static final long serialVersionUID = 5744374451644419814L;

  @ApiModelProperty(value = "消息")
  private String message;

  @ApiModelProperty(value = "描述")
  private String description;

  private List<FieldErrorVM> fieldErrors;

  public void add(String objectName, String field, String message) {
    if (fieldErrors == null) {
      fieldErrors = new ArrayList<>();
    }
    fieldErrors.add(
        FieldErrorVM.builder().objectName(objectName).field(field).message(message).build());
  }
}
