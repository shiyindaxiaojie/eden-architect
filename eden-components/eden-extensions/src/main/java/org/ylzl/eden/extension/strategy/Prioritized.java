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

package org.ylzl.eden.extension.strategy;

import java.util.Comparator;

/**
 * 权重
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface Prioritized extends Comparable<Prioritized> {

	int MAX_PRIORITY = Integer.MIN_VALUE;

	int MIN_PRIORITY = Integer.MAX_VALUE;

	int NORMAL_PRIORITY = 0;

	Comparator<Object> COMPARATOR = (one, two) -> {
		boolean b1 = one instanceof Prioritized;
		boolean b2 = two instanceof Prioritized;
		if (b1 && !b2) {
			return -1;
		}
		if (b2 && !b1) {
			return 1;
		}
		if (b1) {
			return ((Prioritized) one).compareTo((Prioritized) two);
		}
		return 0;
	};

	default int getPriority() {
		return NORMAL_PRIORITY;
	}

	@Override
	default int compareTo(Prioritized that) {
		return Integer.compare(this.getPriority(), that.getPriority());
	}
}
