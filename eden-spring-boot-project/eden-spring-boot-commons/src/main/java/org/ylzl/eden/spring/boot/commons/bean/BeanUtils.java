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

package org.ylzl.eden.spring.boot.commons.bean;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.spring.boot.commons.bean.annotation.BeanAlias;
import org.ylzl.eden.spring.boot.commons.lang.ObjectUtils;
import org.ylzl.eden.spring.boot.commons.lang.reflect.ReflectionUtils;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 对象实体工具集
 *
 * @author gyl
 * @since 0.0.1
 */
@UtilityClass
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

  @SuppressWarnings("unchecked")
  public static <T> T toBean(
      @NonNull Map<?, ?> sourceMap, @NonNull Class<? extends Object> clazz, T targetObject)
      throws InstantiationException, IllegalArgumentException, IllegalAccessException,
          ParseException {
    if (targetObject == null) {
      targetObject = (T) clazz.newInstance();
    }

    List<Field> fields = ReflectionUtils.getDeclaredFields(clazz);
    for (Field field : fields) {
      Object value;
      if (sourceMap.containsKey(field.getName())) {
        value = sourceMap.get(field.getName());
      } else if (field.isAnnotationPresent(BeanAlias.class)) {
        BeanAlias beanAlias = field.getAnnotation(BeanAlias.class);
        if (ObjectUtils.isEmpty(beanAlias) || !sourceMap.containsKey(beanAlias.value())) {
          continue;
        }
        value = sourceMap.get(beanAlias.value());
      } else {
        continue;
      }
      ReflectionUtils.setAccessible(field);
      try {
        field.set(targetObject, ObjectUtils.cast(value, field));
      } catch (Exception e) {
        throw new ParseException(
            MessageFormat.format(
                "字段 {0} 无法转换为类型 {1}，当前值为 {2}", field.getName(), field.getType(), value),
            0);
      }
    }
    return targetObject;
  }

  @SuppressWarnings("unchecked")
  public static <T> T toBean(@NonNull Map<?, ?> sourceMap, @NonNull Class<T> targetClass)
      throws InstantiationException, IllegalArgumentException, IllegalAccessException,
          ParseException {
    return toBean(sourceMap, targetClass, targetClass.newInstance());
  }

  @SuppressWarnings("unchecked")
  public static <T> T toBean(@NonNull Map<?, ?> sourceMap, @NonNull T targetObject)
      throws InstantiationException, IllegalArgumentException, IllegalAccessException,
          ParseException {
    return toBean(sourceMap, targetObject.getClass(), targetObject);
  }
}
