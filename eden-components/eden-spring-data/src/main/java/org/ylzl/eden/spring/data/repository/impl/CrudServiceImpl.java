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

import org.springframework.data.repository.CrudRepository;
import org.ylzl.eden.spring.data.repository.CrudService;

import java.io.Serializable;
import java.util.Optional;

/**
 * 增删查改业务实现
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
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

	@Deprecated
	@Override
	public void delete(ID id) {
		crudRepository.deleteById(id);
	}

	@Override
	public void delete(T entity) {
		crudRepository.delete(entity);
	}

	@Deprecated
	@Override
	public void delete(Iterable<? extends T> entities) {
		crudRepository.deleteAll(entities);
	}

	@Override
	public void deleteAll() {
		crudRepository.deleteAll();
	}

	@Override
	public void deleteAll(Iterable<? extends T> entities) {
		crudRepository.deleteAll(entities);
	}

	@Override
	public void deleteById(ID id) {
		crudRepository.deleteById(id);
	}

	@Override
	public boolean exists(ID id) {
		return crudRepository.existsById(id);
	}

	@Deprecated
	@Override
	public boolean existsById(ID id) {
		return crudRepository.existsById(id);
	}

	@Override
	public Iterable<T> findAll() {
		return crudRepository.findAll();
	}

	@Deprecated
	@Override
	public Iterable<T> findAll(Iterable<ID> ids) {
		return crudRepository.findAllById(ids);
	}

	@Override
	public Iterable<T> findAllById(Iterable<ID> ids) {
		return crudRepository.findAllById(ids);
	}

	@Deprecated
	@Override
	public Optional<T> findOne(ID id) {
		return crudRepository.findById(id);
	}

	@Override
	public Optional<T> findById(ID id) {
		return crudRepository.findById(id);
	}

	@Override
	public <S extends T> S save(S entity) {
		return crudRepository.save(entity);
	}

	@Deprecated
	@Override
	public <S extends T> Iterable<S> save(Iterable<S> entities) {
		return crudRepository.saveAll(entities);
	}

	@Override
	public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
		return crudRepository.saveAll(entities);
	}
}
