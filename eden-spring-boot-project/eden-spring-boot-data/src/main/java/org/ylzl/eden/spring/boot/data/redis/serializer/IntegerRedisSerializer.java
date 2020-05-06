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

package org.ylzl.eden.spring.boot.data.redis.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Integer Redis 序列化
 *
 * @author gyl
 * @since 0.0.1
 */
public enum IntegerRedisSerializer implements RedisSerializer<Integer> {
  INSTANCE;

  @Override
  public byte[] serialize(Integer i) throws SerializationException {
    if (null != i) {
      return i.toString().getBytes();
    } else {
      return new byte[0];
    }
  }

  @Override
  public Integer deserialize(byte[] bytes) throws SerializationException {
    if (bytes.length > 0) {
      return Integer.parseInt(new String(bytes));
    } else {
      return null;
    }
  }
}
