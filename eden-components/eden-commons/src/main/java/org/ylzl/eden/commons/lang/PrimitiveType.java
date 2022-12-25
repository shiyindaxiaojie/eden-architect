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

package org.ylzl.eden.commons.lang;

import lombok.Getter;
import lombok.NonNull;

/**
 * 基本类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public enum PrimitiveType {

	BOOLEAN(Boolean.class) {

		@Override
		public Object cast(String value) {
			return Boolean.parseBoolean(value);
		}
	},
	BYTE(Byte.class) {

		@Override
		public Object cast(String value) {
			return value == null ? (byte) 0 : Byte.parseByte(value);
		}
	},
	CHAR(Character.class) {

		@Override
		public Object cast(String value) {
			return value == null ? '\u0000' : value.charAt(0);
		}
	},
	DOUBLE(Double.class) {

		@Override
		public Object cast(String value) {
			return value == null ? 0.0d : Double.parseDouble(value);
		}
	},
	FLOAT(Float.class) {

		@Override
		public Object cast(String value) {;
			return value == null ? 0.0f : Float.parseFloat(value);
		}
	},
	INTEGER(Integer.class) {

		@Override
		public Object cast(String value) {
			return value == null ? 0 : Integer.parseInt(value);
		}
	},
	LONG(Long.class) {

		@Override
		public Object cast(String value) {
			return value == null ? 0L : Long.parseLong(value);
		}
	},
	SHORT(Short.class) {

		@Override
		public Object cast(String value) {
			return value == null ? (short) 0 : Short.parseShort(value);
		}
	};

	@Getter
	private final Class<?> wrapperClass;

	public abstract Object cast(String value);

	PrimitiveType(Class<?> wrapperClass) {
		this.wrapperClass = wrapperClass;
	}

	public static PrimitiveType parse(@NonNull String type) {
		for (PrimitiveType primitiveType : PrimitiveType.values()) {
			if (primitiveType.name().equalsIgnoreCase(type)) {
				return primitiveType;
			}
		}
		return null;
	}
}
