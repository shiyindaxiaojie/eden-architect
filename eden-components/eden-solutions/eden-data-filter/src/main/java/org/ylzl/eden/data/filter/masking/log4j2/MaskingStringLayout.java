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

	private List<PatternFormatter> patternFormatterList;

	public MaskingStringLayout(Charset charset) {
		super(charset);
		this.patternFormatterList = null;
	}

	public MaskingStringLayout(Charset charset, byte[] header, byte[] footer) {
		super(charset, header, footer);
		this.patternFormatterList = null;
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
		String text = stringBuilder.toString();
		return MaskingFilterHelper.maskingFilter().mask(text);
	}

	@PluginFactory
	public static MaskingStringLayout createLayout(@PluginConfiguration final Configuration config,
												   @PluginNode Node node,
												   @PluginAttribute(value = "charset", defaultString = "UTF-8") String charset,
												   @PluginAttribute(value = "pattern", defaultString = PatternLayout.DEFAULT_CONVERSION_PATTERN) String pattern) {
		PatternParser patternParser = null;
		if (config != null) {
			patternParser = config.getComponent(KEY);
		}
		if (patternParser == null) {
			patternParser = new PatternParser(config, KEY, LogEventPatternConverter.class);
		}

		MaskingStringLayout layout = new MaskingStringLayout(Charset.forName(charset));
		layout.patternFormatterList = patternParser.parse(pattern);
		return layout;
	}
}
