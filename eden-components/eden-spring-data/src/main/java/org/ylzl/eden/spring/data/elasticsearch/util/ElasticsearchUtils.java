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

package org.ylzl.eden.template.elasticsearch;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.ylzl.eden.template.mybatis.SortRuleEnum;

/**
 * Mybatis 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class ElasticsearchUtils {

	/**
	 * 获取索引名称
	 *
	 * @param clazz domain
	 * @return
	 */
	public static String getIndex(Class<?> clazz) {
		TableName tableName = clazz.getAnnotation(TableName.class);
		return tableName.value();
	}

	/**
	 * 排序
	 *
	 * @param queryBuilder
	 * @param sortColumn
	 * @param sortRule
	 */
	public static void sort(
		NativeSearchQueryBuilder queryBuilder, String sortColumn, String sortRule) {
		if (StringUtils.isBlank(sortRule) || StringUtils.isBlank(sortRule)) {
			return;
		}
		queryBuilder.withSort(
			SortBuilders.fieldSort(sortColumn)
				.order(
					SortRuleEnum.DESC.name().equalsIgnoreCase(sortRule)
						? SortOrder.DESC
						: SortOrder.ASC));
	}

	/**
	 * 分页
	 *
	 * @param queryBuilder
	 * @param pageNum
	 * @param pageSize
	 */
	public static Pageable pageable(
		NativeSearchQueryBuilder queryBuilder, @NonNull Long pageNum, @NonNull Long pageSize) {
		Pageable pageable =
			PageRequest.of(pageNum.intValue() <= 0 ? 0 : pageNum.intValue() - 1, pageSize.intValue());
		queryBuilder.withPageable(pageable);
		return pageable;
	}

	/**
	 * 设置返回的字段
	 *
	 * @param queryBuilder
	 * @param includes
	 * @param excludes
	 */
	public static void sourceFilter(
		NativeSearchQueryBuilder queryBuilder, String[] includes, String[] excludes) {
		queryBuilder.withSourceFilter(new FetchSourceFilter(includes, excludes));
	}

	/**
	 * 设置返回的字段
	 *
	 * @param queryBuilder
	 * @param includes
	 */
	public static void sourceFilter(NativeSearchQueryBuilder queryBuilder, String... includes) {
		sourceFilter(queryBuilder, includes, null);
	}
}
