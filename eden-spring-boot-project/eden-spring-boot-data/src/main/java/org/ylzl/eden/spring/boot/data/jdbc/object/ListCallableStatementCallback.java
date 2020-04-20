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
package org.ylzl.eden.spring.boot.data.jdbc.object;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.ylzl.eden.spring.boot.data.jdbc.mapper.MapRowMapper;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 存储据过程回调
 *
 * @author gyl
 * @since 1.0.0
 */
public class ListCallableStatementCallback implements CallableStatementCallback<Object> {

    @Override
    public Object doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        ResultSet resultSet = null;
        try {
            callableStatement.execute();
            resultSet = (ResultSet) callableStatement.getObject(1);
            int rowNum = 1;
            while (resultSet.next()) {
                resultList.add(MapRowMapper.builder().build().mapRow(resultSet, rowNum));
                rowNum++;
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (callableStatement != null) {
                callableStatement.close();
            }
        }
        return resultList;
    }
}
