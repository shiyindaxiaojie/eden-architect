package org.ylzl.eden.spring.integration.bpc.config.parser;

import com.google.common.collect.Lists;
import org.dom4j.Document;
import org.dom4j.Element;
import org.ylzl.eden.spring.integration.bpc.config.ProcessConfig;
import org.ylzl.eden.spring.integration.bpc.config.ProcessNodeConfig;

import java.util.List;

/**
 * 基于 XML 结构的流程解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public abstract class XmlProcessParser implements ProcessParser {

	public static final String PROCESS_NAME = "name";

	public static final String PROCESS_NODE_NAME = "name";

	public static final String PROCESS_NODE_CLASS = "class";

	public static final String PROCESS_NODE_NEXT = "next";

	/**
	 * 解析流程模型
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ProcessConfig> parse() {
		Document document = getDocument();
		Element rootElement = document.getRootElement();
		List<Element> processElements = rootElement.elements();
		List<ProcessConfig> processConfigs = Lists.newArrayList();
		for (Element process : processElements) {
			ProcessConfig model = new ProcessConfig();
			model.setName(process.attributeValue(PROCESS_NAME));
			List<Element> elements = process.elements();
			for (Element node : elements) {
				ProcessNodeConfig processNodeConfig = new ProcessNodeConfig();
				processNodeConfig.setName(node.attributeValue(PROCESS_NODE_NAME));
				processNodeConfig.setClassName(node.attributeValue(PROCESS_NODE_CLASS));

				String next = node.attributeValue(PROCESS_NODE_NEXT);
				if (next != null) {
					processNodeConfig.setNextNode(next);
				}
			}
		}
		return processConfigs;
	}

	/**
	 * 获取 XML 文档对象
	 *
	 * @return
	 */
	protected abstract Document getDocument();
}
