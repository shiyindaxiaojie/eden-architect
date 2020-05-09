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

package org.ylzl.eden.sample.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;
import org.ylzl.eden.sample.domain.User;
import org.ylzl.eden.spring.boot.commons.id.SnowflakeGenerator;
import org.ylzl.eden.spring.boot.commons.json.JacksonUtils;
import org.ylzl.eden.spring.boot.data.redis.jedis.FixedJedisCluster;
import org.ylzl.eden.spring.boot.data.redis.core.RedisClusterTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * 用户 Redis 读写测试
 *
 * @author gyl
 * @since 0.0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

  @Autowired private UserRepository userRepository;

  @Autowired private StringRedisTemplate redisTemplate;

  @Autowired private RedisClusterTemplate redisClusterTemplate;

  @Autowired(required = false)
  private FixedJedisCluster jedisCluster;

  @Test
  public void assertThatCURD() {
    SnowflakeGenerator generator =
        SnowflakeGenerator.builder().datacenterId(0L).workerId(0L).build();
    Date nowDate = new Date();

    String login = "batch";
    User user =
        User.builder()
            .id(generator.nextId())
            .login(login)
            .password("{noop}batch")
            .email("batch.com")
            .activated(false)
            .locked(false)
            .langKey("zh-CN")
            .createdBy("gyl")
            .createdDate(nowDate)
            .build();

    User createdUser = userRepository.save(user);
    assertNotNull(createdUser);

    User queryUser = userRepository.findOneByLogin(login);
    assertNotNull(queryUser);

    String modifyPassword = "233";
    queryUser.setPassword(modifyPassword);
    User modifiedUser = userRepository.save(queryUser);
    assertEquals(modifyPassword, modifiedUser.getPassword());

    userRepository.delete(modifiedUser.getId());
    assertNull(userRepository.findOneByLogin(login));
  }

  @Test
  public void assertThatPipeline() {
    int capacity = 81000;
    final List<User> users = new ArrayList<>(capacity);
    SnowflakeGenerator generator =
        SnowflakeGenerator.builder().datacenterId(0L).workerId(0L).build();
    Date nowDate = new Date();
    for (int i = 0; i < capacity; i++) {
      users.add(
          User.builder()
              .id(generator.nextId())
              /*				.login("batch" + i)
              .password("{noop}batch" + i)
              .email("batch" + i + ".com")
              .activated(false)
              .locked(false)
              .langKey("zh-CN")
              .createdBy("gyl")
              .createdDate(nowDate)*/
              .build());
    }
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    if (jedisCluster != null) {
      redisClusterTemplate.executePipelinedCluster(
          users,
          new RedisClusterTemplate.RedisPipelineCallback<User>() {

            @Override
            public String key(User data) {
              return "sample:users:" + data.getId();
            }

            @Override
            public int expires(User data) {
              return 30;
            }

            @Override
            public String value(User data) {
              return "1";
            }
          });
    } else {
      redisTemplate.executePipelined(
          new SessionCallback<Object>() {

            @SneakyThrows
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations)
                throws DataAccessException {
              for (User user : users) {
                redisTemplate
                    .opsForValue()
                    .set("sample:users:" + user.getId(), "1", 30, TimeUnit.SECONDS);
              }
              return null;
            }
          });
    }
    stopWatch.stop();
    double totalTimeSeconds = stopWatch.getTotalTimeSeconds();
    System.out.println("RedisTemplate.executePipelined cost " + totalTimeSeconds + " seconds");
    assertTrue(totalTimeSeconds <= 5d); // 理论上是 2秒内，电脑配置跟不上
  }

  @Test
  public void assertThatSaveAll() throws JsonProcessingException {
    int capacity = 81000;
    final List<User> users = new ArrayList<>(capacity);
    SnowflakeGenerator generator =
        SnowflakeGenerator.builder().datacenterId(0L).workerId(0L).build();
    Date nowDate = new Date();
    for (int i = capacity + 1; i < capacity << 1; i++) {
      users.add(
          User.builder()
              .id(generator.nextId())
              .login("batch" + i)
              .password("{noop}batch" + i)
              .email("batch" + i + ".com")
              .activated(false)
              .locked(false)
              .langKey("zh-CN")
              .createdBy("gyl")
              .createdDate(nowDate)
              .build());
    }
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    for (User user : users) {
      redisTemplate
          .opsForValue()
          .set(
              "sample:users:" + user.getId(),
              JacksonUtils.toJSONString(user),
              30,
              TimeUnit.SECONDS);
    }
    stopWatch.stop();
    double totalTimeSeconds = stopWatch.getTotalTimeSeconds();
    System.out.println(
        "RedisTemplate.opsForValue save all entity cost " + totalTimeSeconds + " seconds");
    assertTrue(totalTimeSeconds <= 60d);
  }
}
