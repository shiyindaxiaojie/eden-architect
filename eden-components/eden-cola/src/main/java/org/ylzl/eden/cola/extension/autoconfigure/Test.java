package org.ylzl.eden.cola.extension.autoconfigure;

import org.ylzl.eden.cola.extension.BizScenario;
import org.ylzl.eden.cola.extension.executor.ExtensionExecutor;
import org.ylzl.eden.cola.extension.register.ExtensionRegister;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class Test {

	public static void main(String[] args) {
		ExtensionRegister register = new ExtensionRegister();
		register.registerExtension(new ExtA());
		register.registerExtension(new ExtB());

		ExtensionExecutor executor = new ExtensionExecutor(register);
		executor.execute(Ext.class, BizScenario.valueOf("A"), Ext::method);
		executor.execute(Ext.class, BizScenario.valueOf("B"), Ext::method);
	}
}
