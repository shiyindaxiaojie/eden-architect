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
package org.ylzl.eden.commons.env.os;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.ylzl.eden.commons.regex.RegexUtils;

/**
 * 操作系统枚举
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public enum OSEnum {
	AIX("AIX"),
	DIGITAL_UNIX("Digital Unix"),
	FREDD_BSD("FreeBSD"),
	HP_UX("HP-UX"),
	IRIX("Irix"),
	LINUX("Linux"),
	MAS_OS_X("Mac OS X"), // 设置在 Mac OS 之前遍历
	MAS_OS("Mac OS"),
	NET_WARE_411("NetWare"),
	MPEIX("MPE/iX"),
	OS2("OS/2"),
	OS390("OS/390"),
	OSF1("OSF1"),
	OPEN_VMS("OpenVMS"),
	SOLARIS("Solaris"),
	SUN_OS("SunOS"),
	WINDOWS("Windows");

	@Getter
	@Setter
	private String name;

	OSEnum(String name) {
		this.name = name;
	}

	public static OSEnum toOSEnum(@NonNull String name) {
		for (OSEnum osEnum : OSEnum.values()) {
			if (RegexUtils.find(osEnum.getName(), name)) {
				return osEnum;
			}
		}
		return null;
	}
}
