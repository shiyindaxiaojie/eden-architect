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

package org.ylzl.eden.spring.integration.cat.util;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Transaction 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class CatTransactionUtils {

	public static <T> T transaction(String type, String name, Map<String, Object> data, Supplier<T> function) {
		Transaction transaction = Cat.newTransaction(type, name);
		if (data != null && !data.isEmpty()) {
			data.forEach(transaction::addData);
		}
		try {
			T result = function.get();
			transaction.setStatus(Message.SUCCESS);
			return result;
		} catch (Exception e) {
			Cat.logError(e);
			if (e.getMessage() != null) {
				Cat.logEvent(type + "." + name + ".error", e.getMessage());
			}
			transaction.setStatus(e);
			throw e;
		} finally {
			transaction.complete();
		}
	}

	public static <T> T transaction(String type, String name, Supplier<T> function) {
		return transaction(type, name, null, function);
	}

	public static void transaction(String type, String name, Map<String, Object> data, Runnable runnable) {
		Transaction transaction = Cat.newTransaction(type, name);
		if (data != null && !data.isEmpty()) {
			data.forEach(transaction::addData);
		}
		try {
			runnable.run();
			transaction.setStatus(Message.SUCCESS);
		} catch (Exception e) {
			Cat.logError(e);
			if (e.getMessage() != null) {
				Cat.logEvent(type + "." + name + ".error", e.getMessage());
			}
			transaction.setStatus(e);
			throw e;
		} finally {
			transaction.complete();
		}
	}

	public static void transaction(String type, String name, Runnable runnable) {
		transaction(type, name, null, runnable);
	}
}
