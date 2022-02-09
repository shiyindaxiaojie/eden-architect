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

package org.ylzl.eden.commons.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.json.jackson.mapper.DefaultObjectMapper;
import org.ylzl.eden.commons.json.jackson.mapper.DefaultXmlMapper;
import org.ylzl.eden.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Jackson 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class JacksonUtils {

	private static DefaultObjectMapper defaultObjectMapper = new DefaultObjectMapper();

	private static DefaultXmlMapper defaultXmlMapper = new DefaultXmlMapper();

	public static <T> String toJSONString(T object, Include include, ObjectMapper objectMapper)
		throws JsonProcessingException {
		if (objectMapper == null) {
			objectMapper = getDefaultObjectMapper();
		}
		return getObjectWriter(include, objectMapper).writeValueAsString(object);
	}

	public static <T> String toJSONString(T object, Include include) throws JsonProcessingException {
		return toJSONString(object, include, getDefaultObjectMapper());
	}

	public static <T> String toJSONString(T object) throws JsonProcessingException {
		return toJSONString(object, null);
	}

	public static String toJSONString(
		String xmlString,
		Class<?> cls,
		Include include,
		ObjectMapper objectMapper,
		XmlMapper xmlMapper)
		throws IOException {
		if (objectMapper == null) {
			objectMapper = getDefaultObjectMapper();
		}
		if (xmlMapper == null) {
			xmlMapper = getDefaultXmlMapper();
		}
		Object object = xmlMapper.setSerializationInclusion(include).readValue(xmlString, cls);
		return toJSONString(object, include, objectMapper);
	}

	public static String toJSONString(String xmlString, Class<?> cls, Include include)
		throws IOException {
		return toJSONString(xmlString, cls, include, getDefaultObjectMapper(), getDefaultXmlMapper());
	}

	public static String toXMLString(
		String jsonString, Include include, ObjectMapper objectMapper, XmlMapper xmlMapper)
		throws IOException {
		if (objectMapper == null) {
			objectMapper = getDefaultObjectMapper();
		}
		if (xmlMapper == null) {
			xmlMapper = getDefaultXmlMapper();
		}
		ObjectWriter objectWriter = getObjectWriter(include, xmlMapper);
		return StringUtils.trimToEmpty(
			objectWriter.writeValueAsString(objectMapper.readTree(jsonString)));
	}

	public static String toXMLString(String jsonString, Include include) throws IOException {
		return toXMLString(jsonString, include, getDefaultObjectMapper(), getDefaultXmlMapper());
	}

	public static String toXMLString(
		String jsonString,
		Class<?> cls,
		Include include,
		ObjectMapper objectMapper,
		XmlMapper xmlMapper)
		throws IOException {
		Object object = toObject(jsonString, cls, objectMapper);
		ObjectWriter objectWriter = getObjectWriter(include, xmlMapper);
		return StringUtils.trimToEmpty(objectWriter.writeValueAsString(object));
	}

	public static String toXMLString(String jsonString, Class<?> cls, Include include)
		throws JsonProcessingException, IOException {
		return toXMLString(jsonString, cls, include, getDefaultObjectMapper(), getDefaultXmlMapper());
	}

	public static String toXMLString(Object object, XmlMapper xmlMapper)
		throws JsonProcessingException {
		return xmlMapper.writeValueAsString(object);
	}

	public static String toXMLString(Object object) throws JsonProcessingException {
		return toXMLString(object, getDefaultXmlMapper());
	}

	public static <T> T toObject(String jsonString, Class<T> cls, ObjectMapper objectMapper)
		throws IOException {
		return objectMapper.readValue(jsonString, cls);
	}

	public static <T> T toObject(String jsonString, Class<T> cls) throws IOException {
		return toObject(jsonString, cls, getDefaultObjectMapper());
	}

	public static <K, V, T> T toObject(Map<K, V> map, Class<T> cls, ObjectMapper objectMapper) {
		return objectMapper.convertValue(map, cls);
	}

	public static <K, V, T> T toObject(Map<K, V> map, Class<T> cls) {
		return toObject(map, cls, getDefaultObjectMapper());
	}

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> toMap(String jsonString, ObjectMapper objectMapper)
		throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(jsonString, Map.class);
	}

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> toMap(String jsonString) throws IOException {
		return toMap(jsonString, getDefaultObjectMapper());
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> toList(String jsonString, Class<T> cls, ObjectMapper objectMapper)
		throws IOException {
		List<Map<Object, Object>> list =
			objectMapper.readValue(jsonString, new TypeReference<List<Map<Object, Object>>>() {
			});
		List<T> result = new ArrayList<T>();
		for (Map<Object, Object> map : list) {
			result.add(toObject(map, cls, objectMapper));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> toList(String jsonString, Class<T> cls) throws IOException {
		return toList(jsonString, cls, getDefaultObjectMapper());
	}

	public static ObjectWriter getObjectWriter(Include include, ObjectMapper objectMapper) {
		return objectMapper.setSerializationInclusion(include).writerWithDefaultPrettyPrinter();
	}

	public static ObjectWriter getObjectWriter(Include include, XmlMapper xmlMapper) {
		return xmlMapper.setSerializationInclusion(include).writerWithDefaultPrettyPrinter();
	}

	private static ObjectMapper getDefaultObjectMapper() {
		return defaultObjectMapper;
	}

	private static XmlMapper getDefaultXmlMapper() {
		return defaultXmlMapper;
	}
}
