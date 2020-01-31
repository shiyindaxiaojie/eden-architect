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

package org.ylzl.eden.spring.boot.data.redis.support;

import com.sun.istack.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.Map;

/**
 * 增强式 RedisTemplate
 *
 * @author gyl
 * @since 0.0.1
 */
public class EnhancedRedisTemplate<K, V> extends RedisTemplate<K, V> {

    public void execute(@NotNull final Map<K, V> datas) {
        executePipelined(new SessionCallback<Object>() {

            @Override
            public <KK, VV> Object execute(RedisOperations<KK, VV> redisOperations) throws DataAccessException {
                for (Map.Entry<K, V> data: datas.entrySet()) {
					opsForValue().set(data.getKey(), data.getValue());
                }
                return null;
            }
        });
    }
}
