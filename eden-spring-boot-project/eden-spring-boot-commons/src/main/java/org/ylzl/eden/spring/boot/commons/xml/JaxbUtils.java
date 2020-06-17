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

package org.ylzl.eden.spring.boot.commons.xml;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Jaxb 工具集
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public class JaxbUtils {

  public static final String JAXB_DEFAULT_ENCODING = "UTF-8";

  @SuppressWarnings("unchecked")
  public static <T> String toXMLString(
      @NonNull Object object, @NonNull Class<T> cls, @NonNull String jaxbEncoding)
      throws JAXBException, IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    Marshaller marshaller = null;
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(cls);
      marshaller = jaxbContext.createMarshaller();
      marshaller.marshal(object, os);
      return os.toString(jaxbEncoding);
    } finally {
      marshaller = null;
      if (os != null) {
        os.close();
      }
      os = null;
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> String toXMLString(@NonNull Object object, @NonNull Class<T> cls)
      throws JAXBException, IOException {
    return toXMLString(object, cls, JAXB_DEFAULT_ENCODING);
  }

  @SuppressWarnings("unchecked")
  public static <T> T toObject(
      @NonNull String xmlString, @NonNull Class<T> cls, @NonNull String jaxbEncoding)
      throws JAXBException, IOException {
    ByteArrayInputStream stream = new ByteArrayInputStream(xmlString.getBytes(jaxbEncoding));
    Unmarshaller unmarshaller = null;
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(cls);
      unmarshaller = jaxbContext.createUnmarshaller();
      return (T) unmarshaller.unmarshal(stream);
    } finally {
      unmarshaller = null;
      if (stream != null) {
        stream.close();
      }
      stream = null;
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T toObject(@NonNull String xmlString, @NonNull Class<T> cls)
      throws JAXBException, IOException {
    return toObject(xmlString, cls, JAXB_DEFAULT_ENCODING);
  }
}
