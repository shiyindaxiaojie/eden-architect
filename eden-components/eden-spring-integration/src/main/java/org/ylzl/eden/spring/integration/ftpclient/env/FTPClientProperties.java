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

package org.ylzl.eden.spring.integration.ftpclient.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.spring.integration.core.constant.SpringIntegrationConstants;
import org.ylzl.eden.spring.integration.ftpclient.pool2.FTPClientPool2Config;

/**
 * FTPClient 配置属性
 *
 * @author gyl
 * @since 2.4.x
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SpringIntegrationConstants.PROP_PREFIX + ".ftpclient")
public class FTPClientProperties {

  private Boolean enabled = FTPClientDefaults.enabled;

  private String host = FTPClientDefaults.host;

  private Integer port = FTPClientDefaults.port;

  private String username = FTPClientDefaults.username;

  private String password = FTPClientDefaults.password;

  private Integer connectTimeOut = FTPClientDefaults.connectTimeOut;

  private Integer dataTimeout = FTPClientDefaults.dataTimeout;

  private String controlEncoding = FTPClientDefaults.controlEncoding;

  private Integer controlKeepAliveReplyTimeout = FTPClientDefaults.controlKeepAliveReplyTimeout;

  private Integer bufferSize = FTPClientDefaults.bufferSize;

  private Integer fileType = FTPClientDefaults.fileType;

  private Boolean useEPSVwithIPv4 = FTPClientDefaults.useEPSVwithIPv4;

  private Boolean passiveMode = FTPClientDefaults.passiveMode;

  private FTPClientPool2Config pool = new FTPClientPool2Config();
}
