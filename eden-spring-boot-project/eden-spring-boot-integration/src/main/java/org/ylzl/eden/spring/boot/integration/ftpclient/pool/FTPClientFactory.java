package org.ylzl.eden.spring.boot.integration.ftpclient.pool;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;

/**
 * FTP 客户端工厂
 *
 * @author gyl
 * @since 2.0.0
 */
@Slf4j
public class FTPClientFactory extends BasePooledObjectFactory<FTPClient> {

  private static final String MSG_CONNECTING = "FTPClient connect to {}:{}";

  private static final String MSG_CONDITIONING =
      "FTPClient disconnect from {}:{} failed due to connection refused, reply code is {}";

  private static final String MSG_CONNECT_ERROR =
      "FTPClient connect to {}:{} failed, caught exception: {}";

  private static final String MSG_LOGIN_FAILED = "FTPClient login {} to {}:{} failed";

  private static final String MSG_ENTER_PASSIVE_MODE = "FTPClient username {} enter passive mode";

  private static final String MSG_VALIDATE_FAILED = "Validate FTPClient sendNoOp return false";

  @Getter
  private final FTPClientPoolConfig ftpClientPoolConfig;

  public FTPClientFactory(FTPClientPoolConfig ftpClientPoolConfig) {
    this.ftpClientPoolConfig = ftpClientPoolConfig;
  }

  @Override
  public FTPClient create() throws Exception {
    FTPClient ftpClient = new FTPClient();

    String host = ftpClientPoolConfig.getHost();
    int port = ftpClientPoolConfig.getPort();
    log.debug(MSG_CONNECTING, host, port);
    ftpClient.setConnectTimeout(ftpClientPoolConfig.getConnectTimeOut());
    try {
      ftpClient.connect(host, port);
    } catch (IOException e) {
      log.error(MSG_CONNECT_ERROR, host, port, e.getMessage(), e);
      throw e;
    }

    int reply = ftpClient.getReplyCode();
    if (!FTPReply.isPositiveCompletion(reply)) {
      ftpClient.disconnect();
      log.error(MSG_CONDITIONING, host, port, reply);
      return null;
    }

    String username = ftpClientPoolConfig.getUsername();
    boolean isSuccess =
        ftpClient.login(ftpClientPoolConfig.getUsername(), ftpClientPoolConfig.getPassword());
    if (!isSuccess) {
      log.error(MSG_LOGIN_FAILED, username, host, port);
      return null;
    }

    ftpClient.setControlEncoding(ftpClientPoolConfig.getControlEncoding());
    ftpClient.setBufferSize(ftpClientPoolConfig.getBufferSize());
    ftpClient.setFileType(ftpClientPoolConfig.getFileType());
    ftpClient.setDataTimeout(ftpClientPoolConfig.getDataTimeout());
    ftpClient.setUseEPSVwithIPv4(ftpClientPoolConfig.isUseEPSVwithIPv4());
    if (ftpClientPoolConfig.isPassiveMode()) {
      log.info(MSG_ENTER_PASSIVE_MODE, username);
      ftpClient.enterLocalPassiveMode(); // 进入被动模式
    }
    return ftpClient;
  }

  @Override
  public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
    return new DefaultPooledObject<>(ftpClient);
  }

  @Override
  public void destroyObject(PooledObject<FTPClient> p) throws Exception {
    FTPClient ftpClient = p.getObject();
    ftpClient.logout();
    super.destroyObject(p);
  }

  @Override
  public boolean validateObject(PooledObject<FTPClient> p) {
    FTPClient ftpClient = p.getObject();
    boolean connect = false;
    try {
      connect = ftpClient.sendNoOp();
    } catch (IOException e) {
      log.error(MSG_VALIDATE_FAILED);
    }
    return connect;
  }
}
