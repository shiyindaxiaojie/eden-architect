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

package org.ylzl.eden.commons.io;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.lang.ClassLoaderUtils;
import org.ylzl.eden.commons.lang.Strings;

import java.io.InputStream;
import java.net.URL;

/**
 * 资源操作工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class ResourceUtils {

	public static URL getURLFromClassLoader() {
		return getURLFromResource(Strings.EMPTY);
	}

	public static String getPathFromResource(String relativeResource) {
		return getURLFromResource(relativeResource).getPath();
	}

	public static String getPathFromResource() {
		return getPathFromResource(Strings.EMPTY);
	}

	public static URL getURLFromResource(String relativeResource) {
		return ClassLoaderUtils.getClassLoader().getResource(relativeResource);
	}

	public static InputStream getInputStreamFromResource(String relativeResource) {
		return ClassLoaderUtils.getClassLoader().getResourceAsStream(relativeResource);
	}
}
