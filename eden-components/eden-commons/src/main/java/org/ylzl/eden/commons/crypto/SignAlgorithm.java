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

package org.ylzl.eden.commons.crypto;

import lombok.Getter;

/**
 * 签名算法
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum SignAlgorithm {

	NONE_WITH_RSA("NONEwithRSA"),
	MD2_WITH_RSA("MD2withRSA"),
	MD5_WITH_RSA("MD5withRSA"),
	SHA1_WITH_RSA("SHA1withRSA"),
	SHA256_WITH_RSA("SHA256withRSA"),
	SHA384_WITH_RSA("SHA384withRSA"),
	SHA512_WITH_RSA("SHA512withRSA"),

	NONE_WITH_DSA("NONEwithDSA"),
	SHA1_WITH_DSA("SHA1withDSA"),

	NONE_WITH_ECDSA("NONEwithECDSA"),
	SHA1_WITH_ECDSA("SHA1withECDSA"),
	SHA256_WITH_ECDSA("SHA256withECDSA"),
	SHA384_WITH_ECDSA("SHA384withECDSA"),
	SHA512_WITH_ECDSA("SHA512withECDSA"),

	SHA256_WITH_RSA_PSS("SHA256WithRSA/PSS"),
	SHA384_WITH_RSA_PSS("SHA384WithRSA/PSS"),
	SHA512_WITH_RSA_PSS("SHA512WithRSA/PSS");

	private final String value;

	SignAlgorithm(String value) {
		this.value = value;
	}
}
