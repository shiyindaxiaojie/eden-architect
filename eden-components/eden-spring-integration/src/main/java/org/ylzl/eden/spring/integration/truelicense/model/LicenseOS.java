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
package org.ylzl.eden.spring.integration.truelicense.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 许可证操作系统
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class LicenseOS implements Serializable {

	private static final long serialVersionUID = 666760616367846885L;

	/**
	 * CPU 序列号
	 */
	private List<String> cpuSerial;

	/**
	 * 主板序列号
	 */
	private List<String> mainBoardSerial;

	/**
	 * IP 地址
	 */
	private List<String> ipAddress;

	/**
	 * MAC 地址
	 */
	private List<String> macAddress;
}
