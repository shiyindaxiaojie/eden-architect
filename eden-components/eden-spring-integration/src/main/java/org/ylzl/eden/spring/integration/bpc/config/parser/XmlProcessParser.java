package org.ylzl.eden.spring.integration.bpc.config.parser;

import com.google.common.collect.Lists;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.ylzl.eden.spring.integration.bpc.config.model.ProcessModel;
import org.ylzl.eden.spring.integration.bpc.config.model.ProcessNodeModel;

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
	public List<ProcessModel> parse() throws Exception {
		Document document = getDocument();
		Element rootElement = document.getRootElement();
		List<Element> processElements = rootElement.elements();
		List<ProcessModel> processModels = Lists.newArrayList();
		for (Element process : processElements) {
			ProcessModel model = new ProcessModel();
			model.setName(process.attributeValue(PROCESS_NAME));
			List<Element> elements = process.elements();
			for (Element node : elements) {
				ProcessNodeModel processNodeModel = new ProcessNodeModel();
				processNodeModel.setName(node.attributeValue(PROCESS_NODE_NAME));
				processNodeModel.setClassName(node.attributeValue(PROCESS_NODE_CLASS));

				String next = node.attributeValue(PROCESS_NODE_NEXT);
				if (next != null) {
					processNodeModel.setNextNode(next);
				}
			}
		}
		return processModels;
	}

	/**
	 * 获取 XML 文档对象
	 *
	 * @return
	 * @throws DocumentException
	 */
	protected abstract Document getDocument() throws DocumentException;
}
