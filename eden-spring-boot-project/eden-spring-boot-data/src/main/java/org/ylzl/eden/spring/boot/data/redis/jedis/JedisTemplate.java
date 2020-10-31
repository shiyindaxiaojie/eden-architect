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

package org.ylzl.eden.spring.boot.data.redis.jedis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.util.JedisClusterCRC16;

import java.util.*;

/**
 * JedisTemplate
 *
 * @author gyl
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class JedisTemplate extends RedisTemplate<String, Object> {

  @Autowired(required = false)
  private FixedJedisCluster jedisCluster;

  public JedisTemplate() {
    RedisSerializer<String> stringSerializer = new StringRedisSerializer();
    setKeySerializer(stringSerializer);
    setHashKeySerializer(stringSerializer);
    setHashValueSerializer(stringSerializer);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
        new Jackson2JsonRedisSerializer(Object.class);
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
    setValueSerializer(jackson2JsonRedisSerializer);
  }

  public <T> void executePipelinedCluster(Collection<T> datas, RedisPipelineCallback<T> callback) {
    Map<JedisPool, List<T>> groupDatas = new HashMap<>();

    if (jedisCluster == null) {
      throw new UnsupportedOperationException("FixedJedisCluster is not injected");
    }

    jedisCluster.refreshCluster();
    for (T data : datas) {
      String key = callback.key(data);
      int slot = JedisClusterCRC16.getSlot(key);
      JedisPool jedisPool = jedisCluster.getConnectionHandler().getJedisPoolFromSlot(slot);
      if (groupDatas.keySet().contains(jedisPool)) {
        List<T> pooldata = groupDatas.get(jedisPool);
        pooldata.add(data);
      } else {
        List<T> newDatas = new ArrayList<>();
        newDatas.add(data);
        groupDatas.put(jedisPool, newDatas);
      }
    }

    for (Map.Entry<JedisPool, List<T>> entry : groupDatas.entrySet()) {
      Jedis jedis = entry.getKey().getResource();
      Pipeline pipeline;
      try {
        pipeline = jedis.pipelined();
        List<T> groupData = entry.getValue();
        for (T data : groupData) {
          pipeline.setex(callback.key(data), callback.expires(data), callback.value(data));
        }
        pipeline.sync();
      } finally {
        if (jedis != null) {
          jedis.close();
        }
      }
    }
  }

  public static interface RedisPipelineCallback<T> {

    String key(T data);

    int expires(T data);

    String value(T data);
  }
}
