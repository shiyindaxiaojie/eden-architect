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
package org.ylzl.eden.spring.boot.data.jdbc.mapper;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Map 行记录映射
 *
 * @author gyl
 * @since 1.0.0
 */
@Builder
@NoArgsConstructor
public class MapRowMapper implements RowMapper<Object> {

  @Override
  public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
    Map<String, Object> map = new HashMap<String, Object>();
    ResultSetMetaData rmd = rs.getMetaData();
    for (int i = 1; i <= rmd.getColumnCount(); i++) {
      map.put(rmd.getColumnLabel(i), rs.getObject(i));
    }
    return map;
  }
}
