package org.ylzl.eden.cola.extension.autoconfigure;

import org.ylzl.eden.cola.extension.Extension;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Extension(bizId = "A")
public class ExtA implements Ext {

	@Override
	public void method() {
		System.out.println("AAAAA");
	}
}
