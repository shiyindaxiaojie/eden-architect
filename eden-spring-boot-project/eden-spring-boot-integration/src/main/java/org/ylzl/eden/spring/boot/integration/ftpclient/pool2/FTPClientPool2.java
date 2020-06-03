package org.ylzl.eden.spring.boot.integration.ftpclient.pool2;

import lombok.NonNull;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.ylzl.eden.spring.boot.integration.ftpclient.core.FTPClientPool;

/**
 * FTP 客户端连接池
 *
 * @author gyl
 * @since 2.0.0
 */
public class FTPClientPool2 implements FTPClientPool {

  private final GenericObjectPool<FTPClient> pool;

  public FTPClientPool2(FTPClientPool2Factory factory, FTPClientPool2Config config) {
    this.pool = new GenericObjectPool<FTPClient>(factory, config);
  }

  @Override
  public FTPClient borrowObject() throws Exception {
    return pool.borrowObject();
  }

  @Override
  public void returnObject(@NonNull FTPClient ftpClient) {
    pool.returnObject(ftpClient);
  }
}
