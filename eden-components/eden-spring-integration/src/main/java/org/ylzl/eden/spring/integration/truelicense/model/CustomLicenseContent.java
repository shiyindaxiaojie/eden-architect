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
package org.ylzl.eden.spring.integration.truelicense.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.ylzl.eden.commons.lang.time.DateFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 许可证存储
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AllArgsConstructor
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class CustomLicenseContent implements Serializable {

	private static final long serialVersionUID = 2321514142917945172L;

	/**
	 * 证书主题
	 */
	private String subject;

	/**
	 * 密钥别名
	 */
	private String privateAlias;

	/**
	 * 密钥密码
	 */
	@NotBlank
	private String keyPass;

	/**
	 * 访问秘钥库的密码
	 */
	@NotBlank
	private String storePass;

	/**
	 * 证书生成路径
	 */
	@JsonIgnore
	private String licensePath;

	/**
	 * 密钥库存储路径
	 */
	@JsonIgnore
	private String privateKeysStorePath;

	/**
	 * 证书签发时间
	 */
	@JsonIgnore
	private Date issued;

	/**
	 * 证书生效时间
	 */
	@JsonFormat(
		pattern = DateFormat.ISO_8601_EXTENDED_DATE,
		timezone = DateFormat.DEFAULT_TIME_ZONE)
	private Date notBefore;

	/**
	 * 证书到期时间
	 */
	@JsonFormat(
		pattern = DateFormat.ISO_8601_EXTENDED_DATE,
		timezone = DateFormat.DEFAULT_TIME_ZONE)
	private Date notAfter;

	/**
	 * 用户类型
	 */
	private String consumerType;

	/**
	 * 用户数量
	 */
	@Min(1)
	private Integer consumerAmount;

	/**
	 * 描述信息
	 */
	private String info;

	/**
	 * 授权扩展数据
	 */
	private LicenseContentExtra licenseContentExtra;
}
