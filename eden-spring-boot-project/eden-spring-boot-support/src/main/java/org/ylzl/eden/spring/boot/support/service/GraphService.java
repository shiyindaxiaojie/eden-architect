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

/**
 * 图形数据库业务接口
 *
 * @author gyl
 * @since 0.0.1
 */
public interface GraphService<T> extends PagingAndSortingService<T, Long> {

  <S extends T> T save(S entity, int depth);

  <S extends T> Iterable<S> save(Iterable<S> entities, int depth);

  T findOne(Long id, int depth);

  Iterable<T> findAll();

  Iterable<T> findAll(int depth);

  Iterable<T> findAll(Sort sort);

  Iterable<T> findAll(Sort sort, int depth);

  Iterable<T> findAll(Iterable<Long> ids);

  Iterable<T> findAll(Iterable<Long> ids, int depth);

  Iterable<T> findAll(Iterable<Long> ids, Sort sort);

  Iterable<T> findAll(Iterable<Long> ids, Sort sort, int depth);

  Page<T> findAll(Pageable pageable);

  Page<T> findAll(Pageable pageable, int depth);
}
