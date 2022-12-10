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

package org.ylzl.eden.cola.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 响应（分页查询结果集）
 *
 * @author Frank Zhang
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PageResponse<T> extends Response {

	private static final long serialVersionUID = 1L;

	private int totalCount = 0;

	private int pageSize = 1;

	private int pageIndex = 1;

	private Collection<T> data;

	public void setPageSize(int pageSize) {
		this.pageSize = Math.max(pageSize, 1);
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = Math.max(pageIndex, 1);
	}

	public Collection<T> getData() {
		if (null == data) {
			return Collections.emptyList();
		}
		if (data instanceof List) {
			return data;
		}
		return new ArrayList<>(data);
	}

	public void setData(Collection<T> data) {
		this.data = null == data ? Collections.emptyList() : new ArrayList<>(data);
	}

	public int getTotalPages() {
		return this.totalCount % this.pageSize == 0 ? this.totalCount / this.pageSize : (this.totalCount / this.pageSize) + 1;
	}

	public boolean isEmpty() {
		return data == null || data.isEmpty();
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}

	public static <T> PageResponse<T> of(int pageSize, int pageIndex) {
		PageResponse<T> response = new PageResponse<>();
		response.setSuccess(true);
		response.setData(Collections.emptyList());
		response.setTotalCount(0);
		response.setPageSize(pageSize);
		response.setPageIndex(pageIndex);
		return response;
	}

	public static <T> PageResponse<T> of(Collection<T> data, int totalCount, int pageSize, int pageIndex) {
		PageResponse<T> response = new PageResponse<>();
		response.setSuccess(true);
		response.setData(data);
		response.setTotalCount(totalCount);
		response.setPageSize(pageSize);
		response.setPageIndex(pageIndex);
		return response;
	}
}
