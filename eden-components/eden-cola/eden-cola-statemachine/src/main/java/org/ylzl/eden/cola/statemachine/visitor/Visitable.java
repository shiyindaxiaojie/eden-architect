package org.ylzl.eden.cola.statemachine.visitor;

/**
 * 访问接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface Visitable {

	String accept(final Visitor visitor);
}
