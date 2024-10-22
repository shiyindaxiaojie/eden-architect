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

package org.ylzl.eden.data.filter.masking.log4j2;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.ylzl.eden.data.filter.config.MaskingConfig;
import org.ylzl.eden.data.filter.support.MaskingFilterHelper;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 数据脱敏日志格式化
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Plugin(name = "MaskingStringLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class MaskingStringLayout extends AbstractStringLayout {

	private static final String KEY = "Converter";

	private final MaskingConfig maskingConfig;

	private List<PatternFormatter> patternFormatterList;

	public MaskingStringLayout(Charset charset, MaskingConfig maskingConfig) {
		super(charset);
		this.maskingConfig = maskingConfig;
	}

	public MaskingStringLayout(Charset charset, byte[] header, byte[] footer, MaskingConfig maskingConfig) {
		super(charset, header, footer);
		this.maskingConfig = maskingConfig;
	}

	@Override
	public String toSerializable(LogEvent event) {
		if (patternFormatterList == null || patternFormatterList.isEmpty()) {
			return event.getMessage().getFormattedMessage();
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (PatternFormatter formatter : patternFormatterList) {
			formatter.format(event, stringBuilder);
		}
		return MaskingFilterHelper.maskingFilter(maskingConfig).mask(stringBuilder.toString());
	}

	@PluginFactory
	public static MaskingStringLayout createLayout(@PluginConfiguration final Configuration config,
												   @PluginAttribute(value = "charset", defaultString = "UTF-8") String charset,
												   @PluginAttribute(value = "pattern", defaultString = PatternLayout.DEFAULT_CONVERSION_PATTERN) String pattern,
												   @PluginAttribute(value = "type") String type,
												   @PluginAttribute(value = "strategies") String strategies,
												   @PluginAttribute(value = "replacement") String replacement,
												   @PluginAttribute(value = "hash") String hash,
												   @PluginAttribute(value = "whitelist") String whitelist) {

		PatternParser patternParser = null;
		if (config != null) {
			patternParser = config.getComponent(KEY);
		}
		if (patternParser == null) {
			patternParser = new PatternParser(config, KEY, LogEventPatternConverter.class);
		}

		MaskingConfig maskingConfig = new MaskingConfig();
		if (StringUtils.isNotBlank(type)) {
			maskingConfig.setType(type);
		}
		if (MaskingConfig.CHARS_SCAN.equals(maskingConfig.getType())) {
			if (StringUtils.isNotBlank(strategies)) {
				maskingConfig.getCharsScan().setReplaceList(strategies);
				maskingConfig.getCharsScan().setScanList(strategies);
			}
			if (StringUtils.isNotBlank(replacement)) {
				maskingConfig.getCharsScan().setDefaultReplace(replacement);
			}
			if (StringUtils.isNotBlank(hash)) {
				maskingConfig.getCharsScan().setReplaceHash(hash);
			}
			if (StringUtils.isNotBlank(whitelist)) {
				maskingConfig.getCharsScan().setWhiteList(whitelist);
			}
		}

		MaskingStringLayout layout = new MaskingStringLayout(Charset.forName(charset), maskingConfig);
		layout.patternFormatterList = patternParser.parse(pattern);
		return layout;
	}
}
