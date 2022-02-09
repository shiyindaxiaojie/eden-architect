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

package org.ylzl.eden.spring.framework.web.cookie;

import javax.servlet.http.Cookie;
import java.util.*;

/**
 * Cookie 集合
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class CookieCollection implements Collection<Cookie> {

	private final Map<String, Cookie> cookieMap;

	public CookieCollection() {
		cookieMap = new HashMap<>();
	}

	public CookieCollection(Cookie... cookies) {
		this(Arrays.asList(cookies));
	}

	public CookieCollection(Collection<? extends Cookie> cookies) {
		cookieMap = new HashMap<>(cookies.size());
		addAll(cookies);
	}

	@Override
	public int size() {
		return cookieMap.size();
	}

	@Override
	public boolean isEmpty() {
		return cookieMap.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof String) {
			return cookieMap.containsKey(o);
		}
		if (o instanceof Cookie) {
			return cookieMap.containsValue(o);
		}
		return false;
	}

	@Override
	public Iterator<Cookie> iterator() {
		return cookieMap.values().iterator();
	}

	public Cookie[] toArray() {
		Cookie[] cookies = new Cookie[cookieMap.size()];
		return toArray(cookies);
	}

	@Override
	public <T> T[] toArray(T[] ts) {
		return cookieMap.values().toArray(ts);
	}

	@Override
	public boolean add(Cookie cookie) {
		if (cookie == null) {
			return false;
		}
		cookieMap.put(cookie.getName(), cookie);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		if (o instanceof String) {
			return cookieMap.remove((String) o) != null;
		}
		if (o instanceof Cookie) {
			Cookie c = (Cookie) o;
			return cookieMap.remove(c.getName()) != null;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		for (Object o : collection) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends Cookie> collection) {
		boolean result = false;
		for (Cookie cookie : collection) {
			result |= add(cookie);
		}
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean result = false;
		for (Object cookie : collection) {
			result |= remove(cookie);
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		boolean result = false;
		Iterator<Map.Entry<String, Cookie>> it = cookieMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Cookie> e = it.next();
			if (!collection.contains(e.getKey()) && !collection.contains(e.getValue())) {
				it.remove();
				result = true;
			}
		}
		return result;
	}

	@Override
	public void clear() {
		cookieMap.clear();
	}

	public Cookie get(String name) {
		return cookieMap.get(name);
	}
}
