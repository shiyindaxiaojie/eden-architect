package org.ylzl.eden.extension.wrapper;

import org.ylzl.eden.extension.Wrapper;
import org.ylzl.eden.extension.active.ActivateComparator;

import java.util.Comparator;

/**
 * 针对 @Wrapper 排序
 *
 * @see Wrapper
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class WrapperComparator extends ActivateComparator {

	public static final Comparator<Object> COMPARATOR = new WrapperComparator();
}
