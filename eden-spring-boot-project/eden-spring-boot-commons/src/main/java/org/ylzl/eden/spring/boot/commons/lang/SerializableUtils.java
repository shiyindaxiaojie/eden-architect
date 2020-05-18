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

package org.ylzl.eden.spring.boot.commons.lang;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.*;

/**
 * 序列化工具集
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public class SerializableUtils {

  public static Serializable deepCopy(@NonNull Serializable serializableObj)
      throws IOException, ClassNotFoundException {
    try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut); ) {
      objectOut.writeObject(serializableObj);
      try (ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(byteArrayOut.toByteArray());
          ObjectInputStream objectIn = new ObjectInputStream(byteArrayIn); ) {
        return (Serializable) objectIn.readObject();
      }
    }
  }

  public long getSerializedSize(@NonNull Serializable serializableObj) throws IOException {
    try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut); ) {
      objectOut.writeObject(serializableObj);
      objectOut.flush();
      return byteArrayOut.size();
    }
  }

  public static byte[] toByte(@NonNull Serializable serializableObj) throws IOException {
    try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut); ) {
      objectOut.writeObject(serializableObj);
      objectOut.flush();
      return byteArrayOut.toByteArray();
    }
  }

  public static Object toObject(byte[] byteArr) throws IOException, ClassNotFoundException {
    try (ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(byteArr);
        ObjectInputStream objectIn = new ObjectInputStream(byteArrayIn); ) {
      return objectIn.readObject();
    }
  }
}
