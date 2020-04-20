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

package org.ylzl.eden.spring.boot.security.core.login;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 登录视图模型
 *
 * @author gyl
 * @since 1.0.0
 */
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Login implements Serializable {

    private static final long serialVersionUID = -4828891329390986045L;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private Boolean rememberMe;
}
