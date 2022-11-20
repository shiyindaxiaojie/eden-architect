package org.ylzl.eden.cola.extension.autoconfigure;

import org.ylzl.eden.cola.extension.Extension;
import org.ylzl.eden.cola.extension.ExtensionPoint;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Extension(bizId = "B")
public class ExtB implements Ext, ExtensionPoint {

	@Override
	public void method() {
		System.out.println("BBBBB");
	}
}
