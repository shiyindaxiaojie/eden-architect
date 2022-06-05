package org.ylzl.eden.spring.integration.bpc.config.parser;

import lombok.RequiredArgsConstructor;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.StringReader;

/**
 * 基于 XML 字符串的流程解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
public class StringXmlProcessParser extends XmlProcessParser {

	private String xmlConfig;

	/**
	 * 获取 XML 文档对象
	 *
	 * @return
	 * @throws DocumentException
	 */
	@Override
	protected Document getDocument() throws DocumentException {
		SAXReader saxReader = new SAXReader();
		StringReader reader = new StringReader(xmlConfig);
		return saxReader.read(reader);
	}
}
