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

package org.ylzl.eden.flow.compose.parser;

import lombok.RequiredArgsConstructor;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.ylzl.eden.flow.compose.exception.ProcessParseException;

import java.io.StringReader;

/**
 * 基于 XML 字符串的流程解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class StringXmlProcessParser extends XmlProcessParser {

	private final String xmlConfig;

	/**
	 * 获取 XML 文档对象
	 *
	 * @return
	 */
	@Override
	protected Document getDocument() {
		SAXReader saxReader = new SAXReader();
		StringReader reader = new StringReader(xmlConfig);
		try {
			return saxReader.read(reader);
		} catch (DocumentException e) {
			throw new ProcessParseException("StringXmlProcessParser parse '" + xmlConfig + "' error");
		}
	}
}
