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

import org.springframework.data.repository.CrudRepository;
import org.ylzl.eden.spring.boot.support.service.CrudService;

import java.io.Serializable;

/**
 * 增删查改业务实现
 *
 * @author gyl
 * @since 1.0.0
 */
public class CrudServiceImpl<T, ID extends Serializable> implements CrudService<T, ID> {

  private final CrudRepository<T, ID> crudRepository;

  public CrudServiceImpl(CrudRepository<T, ID> crudRepository) {
    this.crudRepository = crudRepository;
  }

  @Override
  public long count() {
    return crudRepository.count();
  }

  @Override
  public void delete(ID id) {
    crudRepository.delete(id);
  }

  @Override
  public void delete(T entity) {
    crudRepository.delete(entity);
  }

  @Override
  public void delete(Iterable<? extends T> entities) {
    crudRepository.delete(entities);
  }

  @Override
  public void deleteAll() {
    crudRepository.deleteAll();
  }

  @Override
  public boolean exists(ID id) {
    return crudRepository.exists(id);
  }

  @Override
  public Iterable<T> findAll() {
    return crudRepository.findAll();
  }

  @Override
  public Iterable<T> findAll(Iterable<ID> ids) {
    return crudRepository.findAll(ids);
  }

  @Override
  public T findOne(ID id) {
    return crudRepository.findOne(id);
  }

  @Override
  public <S extends T> S save(S entity) {
    return crudRepository.save(entity);
  }

  @Override
  public <S extends T> Iterable<S> save(Iterable<S> entities) {
    return crudRepository.save(entities);
  }
}
