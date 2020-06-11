package org.ylzl.eden.spring.boot.integration.ftpclient.core;

import lombok.Data;
import org.ylzl.eden.spring.boot.commons.env.CharsetConstants;

/**
 * FTP 客户端配置
 *
 * @author gyl
 * @since 2.0.0
 */
@Data
public class FTPClientConfig {

  private String host;

  private int port = 21;

  private String username;

  private String password;

  private int connectTimeOut = 5000;

  private int dataTimeout = 60000;

  private String controlEncoding = CharsetConstants.UTF_8_NAME;

  private int controlKeepAliveReplyTimeout = 1000;

  private int bufferSize = 2 << 10;

  private int fileType = 2;

  private boolean useEPSVwithIPv4 = false;

  /** 是否被动模式 */
  private boolean passiveMode = true;
}
