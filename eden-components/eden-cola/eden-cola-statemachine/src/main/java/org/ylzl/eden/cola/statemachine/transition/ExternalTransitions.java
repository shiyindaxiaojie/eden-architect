package org.ylzl.eden.cola.statemachine.transition;

import org.ylzl.eden.cola.statemachine.dsl.FromAmong;

/**
 * 外部流转（多个）
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@SuppressWarnings("unchecked")
public interface ExternalTransitions<S, E, C> {

	FromAmong<S, E, C> fromAmong(S... stateIds);
}
