package org.ylzl.eden.cola.statemachine.state;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.cola.statemachine.State;

import java.util.Map;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class StateHelper {

	public static <S, E, C> State<S, E, C> getState(Map<S, State<S, E, C>> stateMap, S stateId) {
		State<S, E, C> state = stateMap.get(stateId);
		if (state == null) {
			state = new StateImpl<>(stateId);
			stateMap.put(stateId, state);
		}
		return state;
	}
}
