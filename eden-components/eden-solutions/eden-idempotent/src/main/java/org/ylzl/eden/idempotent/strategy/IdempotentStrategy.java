package org.ylzl.eden.idempotent.strategy;

/**
 * 幂等性处理策略
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public enum IdempotentStrategy {

	EXPIRE,
	TOKEN;

	public static IdempotentStrategy parse(String type) {
		for (IdempotentStrategy strategy : IdempotentStrategy.values()) {
			if (strategy.name().equalsIgnoreCase(type)) {
				return strategy;
			}
		}
		return null;
	}
}
