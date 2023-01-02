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

package org.ylzl.eden.commons.bean;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.bean.annotation.Alias;
import org.ylzl.eden.commons.bean.exception.BeanConvertException;
import org.ylzl.eden.commons.lang.MessageFormatUtils;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.commons.lang.reflect.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 对象实体工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 * @see org.apache.commons.beanutils.BeanUtils
 */
@UtilityClass
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

	private static final String CAN_NOT_CAST_TO_TYPE = "Field '{}' value '{}' can not cast to type {}";

	@SuppressWarnings("unchecked")
	public static <T> T toBean(@NonNull Map<?, ?> sourceMap, @NonNull Class<? extends Object> clazz, T targetObject) throws BeanConvertException {
		List<Field> fields = ReflectionUtils.getDeclaredFields(clazz);
		for (Field field : fields) {
			Object value;
			if (sourceMap.containsKey(field.getName())) {
				value = sourceMap.get(field.getName());
			} else if (field.isAnnotationPresent(Alias.class)) {
				Alias alias = field.getAnnotation(Alias.class);
				if (ObjectUtils.isEmpty(alias) || !sourceMap.containsKey(alias.value())) {
					continue;
				}
				value = sourceMap.get(alias.value());
			} else {
				continue;
			}
			ReflectionUtils.setAccessible(field);
			try {
				if (targetObject == null) {
					targetObject = (T) clazz.newInstance();
				}
				field.set(targetObject, ObjectUtils.cast(value, field));
			} catch (Exception ex) {
				throw new BeanConvertException(
					MessageFormatUtils.format(CAN_NOT_CAST_TO_TYPE, field.getName(), value, field.getType()), ex);
			}
		}
		return targetObject;
	}

	public static <T> T toBean(@NonNull Map<?, ?> sourceMap, @NonNull Class<T> targetClass) throws BeanConvertException {
		try {
			return toBean(sourceMap, targetClass, targetClass.newInstance());
		} catch (Exception e) {
			throw new BeanConvertException(e.getMessage(), e);
		}
	}

	public static <T> T toBean(@NonNull Map<?, ?> sourceMap, @NonNull T targetObject) throws BeanConvertException {
		return toBean(sourceMap, targetObject.getClass(), targetObject);
	}
}
