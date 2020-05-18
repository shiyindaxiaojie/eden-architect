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
import org.ylzl.eden.spring.boot.data.neo4j.repository.Neo4jRepository;
import org.ylzl.eden.spring.boot.support.service.Neo4jService;

import java.io.Serializable;
import java.util.Optional;

/**
 * Neo4j 业务实现
 *
 * @author gyl
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class Neo4jServiceImpl<T, ID extends Serializable> extends PagingAndSortingServiceImpl<T, ID>
    implements Neo4jService<T, ID> {

  private final Neo4jRepository<T, ID> neo4jRepository;

  public Neo4jServiceImpl(Neo4jRepository<T, ID> neo4jRepository) {
    super(neo4jRepository);
    this.neo4jRepository = neo4jRepository;
  }

  @Override
  public <S extends T> T save(S entity, int depth) {
    return neo4jRepository.save(entity, depth);
  }

  @Override
  public <S extends T> Iterable<S> save(Iterable<S> entities, int depth) {
    return neo4jRepository.save(entities, depth);
  }

  @Deprecated
  @Override
  public Optional<T> findOne(ID id, int depth) {
    return neo4jRepository.findById(id, depth);
  }

  @Override
  public Optional<T> findById(ID id, int depth) {
    return neo4jRepository.findById(id, depth);
  }

  @Override
  public Iterable<T> findAll(int depth) {
    return neo4jRepository.findAll(depth);
  }

  @Override
  public Iterable<T> findAll(Sort sort, int depth) {
    return neo4jRepository.findAll(sort, depth);
  }

  @Deprecated
  @Override
  public Iterable<T> findAll(Iterable<ID> ids, int depth) {
    return neo4jRepository.findAllById(ids, depth);
  }

  @Deprecated
  @Override
  public Iterable<T> findAll(Iterable<ID> ids, Sort sort) {
    return neo4jRepository.findAllById(ids, sort);
  }

  @Deprecated
  @Override
  public Iterable<T> findAll(Iterable<ID> ids, Sort sort, int depth) {
    return neo4jRepository.findAllById(ids, sort, depth);
  }

  @Override
  public Iterable<T> findAllById(Iterable<ID> ids, int depth) {
    return neo4jRepository.findAllById(ids, depth);
  }

  @Override
  public Iterable<T> findAllById(Iterable<ID> ids, Sort sort) {
    return neo4jRepository.findAllById(ids, sort);
  }

  @Override
  public Iterable<T> findAllById(Iterable<ID> ids, Sort sort, int depth) {
    return neo4jRepository.findAllById(ids, sort, depth);
  }

  @Override
  public Page<T> findAll(Pageable pageable, int depth) {
    return neo4jRepository.findAll(pageable, depth);
  }
}
