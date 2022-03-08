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

package org.ylzl.eden.spring.data.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ylzl.eden.spring.data.repository.PagingAndSortingService;

import java.io.Serializable;

/**
 * 查询分页排序业务实现
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class PagingAndSortingServiceImpl<T, ID extends Serializable> extends CrudServiceImpl<T, ID>
	implements PagingAndSortingService<T, ID> {

	private final PagingAndSortingRepository<T, ID> pagingAndSortingRepository;

	public PagingAndSortingServiceImpl(PagingAndSortingRepository<T, ID> pagingAndSortingRepository) {
		super(pagingAndSortingRepository);
		this.pagingAndSortingRepository = pagingAndSortingRepository;
	}

	@Override
	public Iterable<T> findAll(Sort sort) {
		return pagingAndSortingRepository.findAll(sort);
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		return pagingAndSortingRepository.findAll(pageable);
	}
}
