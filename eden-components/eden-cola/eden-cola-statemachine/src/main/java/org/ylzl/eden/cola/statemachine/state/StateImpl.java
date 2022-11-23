package org.ylzl.eden.cola.statemachine.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.cola.statemachine.State;
import org.ylzl.eden.cola.statemachine.Transition;
import org.ylzl.eden.cola.statemachine.transition.EventTransitions;
import org.ylzl.eden.cola.statemachine.transition.TransitionImpl;
import org.ylzl.eden.cola.statemachine.transition.TransitionType;
import org.ylzl.eden.cola.statemachine.visitor.Visitor;

import java.util.Collection;
import java.util.List;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class StateImpl<S, E, C> implements State<S, E, C> {

	private static final String ADD_NEW_TRANSITION = "Add new transition '{}'";

	private final EventTransitions<S, E, C> eventTransitions = new EventTransitions<>();

	private final S stateId;

	@Override
	public S getId() {
		return stateId;
	}

	@Override
	public String accept(Visitor visitor) {
		String entry = visitor.visitOnEntry(this);
		String exit = visitor.visitOnExit(this);
		return entry + exit;
	}

	@Override
	public Transition<S, E, C> addTransition(E event, State<S, E, C> target, TransitionType transitionType) {
		Transition<S, E, C> newTransition = new TransitionImpl<>();
		newTransition.setSource(this);
		newTransition.setTarget(target);
		newTransition.setEvent(event);
		newTransition.setType(transitionType);

		log.debug(ADD_NEW_TRANSITION, newTransition);
		eventTransitions.put(event, newTransition);
		return newTransition;
	}

	@Override
	public List<Transition<S, E, C>> getEventTransitions(E event) {
		return eventTransitions.get(event);
	}

	@Override
	public Collection<Transition<S, E, C>> getAllTransitions() {
		return eventTransitions.allTransitions();
	}
}
