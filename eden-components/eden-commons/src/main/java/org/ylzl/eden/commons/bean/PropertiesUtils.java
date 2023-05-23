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

package org.ylzl.eden.commons.bean;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.io.ResourceUtils;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.commons.lang.reflect.ReflectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;

/**
 * 属性工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class PropertiesUtils {

	public static Properties toProperties(@NonNull Object obj) {
		Properties prop = new Properties();
		List<Field> fields = ReflectionUtils.getDeclaredFields(obj.getClass());
		for (Field field : fields) {
			ReflectionUtils.setAccessible(field);
			try {
				prop.put(field.getName(), field.get(obj));
			} catch (Exception e) {
				// 可忽略的异常
				prop.put(field.getName(), Strings.EMPTY);
			}
		}
		return prop;
	}

	public static Properties load(@NonNull String relativeResource) throws IOException {
		InputStream inputStream = ResourceUtils.getInputStreamFromResource(relativeResource);
		if (inputStream == null) {
			throw new IOException(MessageFormat.format("无效路径：{0}", relativeResource));
		}
		Properties prop = new Properties();
		prop.load(inputStream);
		return prop;
	}

	public static Properties loadFromFile(@NonNull File file) throws IOException {
		if (file.isDirectory()) {
			throw new IOException(MessageFormat.format("无效路径：{0}", file.getPath()));
		}
		Properties prop = new Properties();
		FileInputStream is = new FileInputStream(file);
		prop.load(is);
		return prop;
	}

	public static Properties split(@NonNull String str, @NonNull String regex) {
		Properties prop = new Properties();
		if (!StringUtils.isBlank(str) && !StringUtils.isBlank(regex)) {
			String[] arr = str.split(regex);
			for (String s : arr) {
				String[] item = s.split(Strings.EQ);
				if (item.length == 1) {
					prop.setProperty(item[0], Strings.EMPTY);
				} else {
					prop.setProperty(item[0], item[1]);
				}
			}
		}
		return prop;
	}
}
