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

package org.ylzl.eden.spring.data.flyway.datasource;

import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Flyway SQLite 数据源
 *
 * @author gyl
 * @since 2.4.x
 */
public class FlywaySQLiteDataSource extends SQLiteDataSource {

  private final DataSource dataSource;
  private Connection connection;

  public FlywaySQLiteDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return getConnectionWithKeepAlive();
  }

  private Connection getConnectionWithKeepAlive() throws SQLException {
    if (connection != null && !connection.isClosed()) {
      connection = dataSource.getConnection();
    }
    return connection;
  }
}
