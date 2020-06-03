package org.ylzl.eden.spring.boot.integration.ftpclient.pool2;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.ylzl.eden.spring.boot.integration.ftpclient.core.FTPClientConfig;

import java.io.IOException;

/**
 * FTP 客户端工厂
 *
 * @author gyl
 * @since 2.0.0
 */
@Slf4j
public class FTPClientPool2Factory extends BasePooledObjectFactory<FTPClient> {

  private static final String MSG_CONNECTING = "FTPClient connect to {}:{}";

  private static final String MSG_CONDITIONING =
      "FTPClient disconnect from {}:{} failed due to connection refused, reply code is {}";

  private static final String MSG_CONNECT_ERROR =
      "FTPClient connect to {}:{} failed, caught exception: {}";

  private static final String MSG_LOGIN_FAILED = "FTPClient login {} to {}:{} failed";

  private static final String MSG_ENTER_PASSIVE_MODE = "FTPClient username {} enter passive mode";

  private static final String MSG_VALIDATE_FAILED = "Validate FTPClient sendNoOp return false";

  @Getter private final FTPClientConfig config;

  public FTPClientPool2Factory(FTPClientConfig config) {
    this.config = config;
  }

  @Override
  public FTPClient create() throws Exception {
    FTPClient client = new FTPClient();

    String host = config.getHost();
    int port = config.getPort();
    log.debug(MSG_CONNECTING, host, port);
    client.setConnectTimeout(config.getConnectTimeOut());
    try {
      client.connect(host, port);
    } catch (IOException e) {
      log.error(MSG_CONNECT_ERROR, host, port, e.getMessage(), e);
      throw e;
    }

    int reply = client.getReplyCode();
    if (!FTPReply.isPositiveCompletion(reply)) {
      client.disconnect();
      log.error(MSG_CONDITIONING, host, port, reply);
      return null;
    }

    String username = config.getUsername();
    boolean isSuccess = client.login(config.getUsername(), config.getPassword());
    if (!isSuccess) {
      log.error(MSG_LOGIN_FAILED, username, host, port);
      return null;
    }

    client.setControlEncoding(config.getControlEncoding());
    client.setBufferSize(config.getBufferSize());
    client.setFileType(config.getFileType());
    client.setDataTimeout(config.getDataTimeout());
    client.setUseEPSVwithIPv4(config.isUseEPSVwithIPv4());
    if (config.isPassiveMode()) {
      log.info(MSG_ENTER_PASSIVE_MODE, username);
      client.enterLocalPassiveMode(); // 进入被动模式
    }
    return client;
  }

  @Override
  public void destroyObject(PooledObject<FTPClient> pooledObject) throws Exception {
    FTPClient client = pooledObject.getObject();
    client.logout();
    super.destroyObject(pooledObject);
  }

  @Override
  public PooledObject<FTPClient> wrap(FTPClient client) {
    return new DefaultPooledObject<>(client);
  }

  @Override
  public boolean validateObject(PooledObject<FTPClient> pooledObject) {
    FTPClient client = pooledObject.getObject();
    boolean connect = false;
    try {
      connect = client.sendNoOp();
    } catch (IOException e) {
      log.error(MSG_VALIDATE_FAILED);
    }
    return connect;
  }
}
