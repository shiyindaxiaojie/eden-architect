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

package org.ylzl.eden.spring.boot.commons.bean;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.spring.boot.commons.lang.ClassUtils;
import org.ylzl.eden.spring.boot.commons.lang.StringConstants;
import org.ylzl.eden.spring.boot.commons.lang.StringUtils;
import org.ylzl.eden.spring.boot.commons.lang.reflect.ReflectionUtils;

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
 * @author gyl
 * @since 1.0.0
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
        prop.put(field.getName(), StringConstants.EMPTY);
      }
    }
    return prop;
  }

  public static Properties load(@NonNull String relativeResource) throws IOException {
    InputStream inputStream = ClassUtils.getInputStreamFromResource(relativeResource);
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
      for (int i = 0; i < arr.length; ++i) {
        String[] item = arr[i].split(StringConstants.EQ);
        if (item.length == 1) {
          prop.setProperty(item[0], StringConstants.EMPTY);
        } else {
          prop.setProperty(item[0], item[1]);
        }
      }
    }
    return prop;
  }
}
