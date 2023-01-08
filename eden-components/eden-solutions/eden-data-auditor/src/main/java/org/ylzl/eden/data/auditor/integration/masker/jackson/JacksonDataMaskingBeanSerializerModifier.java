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

package org.ylzl.eden.data.auditor.integration.masker.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.type.MapType;
import org.ylzl.eden.data.auditor.masker.DataMasking;

import java.util.ArrayList;
import java.util.List;

/**
 * Jackson 数据脱敏自定义序列化
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class JacksonDataMaskingBeanSerializerModifier extends BeanSerializerModifier {

	@Override
	public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
		List<BeanPropertyWriter> newWriters = new ArrayList<>();
		for (BeanPropertyWriter writer : beanProperties) {
			DataMasking dataMasking = writer.getAnnotation(DataMasking.class);
			if (dataMasking != null && writer.getType().isTypeOrSubTypeOf(String.class)) {
				JsonSerializer<Object> serializer = new JacksonDataMaskingJsonSerializer(dataMasking, writer.getSerializer());
				writer.assignSerializer(serializer);
			}
			newWriters.add(writer);
		}
		return newWriters;
	}

	@Override
	public JsonSerializer<?> modifyMapSerializer(SerializationConfig config, MapType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
		return super.modifyMapSerializer(config, valueType, beanDesc, serializer);
	}
}
