package org.ylzl.eden.spring.boot.integration.ftpclient.pool;

import lombok.NonNull;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * FTP 客户端连接池
 *
 * @author gyl
 * @since 2.0.0
 */
public class FTPClientPool {

  private final FTPClientFactory ftpClientFactory;

  private final GenericObjectPool<FTPClient> pool;

  public FTPClientPool(FTPClientFactory ftpClientFactory) {
    this.ftpClientFactory = ftpClientFactory;
    this.pool =
        new GenericObjectPool<FTPClient>(
            ftpClientFactory, ftpClientFactory.getFtpClientPoolConfig());
  }

  public FTPClient borrowObject() throws Exception {
    return pool.borrowObject();
  }

  public void returnObject(@NonNull FTPClient ftpClient) {
    pool.returnObject(ftpClient);
  }
}
