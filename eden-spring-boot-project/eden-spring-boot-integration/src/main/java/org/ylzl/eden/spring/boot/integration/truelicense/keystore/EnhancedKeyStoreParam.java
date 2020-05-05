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
package org.ylzl.eden.spring.boot.integration.truelicense.keystore;

import de.schlichtherle.license.AbstractKeyStoreParam;
import org.ylzl.eden.spring.boot.commons.io.FileConstants;
import org.ylzl.eden.spring.boot.commons.lang.ClassConstants;
import org.ylzl.eden.spring.boot.commons.lang.StringConstants;

import java.io.*;

/**
 * 增强式密钥库参数
 *
 * @author gyl
 * @since 1.0.0
 */
public class EnhancedKeyStoreParam extends AbstractKeyStoreParam {

  private String storePath;

  private String alias;

  private String storePwd;

  private String keyPwd;

  public EnhancedKeyStoreParam(
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
    if (storePath.startsWith(ClassConstants.CLASS_DIR)) {
      String path = storePath.substring(ClassConstants.CLASS_DIR.length());
      if (!path.startsWith(FileConstants.DIR_SEPARATOR)) {
        path = StringConstants.SLASH + path;
      }
      inputStream = this.getClass().getResourceAsStream(path);
    } else {
      inputStream = new FileInputStream(new File(storePath));
    }
    if (inputStream == null) {
      throw new FileNotFoundException(storePath);
    }
    return inputStream;
  }
}
