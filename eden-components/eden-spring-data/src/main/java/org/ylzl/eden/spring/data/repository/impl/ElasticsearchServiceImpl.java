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

package org.ylzl.eden.spring.data.repository.impl;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ylzl.eden.spring.data.elasticsearch.repository.ElasticsearchRepository;
import org.ylzl.eden.spring.data.repository.ElasticsearchService;

import java.io.Serializable;

/**
 * Elasticsearch 业务实现
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class ElasticsearchServiceImpl<T, ID extends Serializable>
	extends org.ylzl.eden.spring.data.repository.impl.PagingAndSortingServiceImpl<T, ID>
	implements ElasticsearchService<T, ID> {

	private final ElasticsearchRepository<T, ID> elasticsearchRepository;

	public ElasticsearchServiceImpl(ElasticsearchRepository<T, ID> elasticsearchRepository) {
		super(elasticsearchRepository);
		this.elasticsearchRepository = elasticsearchRepository;
	}

	@Override
	public <S extends T> S index(S entity) {
		return elasticsearchRepository.index(entity);
	}

	@Override
	public Iterable<T> search(QueryBuilder queryBuilder) {
		return elasticsearchRepository.search(queryBuilder);
	}

	@Override
	public Page<T> search(QueryBuilder queryBuilder, Pageable pageable) {
		return elasticsearchRepository.search(queryBuilder, pageable);
	}

	@Override
	public Page<T> searchSimilar(T entity, String[] var, Pageable pageable) {
		return elasticsearchRepository.searchSimilar(entity, var, pageable);
	}

	@Override
	public void refresh() {
		elasticsearchRepository.refresh();
	}
}
