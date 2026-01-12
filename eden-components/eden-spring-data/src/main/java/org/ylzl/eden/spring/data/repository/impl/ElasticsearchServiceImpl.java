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
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.support.PageableExecutionUtils;
import org.ylzl.eden.spring.data.elasticsearch.repository.ElasticsearchRepository;
import org.ylzl.eden.spring.data.repository.ElasticsearchService;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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

	private final ElasticsearchOperations elasticsearchOperations;

	private final Class<T> entityClass;

	public ElasticsearchServiceImpl(ElasticsearchRepository<T, ID> elasticsearchRepository,
									ElasticsearchOperations elasticsearchOperations,
									Class<T> entityClass) {
		super(elasticsearchRepository);
		this.elasticsearchRepository = elasticsearchRepository;
		this.elasticsearchOperations = elasticsearchOperations;
		this.entityClass = entityClass;
	}

	@Override
	public <S extends T> S index(S entity) {
		return elasticsearchRepository.save(entity);
	}

	@Override
	public Iterable<T> search(QueryBuilder queryBuilder) {
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
			.withQuery(queryBuilder)
			.build();
		SearchHits<T> searchHits = elasticsearchOperations.search(searchQuery, entityClass);
		return searchHits.getSearchHits().stream()
			.map(SearchHit::getContent)
			.collect(Collectors.toList());
	}

	@Override
	public Page<T> search(QueryBuilder queryBuilder, Pageable pageable) {
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
			.withQuery(queryBuilder)
			.withPageable(pageable)
			.build();
		SearchHits<T> searchHits = elasticsearchOperations.search(searchQuery, entityClass);
		List<T> content = searchHits.getSearchHits().stream()
			.map(SearchHit::getContent)
			.collect(Collectors.toList());
		return PageableExecutionUtils.getPage(content, pageable, searchHits::getTotalHits);
	}

	@Override
	public Page<T> searchSimilar(T entity, String[] var, Pageable pageable) {
		return elasticsearchRepository.searchSimilar(entity, var, pageable);
	}

	@Override
	public void refresh() {
		elasticsearchOperations.indexOps(entityClass).refresh();
	}
}
