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

package org.ylzl.eden.dynamic.cache.integration.hotkey.jd;

import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import org.ylzl.eden.dynamic.cache.hotkey.HotKey;

/**
 * 京东热key探测
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class JdHotKey implements HotKey {

	/**
	 * 判断是否为热Key
	 *
	 * @param key Key
	 * @return 是否为热Key
	 */
	@Override
	public <K> boolean isHotKey(K key) {
		return JdHotKeyStore.isHotKey(key.toString());
	}
}
