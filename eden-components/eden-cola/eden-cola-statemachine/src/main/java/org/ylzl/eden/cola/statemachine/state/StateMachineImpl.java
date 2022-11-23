package org.ylzl.eden.cola.statemachine.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.cola.statemachine.State;
import org.ylzl.eden.cola.statemachine.StateMachine;
import org.ylzl.eden.cola.statemachine.Transition;
import org.ylzl.eden.cola.statemachine.callback.Callback;
import org.ylzl.eden.cola.statemachine.visitor.Visitor;
import org.ylzl.eden.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class StateMachineImpl<S, E, C> implements StateMachine<S, E, C> {

	public static final String TRANSITION_NOT_FOUND = "There is no transition for {}";

	private String machineId;

	private final Map<S, State<S, E, C>> stateMap;

	private final Callback<S, E, C> callback;

	@Override
	public String getMachineId() {
		return machineId;
	}

	@Override
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	@Override
	public boolean verify(S sourceState, E event) {
		State<S, E, C> state = StateHelper.getState(stateMap, sourceState);
		List<Transition<S, E, C>> transitions = state.getEventTransitions(event);
		return CollectionUtils.isNotEmpty(transitions);
	}

	@Override
	public S fireEvent(S sourceState, E event, C ctx) {
		Transition<S, E, C> transition = routeTransition(sourceState, event, ctx);
		if (transition == null) {
			log.debug(TRANSITION_NOT_FOUND, event);
			callback.onFail(sourceState, event, ctx);
			return sourceState;
		}

		callback.onSuccess(sourceState, event, ctx);
		return transition.transit(ctx, false).getId();
	}

	@Override
	public String accept(Visitor visitor) {
		StringBuilder sb = new StringBuilder();
		sb.append(visitor.visitOnEntry(this));
		for (State<S, E, C> state : stateMap.values()) {
			sb.append(state.accept(visitor));
		}
		sb.append(visitor.visitOnExit(this));
		return sb.toString();
	}

	private Transition<S, E, C> routeTransition(S sourceState, E event, C ctx) {
		State<S, E, C> state = StateHelper.getState(stateMap, sourceState);
		List<Transition<S, E, C>> transitions = state.getEventTransitions(event);
		if (transitions == null || transitions.size() == 0) {
			return null;
		}

		Transition<S, E, C> transit = null;
		for (Transition<S, E, C> transition : transitions) {
			if (transition.getCondition() == null) {
				transit = transition;
			} else if (transition.getCondition().isSatisfied(ctx)) {
				transit = transition;
				break;
			}
		}
		return transit;
	}
}
