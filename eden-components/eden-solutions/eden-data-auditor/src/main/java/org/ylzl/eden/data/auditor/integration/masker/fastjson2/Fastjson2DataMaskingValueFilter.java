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

package org.ylzl.eden.data.auditor.integration.masker.fastjson2;

import com.alibaba.fastjson2.filter.ValueFilter;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.data.auditor.masker.DataMaskerManager;
import org.ylzl.eden.data.auditor.masker.DataMasking;
import org.ylzl.eden.spring.framework.json.fastjson.FastjsonFilter;

import java.lang.reflect.Field;

/**
 * Fastjson 数据脱敏属性值过滤
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class Fastjson2DataMaskingValueFilter implements FastjsonFilter, ValueFilter {

    @Override
    public Object apply(Object object, String name, Object value) {
		if (!(value instanceof String) || ObjectUtils.isNull(value)) {
			return value;
		}
		try {
			Field field = object.getClass().getDeclaredField(name);
			if (String.class != field.getType()) {
				return value;
			}

			DataMasking dataMasking = field.getAnnotation(DataMasking.class);
			if (dataMasking == null) {
				return value;
			}

			String data = ObjectUtils.trimToString(value);
			return DataMaskerManager.getDataMasker(dataMasking.value()).masking(data);
		} catch (NoSuchFieldException e) {
			throw new Fastjson2ValueFilterException(e);
		}
	}
}
