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
package org.ylzl.eden.spring.data.jdbc.object;

import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.*;
import org.ylzl.eden.spring.data.jdbc.mapper.MapRowMapper;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存储过程实现
 *
 * @author gyl
 * @since 1.0.0
 */
public class ListStoredProcedure extends StoredProcedure {

  @Getter @Setter private boolean hasOutParameter;

  public ListStoredProcedure(DataSource dataSource, String sql) {
    setDataSource(dataSource);
    setSql(sql);
  }

  public ListStoredProcedure(JdbcTemplate jdbcTemplate, String sql) {
    setJdbcTemplate(jdbcTemplate);
    setSql(sql);
  }

  @Override
  public Map<String, Object> execute(String param) {
    if (!isHasOutParameter()) {
      return execute();
    }
    declareParameter(new SqlOutParameter(param, TYPES_RESULT));
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(param, new ListCallableStatementCallback());
    return execute(map);
  }

  @Override
  public List<SqlParameter> getDeclaredParameters() {
    return super.getDeclaredParameters();
  }

  public void setSqlParameter(String param, String value) {
    declareParameter(new SqlParameter(param, Types.VARCHAR));
  }

  public void setSqlParameter(String param, Double value) {
    declareParameter(new SqlParameter(param, Types.DOUBLE));
  }

  public void setSqlParameter(String param, Integer value) {
    declareParameter(new SqlParameter(param, Types.INTEGER));
  }

  public void setSqlOutParameter(String param, String value) {
    declareParameter(new SqlOutParameter(param, Types.VARCHAR));
  }

  public void setSqlOutParameter(String param, Double value) {
    declareParameter(new SqlOutParameter(param, Types.DOUBLE));
  }

  public void setSqlOutParameter(String param, Integer value) {
    declareParameter(new SqlOutParameter(param, Types.INTEGER));
  }

  public void setResultsetOutParam(String param, RowMapper<?> rowMap) {
    declareParameter(new SqlReturnResultSet(param, rowMap));
  }

  public void setResultsetOutParam(String param) {
    declareParameter(new SqlReturnResultSet(param, new MapRowMapper()));
  }
}
