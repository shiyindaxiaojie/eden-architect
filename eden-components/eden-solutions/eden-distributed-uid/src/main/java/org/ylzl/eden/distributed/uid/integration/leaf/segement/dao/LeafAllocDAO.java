/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.distributed.uid.integration.leaf.segement.dao;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.ylzl.eden.distributed.uid.integration.leaf.segement.model.LeafAlloc;

import javax.sql.DataSource;
import java.util.List;

/**
 * LeafAlloc 数据访问接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
public class LeafAllocDAO {

	private final SqlSessionFactory sqlSessionFactory;

	public LeafAllocDAO(DataSource dataSource) {
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("leaf-alloc", transactionFactory, dataSource);
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(LeafAllocMapper.class);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
	}

	public List<LeafAlloc> getAllLeafAllocs() {
		try (SqlSession sqlSession = sqlSessionFactory.openSession(false)) {
			return sqlSession.selectList(LeafAllocMapper.class.getName() + ".getAllLeafAllocs");
		}
	}

	public LeafAlloc updateMaxIdAndGetLeafAlloc(String tag) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			sqlSession.update(LeafAllocMapper.class.getName() + ".updateMaxId", tag);
			LeafAlloc result = sqlSession.selectOne(LeafAllocMapper.class.getName() + ".getLeafAlloc", tag);
			sqlSession.commit();
			return result;
		}
	}

	public LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			sqlSession.update(LeafAllocMapper.class.getName() + ".updateMaxIdByCustomStep", leafAlloc);
			LeafAlloc result = sqlSession.selectOne(LeafAllocMapper.class.getName() + ".getLeafAlloc", leafAlloc.getKey());
			sqlSession.commit();
			return result;
		}
	}

	public List<String> getAllTags() {
		try (SqlSession sqlSession = sqlSessionFactory.openSession(false)) {
			return sqlSession.selectList(LeafAllocMapper.class.getName() + ".getAllTags");
		}
	}
}
