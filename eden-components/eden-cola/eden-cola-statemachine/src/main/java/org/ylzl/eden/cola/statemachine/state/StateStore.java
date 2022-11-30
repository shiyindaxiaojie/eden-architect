package org.ylzl.eden.cola.statemachine.state;

import lombok.Getter;
import org.ylzl.eden.cola.statemachine.State;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class StateStore<S, E, C> {

	@Getter
	private final Map<S, State<S, E, C>> stateMap = new ConcurrentHashMap<>();

	public State<S, E, C> getState(S stateId) {
		State<S, E, C> state = stateMap.get(stateId);
		if (state == null) {
			state = new StateImpl<>(stateId);
			stateMap.putIfAbsent(stateId, state);
		}
		return state;
	}
}
