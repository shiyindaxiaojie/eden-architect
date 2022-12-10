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

package org.ylzl.eden.commons.lang;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.*;

/**
 * 序列化工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class SerializableUtils {

	/**
	 * 深拷贝
	 *
	 * @param serializableObj
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Serializable deepCopy(@NonNull Serializable serializableObj)
		throws IOException, ClassNotFoundException {
		try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			 ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut);) {
			objectOut.writeObject(serializableObj);
			try (ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(byteArrayOut.toByteArray());
				 ObjectInputStream objectIn = new ObjectInputStream(byteArrayIn);) {
				return (Serializable) objectIn.readObject();
			}
		}
	}

	/**
	 * 获取序列化对象大小
	 *
	 * @param serializableObj
	 * @return
	 * @throws IOException
	 */
	public long getSerializedSize(@NonNull Serializable serializableObj) throws IOException {
		try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			 ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut);) {
			objectOut.writeObject(serializableObj);
			objectOut.flush();
			return byteArrayOut.size();
		}
	}

	/**
	 * 将序列化对象转化为字节数组
	 *
	 * @param serializableObj
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByte(@NonNull Serializable serializableObj) throws IOException {
		try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			 ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut);) {
			objectOut.writeObject(serializableObj);
			objectOut.flush();
			return byteArrayOut.toByteArray();
		}
	}

	/**
	 * 将字节数组转化为对象
	 *
	 * @param byteArr
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object toObject(byte[] byteArr) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(byteArr);
			 ObjectInputStream objectIn = new ObjectInputStream(byteArrayIn);) {
			return objectIn.readObject();
		}
	}
}
