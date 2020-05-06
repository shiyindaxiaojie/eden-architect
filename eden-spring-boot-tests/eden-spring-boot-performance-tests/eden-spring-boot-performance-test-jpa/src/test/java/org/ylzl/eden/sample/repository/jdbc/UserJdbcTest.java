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

package org.ylzl.eden.sample.repository.jdbc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.ylzl.eden.sample.domain.User;
import org.ylzl.eden.spring.boot.commons.id.SnowflakeGenerator;
import org.ylzl.eden.spring.boot.data.jdbc.namedparam.EnhancedNamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * 用户 JDBC 读写测试
 *
 * <p>使用 MySQL 测试，最好设置参数 innodb_flush_log_at_trx_commit=2
 *
 * @author gyl
 * @since 0.0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserJdbcTest {

  @Autowired private EnhancedNamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired private JdbcTemplate jdbcTemplate;

  @Autowired private DataSource dataSource;

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
    clear();
  }

  private void clear() {
    jdbcTemplate.execute("truncate table sample_user");
  }

  @Transactional
  @Rollback(false)
//  @Test
  public void assertThatBatchInsert() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    namedParameterJdbcTemplate.batchInsert(users, 500);
    stopWatch.stop();
    double totalTimeSeconds = stopWatch.getTotalTimeSeconds();
    System.out.println(
        "EnhancedNamedParameterJdbcTemplate.batchInsert cost " + totalTimeSeconds + " seconds");
    assertTrue(totalTimeSeconds <= 60d);
  }
}
