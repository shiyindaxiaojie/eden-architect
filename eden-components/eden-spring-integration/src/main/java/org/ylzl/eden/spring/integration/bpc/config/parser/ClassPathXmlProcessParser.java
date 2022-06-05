package org.ylzl.eden.spring.integration.bpc.config.parser;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * 基于 Classpath 加载 XML 的流程解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ClassPathXmlProcessParser extends XmlProcessParser {

	private final String file;

	public ClassPathXmlProcessParser(String file) {
		this.file = file.startsWith("/") ? file : "/" + file;
	}

	/**
	 * 获取 XML 文档对象
	 *
	 * @return
	 * @throws DocumentException
	 */
	@Override
	protected Document getDocument() throws DocumentException {
		InputStream resourceAsStream = getClass().getResourceAsStream(file);
		SAXReader saxReader = new SAXReader();
		return saxReader.read(resourceAsStream);
	}
}
