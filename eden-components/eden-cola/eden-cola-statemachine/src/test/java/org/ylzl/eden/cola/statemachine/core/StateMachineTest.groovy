package org.ylzl.eden.cola.statemachine.core

import org.ylzl.eden.cola.statemachine.factory.StateMachineFactory
import spock.lang.*

class StateMachineTest extends Specification {

	enum States {
		WAIT_BUYER_PAY, // 创建交易，等待买家付款 -> 待支付
		TRADE_SUCCESS, // 交易支付成功 -> 已支付
		TRADE_FINISHED, // 交易结束，不可退款 -> 已完成
		TRADE_CLOSED, // 未付款交易超时关闭 -> 已取消
		TRADE_REFUND; // 交易退款中 -> 已退款
	}

	enum Events {
		ORDER_CREATED,
		PAY_SUCCESS,
		PAY_FAILED
	}

	class Context {
		String customer = "sion"
		String item = "book"
		boolean passed = true
	}

	def "test external transition"() {
		given:
		StateMachine<States, Events, Context> stateMachine = StateMachineFactory.create("external transition")
		stateMachine.externalTransition()
			.from(States.WAIT_BUYER_PAY)
			.to(States.TRADE_SUCCESS)
			.on(Events.PAY_SUCCESS)
			.when((context) -> {
				return context.passed;
			})
			.perform((from, to, event, ctx) -> {
				System.out.println(
					ctx.customer + " buy " + ctx.item + " from " + from + " to " + to + " on " + event);
			})

		when:
		States target = stateMachine.fireEvent(States.WAIT_BUYER_PAY, Events.PAY_SUCCESS, new Context())

		then:
		target == States.TRADE_SUCCESS
	}
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
