package org.ylzl.eden.cola.extension;

/**
 * 业务场景
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class BizScenario {

	public final static String DEFAULT_BIZ_ID = "#defaultBizId#";

	public final static String DEFAULT_USE_CASE = "#defaultUseCase#";

	public final static String DEFAULT_SCENARIO = "#defaultScenario#";

	private final static String DOT_SEPARATOR = ".";

	private String bizId = DEFAULT_BIZ_ID;

	private String useCase = DEFAULT_USE_CASE;

	private String scenario = DEFAULT_SCENARIO;

	public static BizScenario valueOf(String bizId, String useCase, String scenario) {
		BizScenario bizScenario = new BizScenario();
		bizScenario.bizId = bizId;
		bizScenario.useCase = useCase;
		bizScenario.scenario = scenario;
		return bizScenario;
	}

	public static BizScenario valueOf(String bizId, String useCase) {
		return BizScenario.valueOf(bizId, useCase, DEFAULT_SCENARIO);
	}

	public static BizScenario valueOf(String bizId) {
		return BizScenario.valueOf(bizId, DEFAULT_USE_CASE, DEFAULT_SCENARIO);
	}

	public String getUniqueIdentity() {
		return bizId + DOT_SEPARATOR + useCase + DOT_SEPARATOR + scenario;
	}

	public String getIdentityWithDefaultUseCase() {
		return bizId + DOT_SEPARATOR + DEFAULT_USE_CASE + DOT_SEPARATOR + DEFAULT_SCENARIO;
	}

	public String getIdentityWithDefaultScenario() {
		return bizId + DOT_SEPARATOR + useCase + DOT_SEPARATOR + DEFAULT_SCENARIO;
	}
}
