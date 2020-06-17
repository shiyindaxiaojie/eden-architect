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

package org.ylzl.eden.spring.boot.support.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.ylzl.eden.spring.boot.data.jpa.repository.JpaRepository;
import org.ylzl.eden.spring.boot.support.service.JpaService;

import java.io.Serializable;
import java.util.List;

/**
 * 关系数据库业务实现
 *
 * @author gyl
 * @since 1.0.0
 */
public class JpaServiceImpl<T, ID extends Serializable> extends PagingAndSortingServiceImpl<T, ID>
    implements JpaService<T, ID> {

  private final JpaRepository<T, ID> jpaRepository;

  public JpaServiceImpl(JpaRepository<T, ID> jpaRepository) {
    super(jpaRepository);
    this.jpaRepository = jpaRepository;
  }

  @Override
  public long count(Specification<T> spec) {
    return jpaRepository.count(spec);
  }

  @Override
  public void deleteAllInBatch() {
    jpaRepository.deleteAllInBatch();
  }

  @Override
  public void deleteInBatch(Iterable<T> entities) {
    jpaRepository.deleteInBatch(entities);
  }

  @Override
  public List<T> findAll() {
    return jpaRepository.findAll();
  }

  @Override
  public List<T> findAll(Iterable<ID> ids) {
    return jpaRepository.findAll(ids);
  }

  @Override
  public List<T> findAll(Specification<T> spec) {
    return jpaRepository.findAll(spec);
  }

  @Override
  public Page<T> findAll(Specification<T> spec, Pageable pageable) {
    return jpaRepository.findAll(spec, pageable);
  }

  @Override
  public List<T> findAll(Specification<T> spec, Sort sort) {
    return jpaRepository.findAll(spec, sort);
  }

  @Override
  public List<T> findAll(Sort sort) {
    return jpaRepository.findAll(sort);
  }

  @Override
  public T findOne(Specification<T> spec) {
    return jpaRepository.findOne(spec);
  }

  @Override
  public void flush() {
    jpaRepository.flush();
  }

  @Override
  public T getOne(ID id) {
    return jpaRepository.getOne(id);
  }

  @Override
  public <S extends T> List<S> save(Iterable<S> entities) {
    return jpaRepository.save(entities);
  }

  @Override
  public <S extends T> S saveAndFlush(S entity) {
    return jpaRepository.saveAndFlush(entity);
  }
}
