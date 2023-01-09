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

package org.ylzl.eden.data.auditor.support;

import lombok.experimental.UtilityClass;

/**
 * 数据脱敏内置策略
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class DataMaskerStrategy {

	public static final String ADDRESS = "address";

	public static final String BANK_CARD = "bank-card";

	public static final String CAR_LICENSE = "car-license";

	public static final String CHINESE_NAME = "chinese-name";

	public static final String EMAIL = "email";

	public static final String ID_CARD = "id-card";

	public static final String MOBILE_PHONE = "mobile-phone";

	public static final String MONEY = "money";

	public static final String PASSWORD = "password";

	public static final String TELEPHONE = "telephone";

	public static final String USERNAME = "username";
}
