package org.ylzl.eden.cola.statemachine;

/**
 * 访问器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface Visitor {

	char LF = '\n';

	String visitOnEntry(StateMachine<?, ?, ?> visitable);

	String visitOnExit(StateMachine<?, ?, ?> visitable);

	String visitOnEntry(State<?, ?, ?> visitable);

	String visitOnExit(State<?, ?, ?> visitable);
}
