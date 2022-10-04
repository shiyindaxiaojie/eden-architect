package org.ylzl.eden.spring.framework.extension.loader;

import java.util.Comparator;

/**
 * 权重
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
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
		if (b1 && b2) {
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
