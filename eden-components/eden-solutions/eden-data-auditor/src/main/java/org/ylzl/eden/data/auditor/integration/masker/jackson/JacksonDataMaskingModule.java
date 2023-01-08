/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.data.auditor.integration.masker.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.Module;
import org.ylzl.eden.spring.framework.bootstrap.constant.Globals;
import org.ylzl.eden.spring.framework.json.jackson.Jackson;
import org.ylzl.eden.spring.framework.json.support.JSONHelper;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Jackson 数据脱敏模块
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class JacksonDataMaskingModule extends Module {

	private static final String MODULE_NAME = "data-masker";

	private static final Version VERSION = VersionUtil.parseVersion(Globals.VERSION, Globals.GROUP_ID, MODULE_NAME);

	private static final AtomicBoolean REGISTER_STATE = new AtomicBoolean(false);

	public static void register() {
		if (!REGISTER_STATE.compareAndSet(false, true)) {
			return;
		}
		Jackson jackson = (Jackson) JSONHelper.json();
		jackson.getObjectMapper().registerModule(new JacksonDataMaskingModule());
	}

	@Override
	public String getModuleName() {
		return MODULE_NAME;
	}

	@Override
	public Version version() {
		return VERSION;
	}

	@Override
	public void setupModule(SetupContext setupContext) {
		setupContext.addBeanSerializerModifier(new JacksonDataMaskingBeanSerializerModifier());
	}
}
