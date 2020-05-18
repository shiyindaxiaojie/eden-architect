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

package org.ylzl.eden.spring.boot.security.oauth2.token.store;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 持久化令牌缓存
 *
 * @author gyl
 * @since 1.0.0
 */
public class PersistentTokenCache<T> {

  private final long expireMillis;

  private final Map<String, Value> map;

  private long latestWriteTime;

  public PersistentTokenCache(long expireMillis) {
    if (expireMillis <= 0l) {
      throw new IllegalArgumentException();
    }
    this.expireMillis = expireMillis;

    map = new LinkedHashMap<>(64, 0.75f);
    latestWriteTime = System.currentTimeMillis();
  }

  public T get(String key) {
    purge();
    final Value val = map.get(key);
    final long time = System.currentTimeMillis();
    return val != null && time < val.expire ? val.token : null;
  }

  public void put(String key, T token) {
    purge();
    if (map.containsKey(key)) {
      map.remove(key);
    }
    final long time = System.currentTimeMillis();
    map.put(key, new Value(token, time + expireMillis));
    latestWriteTime = time;
  }

  public int size() {
    return map.size();
  }

  public void purge() {
    long time = System.currentTimeMillis();
    if (time - latestWriteTime > expireMillis) {
      map.clear();
    } else {
      Iterator<Value> values = map.values().iterator();
      while (values.hasNext()) {
        if (time >= values.next().expire) {
          values.remove();
        } else {
          break;
        }
      }
    }
  }

  /**
   * 令牌值对象
   *
   * @author gyl
   * @since 1.0.0
   */
  private class Value {

    private final T token;

    private final long expire;

    Value(T token, long expire) {
      this.token = token;
      this.expire = expire;
    }
  }
}
