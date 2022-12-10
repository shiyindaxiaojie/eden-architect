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

package org.ylzl.eden.commons.env.os;

import lombok.Getter;
import lombok.NonNull;
import org.ylzl.eden.commons.regex.RegexUtils;

/**
 * 操作系统架构枚举
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
public enum OSArchitect {

	ALPHA("alpha"),
	AMD64("amd64"),
	ARM("arm"),
	ARMV41("armv41"),
	I386("i386"),
	I686("i686"),
	IA64N("IA64N"),
	MIPS("mips"),
	OS390("02.10.00"),
	PA_RISC("PA_RISC"),
	PA_RISC2("PA_RISC2.0"),
	PARISC("PA-RISC"),
	POWER_RS("POWER_RS"),
	POWERPC("PowerPC"),
	PPC("ppc"),
	PPC64("ppc64"),
	SPARC("sparc"),
	X86("x86"),
	X86_64("x86_64");

	private final String name;

	OSArchitect(String name) {
		this.name = name;
	}

	public static OSArchitect parse(@NonNull String name) {
		for (OSArchitect osArchitect : OSArchitect.values()) {
			if (RegexUtils.find(osArchitect.getName(), name)) {
				return osArchitect;
			}
		}
		return null;
	}
}
