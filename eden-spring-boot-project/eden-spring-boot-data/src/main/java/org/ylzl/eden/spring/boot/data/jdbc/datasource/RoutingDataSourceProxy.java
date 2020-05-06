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
package org.ylzl.eden.spring.boot.data.jdbc.datasource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 动态数据源代理
 *
 * @author gyl
 * @since 0.0.1
 */
public class RoutingDataSourceProxy extends AbstractRoutingDataSource {

  private static final String MSG_UNKNOWN_DATASOURCE =
      "Invalid DataSource name in datasource properties";

  private AtomicInteger masterCount = new AtomicInteger();

  private AtomicInteger slaveCount = new AtomicInteger();

  @Getter @Setter private List<String> masterDataSources;

  @Getter @Setter private List<String> slaveDataSources;

  @SuppressWarnings("unchecked")
  @Override
  protected Object determineCurrentLookupKey() {
    DataSourceEnum dataSourceEnum = DataSourceContextHolder.get();
    switch (dataSourceEnum) {
      case MANUAL:
        return dataSourceEnum.getDataSourceName();
      case MASTER:
        return this.getDataSourceName(masterDataSources, masterCount);
      case SLAVE:
        return this.getDataSourceName(slaveDataSources, slaveCount);
    }
    return null;
  }

  private String getDataSourceName(List<String> dataSources, AtomicInteger count) {
    if (dataSources == null || dataSources.isEmpty()) {
      throw new RuntimeException(MSG_UNKNOWN_DATASOURCE);
    }

    int increment = count.getAndAdd(1);
    int lookupKey = increment % dataSources.size();
    return dataSources.get(lookupKey);
  }
}
