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
package org.ylzl.eden.spring.integration.truelicense.manager.conditional;

import org.ylzl.eden.commons.lang.type.PrimitiveTypeEnum;

/**
 * 动态条件枚举
 *
 * @author gyl
 * @since 2.4.x
 */
public enum ConditionalEnum {
  EQ(
      new Comparator() {

        @Override
        public boolean compare(String source, String target, String type) {
          PrimitiveTypeEnum primitiveTypeEnum = PrimitiveTypeEnum.toPrimitiveTypeEnum(type);
          if (primitiveTypeEnum != null) {
            if (primitiveTypeEnum.getHandler().cast(source)
                == primitiveTypeEnum.getHandler().cast(target)) {
              return true;
            }
          }
          return false;
        }
      }),
  NE(
      new Comparator() {

        @Override
        public boolean compare(String source, String target, String type) {
          PrimitiveTypeEnum primitiveTypeEnum = PrimitiveTypeEnum.toPrimitiveTypeEnum(type);
          if (primitiveTypeEnum != null) {
            if (primitiveTypeEnum.getHandler().cast(source)
                != primitiveTypeEnum.getHandler().cast(target)) {
              return true;
            }
          }
          return false;
        }
      }),
  GT(
      new Comparator() {

        @Override
        public boolean compare(String source, String target, String type) {
          PrimitiveTypeEnum primitiveTypeEnum = PrimitiveTypeEnum.toPrimitiveTypeEnum(type);
          if (primitiveTypeEnum != null) {
            switch (primitiveTypeEnum) {
              case BOOLEAN:
                break;
              case BYTE:
                if ((Byte) primitiveTypeEnum.getHandler().cast(source)
                    > (Byte) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case CHAR:
                if (((Character) primitiveTypeEnum.getHandler().cast(source))
                        .compareTo((Character) primitiveTypeEnum.getHandler().cast(target))
                    > 0) {
                  return true;
                }
                break;
              case DOUBLE:
                if ((Double) primitiveTypeEnum.getHandler().cast(source)
                    > (Double) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case FLOAT:
                if ((Float) primitiveTypeEnum.getHandler().cast(source)
                    > (Float) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case INTEGER:
                if ((Integer) primitiveTypeEnum.getHandler().cast(source)
                    > (Integer) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case LONG:
                if ((Long) primitiveTypeEnum.getHandler().cast(source)
                    > (Long) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case SHORT:
                if ((Short) primitiveTypeEnum.getHandler().cast(source)
                    > (Short) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
            }
          }
          return false;
        }
      }),
  LT(
      new Comparator() {

        @Override
        public boolean compare(String source, String target, String type) {
          PrimitiveTypeEnum primitiveTypeEnum = PrimitiveTypeEnum.toPrimitiveTypeEnum(type);
          if (primitiveTypeEnum != null) {
            switch (primitiveTypeEnum) {
              case BOOLEAN:
                break;
              case BYTE:
                if ((Byte) primitiveTypeEnum.getHandler().cast(source)
                    < (Byte) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case CHAR:
                if (((Character) primitiveTypeEnum.getHandler().cast(source))
                        .compareTo((Character) primitiveTypeEnum.getHandler().cast(target))
                    < 0) {
                  return true;
                }
                break;
              case DOUBLE:
                if ((Double) primitiveTypeEnum.getHandler().cast(source)
                    < (Double) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case FLOAT:
                if ((Float) primitiveTypeEnum.getHandler().cast(source)
                    < (Float) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case INTEGER:
                if ((Integer) primitiveTypeEnum.getHandler().cast(source)
                    < (Integer) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case LONG:
                if ((Long) primitiveTypeEnum.getHandler().cast(source)
                    < (Long) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case SHORT:
                if ((Short) primitiveTypeEnum.getHandler().cast(source)
                    < (Short) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
            }
          }
          return false;
        }
      }),
  GE(
      new Comparator() {

        @Override
        public boolean compare(String source, String target, String type) {
          PrimitiveTypeEnum primitiveTypeEnum = PrimitiveTypeEnum.toPrimitiveTypeEnum(type);
          if (primitiveTypeEnum != null) {
            switch (primitiveTypeEnum) {
              case BOOLEAN:
                break;
              case BYTE:
                if ((Byte) primitiveTypeEnum.getHandler().cast(source)
                    >= (Byte) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case CHAR:
                if (((Character) primitiveTypeEnum.getHandler().cast(source))
                        .compareTo((Character) primitiveTypeEnum.getHandler().cast(target))
                    >= 0) {
                  return true;
                }
                break;
              case DOUBLE:
                if ((Double) primitiveTypeEnum.getHandler().cast(source)
                    >= (Double) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case FLOAT:
                if ((Float) primitiveTypeEnum.getHandler().cast(source)
                    >= (Float) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case INTEGER:
                if ((Integer) primitiveTypeEnum.getHandler().cast(source)
                    >= (Integer) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case LONG:
                if ((Long) primitiveTypeEnum.getHandler().cast(source)
                    >= (Long) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case SHORT:
                if ((Short) primitiveTypeEnum.getHandler().cast(source)
                    >= (Short) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
            }
          }
          return false;
        }
      }),
  LE(
      new Comparator() {

        @Override
        public boolean compare(String source, String target, String type) {
          PrimitiveTypeEnum primitiveTypeEnum = PrimitiveTypeEnum.toPrimitiveTypeEnum(type);
          if (primitiveTypeEnum != null) {
            switch (primitiveTypeEnum) {
              case BOOLEAN:
                break;
              case BYTE:
                if ((Byte) primitiveTypeEnum.getHandler().cast(source)
                    <= (Byte) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case CHAR:
                if (((Character) primitiveTypeEnum.getHandler().cast(source))
                        .compareTo((Character) primitiveTypeEnum.getHandler().cast(target))
                    <= 0) {
                  return true;
                }
                break;
              case DOUBLE:
                if ((Double) primitiveTypeEnum.getHandler().cast(source)
                    <= (Double) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case FLOAT:
                if ((Float) primitiveTypeEnum.getHandler().cast(source)
                    <= (Float) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case INTEGER:
                if ((Integer) primitiveTypeEnum.getHandler().cast(source)
                    <= (Integer) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case LONG:
                if ((Long) primitiveTypeEnum.getHandler().cast(source)
                    <= (Long) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
              case SHORT:
                if ((Short) primitiveTypeEnum.getHandler().cast(source)
                    <= (Short) primitiveTypeEnum.getHandler().cast(target)) {
                  return true;
                }
                break;
            }
          }
          return false;
        }
      });

  private final Comparator comparator;

  ConditionalEnum(Comparator comparator) {
    this.comparator = comparator;
  }

  public static ConditionalEnum toConditionalEnum(String name) {
    for (ConditionalEnum conditionalEnum : ConditionalEnum.values()) {
      if (conditionalEnum.name().equalsIgnoreCase(name)) {
        return conditionalEnum;
      }
    }
    return null;
  }

  public Comparator getComparator() {
    return comparator;
  }

  public interface Comparator {

    boolean compare(String source, String target, String type);
  }
}
