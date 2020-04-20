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
package org.ylzl.eden.spring.boot.data.neo4j.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Neo4j 数据仓库
 *
 * <P>Spring Data Neo4j 升级到 5.X</P>
 * <ul>
 *     <li>org.springframework.data.neo4j.repository.GraphRepository 变更为 {@link org.springframework.data.neo4j.repository.Neo4jRepository}</li>
 * </ul>
 *
 * @author gyl
 * @since 2.0.0
 */
@NoRepositoryBean
public interface Neo4jRepository<T, ID extends Serializable> extends org.springframework.data.neo4j.repository.Neo4jRepository<T, ID> {

}
