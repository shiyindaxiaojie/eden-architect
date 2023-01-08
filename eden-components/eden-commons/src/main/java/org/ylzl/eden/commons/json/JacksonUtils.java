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

package org.ylzl.eden.commons.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.json.jackson.exception.JacksonException;
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
 * @since 2.4.13
 */
@UtilityClass
public class JacksonUtils {

	private static final ObjectMapper defaultObjectMapper = new DefaultObjectMapper();

	private static final XmlMapper defaultXmlMapper = new DefaultXmlMapper();

	public static ObjectMapper getDefaultObjectMapper() {
		return defaultObjectMapper;
	}

	public static XmlMapper getDefaultXmlMapper() {
		return defaultXmlMapper;
	}

	public static <T> String toJSONString(T object) {
		return toJSONString(object, Include.USE_DEFAULTS);
	}

	public static <T> String toJSONString(T object, Include include) {
		return toJSONString(object, include, getDefaultObjectMapper());
	}

	public static <T> String toJSONString(T object, Include include, ObjectMapper objectMapper) {
		if (objectMapper == null) {
			objectMapper = getDefaultObjectMapper();
		}
		try {
			return getObjectWriter(include, objectMapper).writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new JacksonException(e);
		}
	}

	public static String toJSONString(String text, Class<?> cls) {
		return toJSONString(text, cls, Include.USE_DEFAULTS, getDefaultObjectMapper(), getDefaultXmlMapper());
	}

	public static String toJSONString(String text, Class<?> cls, Include include) {
		return toJSONString(text, cls, include, getDefaultObjectMapper(), getDefaultXmlMapper());
	}

	public static String toJSONString(String text, Class<?> cls, Include include,
									  ObjectMapper objectMapper, XmlMapper xmlMapper) {
		if (objectMapper == null) {
			objectMapper = getDefaultObjectMapper();
		}
		if (xmlMapper == null) {
			xmlMapper = getDefaultXmlMapper();
		}
		try {
			Object object = xmlMapper.setSerializationInclusion(include).readValue(text, cls);
			return toJSONString(object, include, objectMapper);
		} catch (JsonProcessingException e) {
			throw new JacksonException(e);
		}
	}

	public static <T> T parseObject(String text, Class<T> cls, ObjectMapper objectMapper) {
		try {
			return objectMapper.readValue(text, cls);
		} catch (IOException e) {
			throw new JacksonException(e);
		}
	}

	public static <T> T parseObject(String text, Class<T> cls) {
		return parseObject(text, cls, getDefaultObjectMapper());
	}

	public static <K, V, T> T parseObject(Map<K, V> map, Class<T> cls, ObjectMapper objectMapper) {
		return objectMapper.convertValue(map, cls);
	}

	public static <K, V, T> T parseObject(Map<K, V> map, Class<T> cls) {
		return parseObject(map, cls, getDefaultObjectMapper());
	}

	public static String toXMLString(String text, Include include) throws IOException {
		return toXMLString(text, include, getDefaultObjectMapper(), getDefaultXmlMapper());
	}

	public static String toXMLString(String text, Include include, ObjectMapper objectMapper, XmlMapper xmlMapper)
		throws IOException {
		if (objectMapper == null) {
			objectMapper = getDefaultObjectMapper();
		}
		if (xmlMapper == null) {
			xmlMapper = getDefaultXmlMapper();
		}
		ObjectWriter objectWriter = getObjectWriter(include, xmlMapper);
		return StringUtils.trimToEmpty(
			objectWriter.writeValueAsString(objectMapper.readTree(text)));
	}

	public static String toXMLString(String text, Class<?> cls, Include include) {
		return toXMLString(text, cls, include, getDefaultObjectMapper(), getDefaultXmlMapper());
	}

	public static String toXMLString(String text, Class<?> cls, Include include,
									 ObjectMapper objectMapper, XmlMapper xmlMapper) {
		try {
			Object object = parseObject(text, cls, objectMapper);
			ObjectWriter objectWriter = getObjectWriter(include, xmlMapper);
			return StringUtils.trimToEmpty(objectWriter.writeValueAsString(object));
		} catch (IOException e) {
			throw new JacksonException(e);
		}
	}

	public static String toXMLString(Object object, XmlMapper xmlMapper) {
		try {
			return xmlMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new JacksonException(e);
		}
	}

	public static String toXMLString(Object object) {
		return toXMLString(object, getDefaultXmlMapper());
	}

	public static <K, V> Map<K, V> parseMap(String text) {
		return parseMap(text, getDefaultObjectMapper());
	}

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> parseMap(String text, ObjectMapper objectMapper) {
		try {
			return objectMapper.readValue(text, Map.class);
		} catch (JsonProcessingException e) {
			throw new JacksonException(e);
		}
	}

	public static <T> List<T> parseList(String text, Class<T> cls) {
		return parseList(text, cls, getDefaultObjectMapper());
	}

	public static <T> List<T> parseList(String text, Class<T> cls, ObjectMapper objectMapper) {
		List<Map<Object, Object>> list;
		try {
			list = objectMapper.readValue(text, new TypeReference<List<Map<Object, Object>>>() {
			});
		} catch (JsonProcessingException e) {
			throw new JacksonException(e);
		}
		List<T> result = new ArrayList<T>();
		for (Map<Object, Object> map : list) {
			result.add(parseObject(map, cls, objectMapper));
		}
		return result;
	}

	private static ObjectWriter getObjectWriter(Include include, ObjectMapper objectMapper) {
		return objectMapper.setSerializationInclusion(include).writer();
	}

	private static ObjectWriter getObjectWriter(Include include, XmlMapper xmlMapper) {
		return xmlMapper.setSerializationInclusion(include).writer();
	}

}
