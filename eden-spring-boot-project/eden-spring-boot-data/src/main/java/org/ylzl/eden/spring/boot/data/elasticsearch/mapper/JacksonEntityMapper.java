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

package org.ylzl.eden.spring.boot.data.elasticsearch.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.mapping.MappingException;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Jackson 实体映射器
 *
 * <p>变更日志：Spring Data Elastcisearch 升级到 3.X
 *
 * <ul>
 *   <li>{@link EntityMapper} 新增 {@code mapObject(Object source)}
 *   <li>{@link EntityMapper} 新增 {@code readObject(Map<String, Object> source, Class<T> targetType)}
 * </ul>
 *
 * @author gyl
 * @since 2.0.0
 */
public class JacksonEntityMapper implements EntityMapper {

  private ObjectMapper objectMapper;

  public JacksonEntityMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
    objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
    objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
  }

  @Override
  public String mapToString(Object object) throws IOException {
    return objectMapper.writeValueAsString(object);
  }

  @Override
  public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
    return objectMapper.readValue(source, clazz);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> mapObject(Object source) {
    try {
      return Collections.unmodifiableMap(
          objectMapper.readValue(mapToString(source), HashMap.class));
    } catch (IOException e) {
      throw new MappingException(e.getMessage(), e);
    }
  }

  @Override
  public <T> T readObject(Map<String, Object> source, Class<T> targetType) {
    try {
      return mapToObject(mapToString(source), targetType);
    } catch (IOException e) {
      throw new MappingException(e.getMessage(), e);
    }
  }
}
