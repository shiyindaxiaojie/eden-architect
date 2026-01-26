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
package org.ylzl.eden.spring.data.hibernate.dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.DatabaseVersion;

import java.sql.Types;

/**
 * H2 方言类
 *
 * <p>Hibernate 6.x API 变更：registerColumnType 方法已移除，
 * 使用构造器传递数据库版本，类型映射由 Hibernate 自动处理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class FixedH2Dialect extends H2Dialect {

	public FixedH2Dialect() {
		super();
		// Hibernate 6.x 中 registerColumnType 已被移除
		// FLOAT 类型映射由 Hibernate 自动处理，无需手动注册
	}
	
	public FixedH2Dialect(DatabaseVersion version) {
		super(version);
	}
}
