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

package org.ylzl.eden.distributed.uid.integration.leaf.segement.dao;

import org.apache.ibatis.annotations.*;
import org.ylzl.eden.distributed.uid.integration.leaf.segement.model.LeafAlloc;

import java.util.List;

/**
 * LeafAlloc 表操作
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Mapper
public interface LeafAllocMapper {

	@Select("SELECT biz_tag, max_id, step, update_time FROM leaf_alloc")
	@Results(value = {
		@Result(column = "biz_tag", property = "key"),
		@Result(column = "max_id", property = "maxId"),
		@Result(column = "step", property = "step"),
		@Result(column = "update_time", property = "updateTime")
	})
	List<LeafAlloc> getAllLeafAllocs();

	@Select("SELECT biz_tag, max_id, step FROM leaf_alloc WHERE biz_tag = #{tag}")
	@Results(value = {
		@Result(column = "biz_tag", property = "key"),
		@Result(column = "max_id", property = "maxId"),
		@Result(column = "step", property = "step")
	})
	LeafAlloc getLeafAlloc(@Param("tag") String tag);

	@Update("UPDATE leaf_alloc SET max_id = max_id + step WHERE biz_tag = #{tag}")
	void updateMaxId(@Param("tag") String tag);

	@Update("UPDATE leaf_alloc SET max_id = max_id + #{step} WHERE biz_tag = #{key}")
	void updateMaxIdByCustomStep(@Param("leafAlloc") LeafAlloc leafAlloc);

	@Select("SELECT biz_tag FROM leaf_alloc")
	List<String> getAllTags();
}
