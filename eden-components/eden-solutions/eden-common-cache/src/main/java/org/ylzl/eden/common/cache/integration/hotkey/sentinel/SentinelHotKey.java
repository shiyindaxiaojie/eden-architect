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

package org.ylzl.eden.common.cache.integration.hotkey.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.jetbrains.annotations.NotNull;
import org.ylzl.eden.common.cache.hotkey.HotKey;

/**
 * Sentinel热key探测
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class SentinelHotKey implements HotKey {

	/**
	 * 判断是否为热Key
	 *
	 * @param name 资源名称
	 * @param key  Key
	 * @return 是否为热Key
	 */
	@Override
	public boolean isHotKey(@NotNull String name, @NotNull Object key) {
		try (Entry ignored = SphU.entry(name, EntryType.IN, 1, key)) {
			return false;
		} catch (BlockException ignored) {
			return true;
		}
	}
}
