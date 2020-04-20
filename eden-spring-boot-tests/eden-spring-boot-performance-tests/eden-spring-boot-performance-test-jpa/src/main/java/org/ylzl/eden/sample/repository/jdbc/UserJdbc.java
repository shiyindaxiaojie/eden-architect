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

import lombok.NonNull;
import org.springframework.stereotype.Repository;
import org.ylzl.eden.sample.domain.User;
import org.ylzl.eden.spring.boot.data.jdbc.namedparam.EnhancedNamedParameterJdbcTemplate;

import java.util.List;

/**
 * TODO
 *
 * @author gyl
 * @since 0.0.1
 */
@Repository
public class UserJdbc {

	private final EnhancedNamedParameterJdbcTemplate enhancedNamedParameterJdbcTemplate;

	public UserJdbc(EnhancedNamedParameterJdbcTemplate enhancedNamedParameterJdbcTemplate) {
		this.enhancedNamedParameterJdbcTemplate = enhancedNamedParameterJdbcTemplate;
	}

	public int[] batchInsert(@NonNull List<User> users, int executeBatchSize) {
		return enhancedNamedParameterJdbcTemplate.batchInsert(users, executeBatchSize);
	}
}