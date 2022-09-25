package org.ylzl.eden.bpm.config.parser;

import lombok.RequiredArgsConstructor;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

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
