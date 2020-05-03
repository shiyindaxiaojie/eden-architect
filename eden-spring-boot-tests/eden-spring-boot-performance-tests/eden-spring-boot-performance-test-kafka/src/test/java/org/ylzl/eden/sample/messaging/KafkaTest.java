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

package org.ylzl.eden.sample.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;
import org.ylzl.eden.sample.domain.User;
import org.ylzl.eden.spring.boot.commons.id.SnowflakeGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Kafka 测试
 *
 * <p>使用 MySQL 测试，最好设置参数 innodb_flush_log_at_trx_commit=2
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaTest {

  @Autowired private UserProducer userProducer;

  @Autowired private JdbcTemplate jdbcTemplate;

  private List<User> users = new ArrayList<>();

  @Before
  public void init() {
    SnowflakeGenerator generator =
        SnowflakeGenerator.builder().datacenterId(0L).workerId(0L).build();
    Date nowDate = new Date();
    for (int i = 0; i < 1000000; i++) {
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
  }

  private void clear() {
    jdbcTemplate.execute("truncate table sample_user");
  }

  @Test
  public void assertThatBatchSend() throws JsonProcessingException, InterruptedException {
    clear();
    StopWatch stopWatch = new StopWatch();
    stopWatch.start("send");
    for (User user : users) {
      userProducer.send(user);
    }
    stopWatch.stop();
    log.debug("批次发送大小为 {}，耗时为 {} 秒", users.size(), stopWatch.getTotalTimeSeconds());
    Thread.sleep(1000L, 999999);
  }
}
