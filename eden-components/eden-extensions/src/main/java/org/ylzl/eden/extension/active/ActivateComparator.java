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
