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

package org.ylzl.eden.commons.lang;

import lombok.Getter;
import lombok.NonNull;

/**
 * 基本类型枚举
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public enum PrimitiveType {
	BOOLEAN(Boolean::parseBoolean, Boolean.class),
	BYTE(value -> value == null ? (byte) 0 : Byte.parseByte(value), Byte.class),
	CHAR(value -> value == null ? '\u0000' : value.charAt(0), Character.class),
	DOUBLE(value -> value == null ? 0.0d : Double.parseDouble(value), Double.class),
	FLOAT(value -> value == null ? 0.0f : Float.parseFloat(value), Float.class),
	INTEGER(value -> value == null ? 0 : Integer.parseInt(value), Integer.class),
	LONG(value -> value == null ? 0L : Long.parseLong(value), Long.class),
	SHORT(value -> value == null ? (short) 0 : Short.parseShort(value), Short.class);

	@Getter
	private final Handler handler;

	@Getter
	private final Class<?> wrapperClass;

	PrimitiveType(Handler handler, Class<?> wrapperClass) {
		this.handler = handler;
		this.wrapperClass = wrapperClass;
	}

	public static PrimitiveType toPrimitiveTypeEnum(@NonNull String type) {
		for (PrimitiveType primitiveType : PrimitiveType.values()) {
			if (primitiveType.name().equalsIgnoreCase(type)) {
				return primitiveType;
			}
		}
		return null;
	}

	public interface Handler {
		Object cast(String value);
	}
}
