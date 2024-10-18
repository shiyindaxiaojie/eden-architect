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
												   @PluginAttribute(value = "prefix") String prefix,
												   @PluginAttribute(value = "scanList") String scanList,
												   @PluginAttribute(value = "replaceList") String replaceList,
												   @PluginAttribute(value = "defaultReplace") String defaultReplace,
												   @PluginAttribute(value = "replaceHash") String replaceHash,
												   @PluginAttribute(value = "whiteList") String whiteList) {

		PatternParser patternParser = null;
		if (config != null) {
			patternParser = config.getComponent(KEY);
		}
		if (patternParser == null) {
			patternParser = new PatternParser(config, KEY, LogEventPatternConverter.class);
		}

		MaskingConfig maskingConfig = new MaskingConfig();
		if (type != null) {
			maskingConfig.setType(type);
		}
		if (prefix != null) {
			maskingConfig.getCharsScan().setPrefix(prefix);
		}
		if (scanList != null) {
			maskingConfig.getCharsScan().setScanList(scanList);
		}
		if (replaceList != null) {
			maskingConfig.getCharsScan().setReplaceList(replaceList);
		}
		if (defaultReplace != null) {
			maskingConfig.getCharsScan().setDefaultReplace(defaultReplace);
		}
		if (replaceHash != null) {
			maskingConfig.getCharsScan().setReplaceHash(replaceHash);
		}
		if (whiteList != null) {
			maskingConfig.getCharsScan().setWhiteList(whiteList);
		}

		MaskingStringLayout layout = new MaskingStringLayout(Charset.forName(charset), maskingConfig);
		layout.patternFormatterList = patternParser.parse(pattern);
		return layout;
	}
}
