/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.commons.xml;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Dom4j 工具集
 *
 * @author gyl
 * @since 2.4.x
 */
@UtilityClass
public class Dom4jUtils {

	public static Document createDocument() {
		return DocumentHelper.createDocument();
	}

	public static Document toDocument(@NonNull String xmlString) throws DocumentException {
		return DocumentHelper.parseText(xmlString);
	}

	public static Document getDocument(@NonNull InputStream inputStream, boolean validation)
		throws Exception {
		return getSAXReader(validation).read(inputStream);
	}

	public static Document getDocument(@NonNull File file, boolean validation) throws Exception {
		return getSAXReader(validation).read(file);
	}

	public static SAXReader getSAXReader(boolean validation) {
		SAXReader saxReader = new SAXReader();
		saxReader.setValidation(validation);
		return saxReader;
	}

	public static Node selectSingleNode(@NonNull Object obj, @NonNull String expression) {
		if (obj instanceof Document) {
			Document document = (Document) obj;
			return document.selectSingleNode(expression);
		}
		if (obj instanceof Node) {
			Node node = (Node) obj;
			return node.selectSingleNode(expression);
		}
		if (obj instanceof Element) {
			Element element = (Element) obj;
			return element.selectSingleNode(expression);
		}
		return null;
	}

	public static List selectNodes(@NonNull Object obj, @NonNull String expression) {
		if (obj instanceof Document) {
			Document document = (Document) obj;
			return document.selectNodes(expression);
		}
		if (obj instanceof Node) {
			Node node = (Node) obj;
			return node.selectNodes(expression);
		}
		if (obj instanceof Element) {
			Element element = (Element) obj;
			return element.selectNodes(expression);
		}
		return null;
	}
}
