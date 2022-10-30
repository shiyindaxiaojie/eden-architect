package org.ylzl.eden.spring.framework.extension.wrapper;

import org.ylzl.eden.spring.framework.extension.active.ActivateComparator;

import java.util.Comparator;

/**
 * 针对 @Wrapper 排序
 *
 * @see org.ylzl.eden.spring.framework.extension.Wrapper
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class WrapperComparator extends ActivateComparator {

	public static final Comparator<Object> COMPARATOR = new WrapperComparator();
}
