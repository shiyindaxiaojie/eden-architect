package org.ylzl.eden.cola.statemachine.state;

import org.ylzl.eden.cola.statemachine.transition.Transition;
import org.ylzl.eden.cola.statemachine.transition.TransitionType;
import org.ylzl.eden.cola.statemachine.visitor.Visitable;

import java.util.Collection;
import java.util.List;

/**
 * 状态
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface State<S, E, C> extends Visitable {

	S getId();

	Transition<S, E, C> addTransition(E event, State<S, E, C> target, TransitionType transitionType);

	List<Transition<S, E, C>> getEventTransitions(E event);

	Collection<Transition<S, E, C>> getAllTransitions();
}
