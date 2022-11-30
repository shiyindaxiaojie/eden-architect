package org.ylzl.eden.cola.statemachine.transition;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.ylzl.eden.cola.statemachine.exception.StateMachineException;

import java.util.List;
import java.util.Map;

/**
 * 同一个 Event 可以触发多个 Transitions
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class EventTransitions<S, E, C> {

	private final Map<E, List<Transition<S, E, C>>> eventTransitions = Maps.newHashMap();

	public List<Transition<S, E, C>> get(E event) {
		return eventTransitions.get(event);
	}

	public void put(E event, Transition<S, E, C> transition) {
		if (eventTransitions.get(event) == null) {
			List<Transition<S, E, C>> transitions = Lists.newArrayList();
			transitions.add(transition);
			eventTransitions.put(event, transitions);
		} else {
			List<Transition<S, E, C>> existingTransitions = eventTransitions.get(event);
			verify(existingTransitions, transition);
			existingTransitions.add(transition);
		}
	}

	public List<Transition<S, E, C>> allTransitions() {
		List<Transition<S, E, C>> allTransitions = Lists.newArrayList();
		for (List<Transition<S, E, C>> transitions : eventTransitions.values()) {
			allTransitions.addAll(transitions);
		}
		return allTransitions;
	}

	private void verify(List<Transition<S, E, C>> existingTransitions, Transition<S, E, C> newTransition) {
		for (Transition<S, E, C> transition : existingTransitions) {
			if (transition.equals(newTransition)) {
				throw new StateMachineException(transition + " already exist, you can not add another one");
			}
		}
	}
}
