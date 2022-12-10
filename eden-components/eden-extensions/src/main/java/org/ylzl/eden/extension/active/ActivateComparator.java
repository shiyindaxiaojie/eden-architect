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

package org.ylzl.eden.extension.active;

import org.ylzl.eden.extension.Activate;

import java.util.Comparator;

/**
 * 针对 @Activate 排序
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @see Activate#order()
 * @since 2.4.13
 */
public class ActivateComparator implements Comparator<Object> {

	public static final Comparator<Object> COMPARATOR = new ActivateComparator();

	@Override
	public int compare(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		if (o1.equals(o2)) {
			return 0;
		}

		return getOrder(o1.getClass()) > getOrder(o2.getClass()) ? 1 : -1;
	}

	private int getOrder(Class<?> clazz) {
		if (clazz.isAnnotationPresent(Activate.class)) {
			Activate activate = clazz.getAnnotation(Activate.class);
			return activate.order();
		}
		return 0;
	}
}
