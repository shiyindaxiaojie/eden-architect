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
package org.ylzl.eden.spring.integration.truelicense.keystore;

import de.schlichtherle.license.AbstractKeyStoreParam;
import org.ylzl.eden.commons.io.IO;
import org.ylzl.eden.commons.lang.Classes;
import org.ylzl.eden.commons.lang.Strings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * 增强式密钥库参数
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CustomKeyStoreParam extends AbstractKeyStoreParam {

	private final String storePath;

	private final String alias;

	private final String storePwd;

	private final String keyPwd;

	public CustomKeyStoreParam(
		Class clazz, String resource, String alias, String storePwd, String keyPwd) {
		super(clazz, resource);
		this.storePath = resource;
		this.alias = alias;
		this.storePwd = storePwd;
		this.keyPwd = keyPwd;
	}

	@Override
	public String getAlias() {
		return alias;
	}

	@Override
	public String getStorePwd() {
		return storePwd;
	}

	@Override
	public String getKeyPwd() {
		return keyPwd;
	}

	@Override
	public InputStream getStream() throws IOException {
		InputStream inputStream;
		if (storePath.startsWith(Classes.CLASS_DIR)) {
			String path = storePath.substring(Classes.CLASS_DIR.length());
			if (!path.startsWith(IO.DIR_SEPARATOR)) {
				path = Strings.SLASH + path;
			}
			inputStream = this.getClass().getResourceAsStream(path);
		} else {
			inputStream = Files.newInputStream(new File(storePath).toPath());
		}
		if (inputStream == null) {
			throw new FileNotFoundException(storePath);
		}
		return inputStream;
	}
}
