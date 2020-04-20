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

package org.ylzl.eden.spring.boot.support.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Optional;

/**
 * Neo4j 业务接口
 *
 * @author gyl
 * @since 1.0.0
 */
public interface Neo4jService<T, ID extends Serializable> extends PagingAndSortingService<T, ID> {

    <S extends T> T save(S entity, int depth);

    <S extends T> Iterable<S> save(Iterable<S> entities, int depth);

    @Deprecated
	Optional<T> findOne(ID id, int depth);

	Optional<T> findById(ID id, int depth);

    Iterable<T> findAll(int depth);

    Iterable<T> findAll(Sort sort);

    Iterable<T> findAll(Sort sort, int depth);

	@Deprecated
	Iterable<T> findAll(Iterable<ID> ids, int depth);

	@Deprecated
	Iterable<T> findAll(Iterable<ID> ids, Sort sort);

	@Deprecated
	Iterable<T> findAll(Iterable<ID> ids, Sort sort, int depth);

	Iterable<T> findAllById(Iterable<ID> ids, int depth);

	Iterable<T> findAllById(Iterable<ID> ids, Sort sort);

	Iterable<T> findAllById(Iterable<ID> ids, Sort sort, int depth);

    Page<T> findAll(Pageable pageable);

    Page<T> findAll(Pageable pageable, int depth);
}
