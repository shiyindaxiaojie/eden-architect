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
import org.springframework.data.neo4j.repository.GraphRepository;
import org.ylzl.eden.spring.boot.support.service.GraphService;

/**
 * 图形数据库业务实现
 *
 * @author gyl
 * @since 0.0.1
 */
@SuppressWarnings("unchecked")
public class GraphServiceImpl<T> extends PagingAndSortingServiceImpl<T, Long> implements GraphService<T> {

    private final GraphRepository<T> graphRepository;

    public GraphServiceImpl(GraphRepository<T> graphRepository) {
        super(graphRepository);
        this.graphRepository = graphRepository;
    }

    @Override
    public <S extends T> T save(S entity, int depth) {
        return graphRepository.save(entity, depth);
    }

    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities, int depth) {
        return graphRepository.save(entities, depth);
    }

    @Override
    public T findOne(Long id, int depth) {
        return graphRepository.findOne(id, depth);
    }

    @Override
    public Iterable<T> findAll(int depth) {
        return graphRepository.findAll(depth);
    }

    @Override
    public Iterable<T> findAll(Sort sort, int depth) {
        return graphRepository.findAll(sort, depth);
    }

    @Override
    public Iterable<T> findAll(Iterable ids) {
        return graphRepository.findAll(ids);
    }

    @Override
    public Iterable<T> findAll(Iterable ids, int depth) {
        return graphRepository.findAll(ids, depth);
    }

    @Override
    public Iterable<T> findAll(Iterable ids, Sort sort) {
        return graphRepository.findAll(ids, sort);
    }

    @Override
    public Iterable<T> findAll(Iterable ids, Sort sort, int depth) {
        return graphRepository.findAll(ids, sort, depth);
    }

    @Override
    public Page<T> findAll(Pageable pageable, int depth) {
        return graphRepository.findAll(pageable, depth);
    }
}
