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

package org.ylzl.eden.data.masker.integration.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.data.masker.DataMaskerManager;
import org.ylzl.eden.data.masker.Masking;

import java.io.IOException;

/**
 * Jackson 数据脱敏自定义序列化
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class JacksonDataMaskingJsonSerializer extends JsonSerializer<Object> {

	private final Masking masking;

	private final JsonSerializer<Object> serializer;

	public JacksonDataMaskingJsonSerializer(Masking masking, JsonSerializer<Object> serializer) {
		this.masking = masking;
		this.serializer = serializer;
	}

	@Override
	public void serialize(Object object, JsonGenerator jsonGenerator,
						  SerializerProvider serializerProvider) throws IOException {
		if (ObjectUtils.isNotNull(object) && object instanceof String) {
			String data = ObjectUtils.trimToString(object);
			object = DataMaskerManager.getDataMasker(masking.value()).masking(data);
		}

		if (this.serializer != null) {
			this.serializer.serialize(object, jsonGenerator, serializerProvider);
		} else {
			serializerProvider.defaultSerializeValue(object, jsonGenerator);
		}
	}
}
