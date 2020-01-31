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
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

/**
 * 关系数据库业务接口
 *
 * @author gyl
 * @since 0.0.1
 */
public interface JpaService<T, ID extends Serializable> extends CrudService<T, ID>, PagingAndSortingService<T, ID> {

    long count(Specification<T> spec);

    void deleteAllInBatch();

    void deleteInBatch(Iterable<T> entities);

    List<T> findAll();

    List<T> findAll(Iterable<ID> ids);

    List<T> findAll(Specification<T> spec);

    Page<T> findAll(Specification<T> spec, Pageable pageable);

    List<T> findAll(Specification<T> spec, Sort sort);

    List<T> findAll(Sort sort);

    T findOne(Specification<T> spec);

    void flush();

    T getOne(ID id);

    <S extends T> List<S> save(Iterable<S> entities);

    <S extends T> S saveAndFlush(S entity);
}
