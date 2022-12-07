package org.ylzl.eden.flow.compose.parser;

import com.google.common.collect.Lists;
import org.dom4j.Document;
import org.dom4j.Element;
import org.ylzl.eden.flow.compose.parser.element.NodeElement;
import org.ylzl.eden.flow.compose.parser.element.ProcessElement;

import java.util.List;

/**
 * 基于 XML 结构的流程解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public abstract class XmlProcessParser implements ProcessParser {

	public static final String PROCESS_NAME = "name";

	public static final String PROCESS_NODE_NAME = "name";

	public static final String PROCESS_NODE_CLASS = "class";

	public static final String PROCESS_NODE_NEXT = "next";

	public static final String PROCESS_NODE_BEGIN = "begin";

	public static final String PROCESS_NODE_ASYNC = "async";

	/**
	 * 解析流程模型
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ProcessElement> parse() {
		Document document = getDocument();
		Element rootElement = document.getRootElement();
		List<Element> processElements = rootElement.elements();
		List<ProcessElement> elements = Lists.newArrayList();
		for (Element process : processElements) {
			ProcessElement processElement = new ProcessElement();
			processElement.setName(process.attributeValue(PROCESS_NAME));
			for (Element node : process.elements()) {
				NodeElement nodeElement = new NodeElement();
				nodeElement.setName(node.attributeValue(PROCESS_NODE_NAME));
				nodeElement.setClassName(node.attributeValue(PROCESS_NODE_CLASS));

				String next = node.attributeValue(PROCESS_NODE_NEXT);
				if (next != null) {
					nodeElement.setNextNode(next);
				}

				String begin = node.attributeValue(PROCESS_NODE_BEGIN);
				nodeElement.setBegin(Boolean.parseBoolean(begin));

				String async = node.attributeValue(PROCESS_NODE_ASYNC);
				nodeElement.setAsync(Boolean.parseBoolean(async));

				processElement.addNode(nodeElement);
			}
			elements.add(processElement);
		}
		return elements;
	}

	/**
	 * 获取 XML 文档对象
	 *
	 * @return
	 */
	protected abstract Document getDocument();
}
