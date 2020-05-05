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

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.io.Serializable;

/**
 * Elasticsearch 业务接口
 *
 * @author gyl
 * @since 1.0.0
 */
public interface ElasticsearchService<T, ID extends Serializable>
    extends PagingAndSortingService<T, ID> {

  <S extends T> S index(S entity);

  Iterable<T> search(QueryBuilder queryBuilder);

  Page<T> search(QueryBuilder queryBuilder, Pageable pageable);

  Page<T> search(SearchQuery searchQuery);

  Page<T> searchSimilar(T entity, String[] var, Pageable pageable);

  void refresh();
}
