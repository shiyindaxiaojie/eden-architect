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

package org.ylzl.eden.spring.boot.commons.lang.type;

import lombok.Getter;
import lombok.NonNull;

/**
 * 基本类型枚举
 *
 * @author gyl
 * @since 0.0.1
 */
public enum PrimitiveTypeEnum {
  BOOLEAN(
      new Handler() {

        @Override
        public Object cast(String value) {
          return value == null ? false : Boolean.parseBoolean(value);
        }
      },
      Boolean.class),
  BYTE(
      new Handler() {

        @Override
        public Object cast(String value) {
          return value == null ? (byte) 0 : Byte.parseByte(value);
        }
      },
      Byte.class),
  CHAR(
      new Handler() {

        @Override
        public Object cast(String value) {
          return value == null ? '\u0000' : value.charAt(0);
        }
      },
      Character.class),
  DOUBLE(
      new Handler() {

        @Override
        public Object cast(String value) {
          return value == null ? 0.0d : Double.parseDouble(value);
        }
      },
      Double.class),
  FLOAT(
      new Handler() {

        @Override
        public Object cast(String value) {
          return value == null ? 0.0f : Float.parseFloat(value);
        }
      },
      Float.class),
  INTEGER(
      new Handler() {

        @Override
        public Object cast(String value) {
          return value == null ? 0 : Integer.parseInt(value);
        }
      },
      Integer.class),
  LONG(
      new Handler() {

        @Override
        public Object cast(String value) {
          return value == null ? 0L : Long.parseLong(value);
        }
      },
      Long.class),
  SHORT(
      new Handler() {

        @Override
        public Object cast(String value) {
          return value == null ? (short) 0 : Short.parseShort(value);
        }
      },
      Short.class);

  @Getter private final Handler handler;

  @Getter private final Class<?> wrapperClass;

  PrimitiveTypeEnum(Handler handler, Class<?> wrapperClass) {
    this.handler = handler;
    this.wrapperClass = wrapperClass;
  }

  public static PrimitiveTypeEnum toPrimitiveTypeEnum(@NonNull String type) {
    for (PrimitiveTypeEnum primitiveTypeEnum : PrimitiveTypeEnum.values()) {
      if (primitiveTypeEnum.name().equalsIgnoreCase(type)) {
        return primitiveTypeEnum;
      }
    }
    return null;
  }

  public interface Handler {
    Object cast(String value);
  }
}
