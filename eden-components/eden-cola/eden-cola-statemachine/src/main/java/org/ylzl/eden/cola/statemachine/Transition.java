package org.ylzl.eden.cola.statemachine;

import org.ylzl.eden.cola.statemachine.transition.TransitionType;

/**
 * 从一个状态到另一个状态的流转
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface Transition<S, E, C> {

	State<S,E,C> getSource();

	void setSource(State<S, E, C> state);

	State<S,E,C> getTarget();

	void setTarget(State<S, E, C> target);

	E getEvent();

	void setEvent(E event);

	void setType(TransitionType type);

	Condition<C> getCondition();

	void setCondition(Condition<C> condition);

	Action<S,E,C> getAction();

	void setAction(Action<S, E, C> action);

	State<S, E, C> transit(C ctx, boolean checkCondition);

	void verify();
}
