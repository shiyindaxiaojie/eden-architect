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
package org.ylzl.eden.spring.boot.data.jpa.id;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;
import org.ylzl.eden.spring.boot.commons.id.SnowflakeGenerator;

import java.io.Serializable;
import java.util.Properties;

/**
 * JPA 自定义生成器
 *
 * @author gyl
 * @since 0.0.1
 */
public class JpaIdentifierGenerator implements IdentifierGenerator, Configurable {

  public static final String NAME = "JPA_ID";

  public static final String STRATEGY =
      "org.ylzl.eden.spring.boot.data.jpa.id.JpaIdentifierGenerator";

  private SnowflakeGenerator snowflakeGenerator;

  public JpaIdentifierGenerator() {
    snowflakeGenerator = SnowflakeGenerator.builder().workerId(0L).datacenterId(0L).build();
  }

  @Override
  public void configure(Type type, Properties params, Dialect d) throws MappingException {}

  @Override
  public Serializable generate(SessionImplementor session, Object object)
      throws HibernateException {
    return snowflakeGenerator.nextId();
  }
}
