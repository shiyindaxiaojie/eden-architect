package org.ylzl.eden.spring.boot.integration.ftpclient.core;

import org.apache.commons.net.ftp.FTPClient;
import org.ylzl.eden.spring.boot.commons.io.IOUtils;
import org.ylzl.eden.spring.boot.integration.ftpclient.pool.FTPClientPool;

import java.io.InputStream;

/**
 * FTPClient 模板
 *
 * @author gyl
 * @since 2.0.0
 */
public class FTPClientTemplate {

  private final FTPClientPool pool;

  public FTPClientTemplate(FTPClientPool pool) {
    this.pool = pool;
  }

  public byte[] retrieveFileStream(String remote) throws Exception {
    FTPClient client = null;
    try {
      client = pool.borrowObject();
      try (InputStream in = client.retrieveFileStream(remote); ) {
        if (in != null) {
          return IOUtils.toByteArray(in);
        }
      }
    } finally {
      pool.returnObject(client);
    }
    return null;
  }

  public boolean makeDirectory(String pathname) throws Exception {
    FTPClient client = null;
    try {
      client = pool.borrowObject();
      return client.makeDirectory(pathname);
    } finally {
      pool.returnObject(client);
    }
  }

  public boolean removeDirectory(String pathname) throws Exception {
    FTPClient client = null;
    try {
      client = pool.borrowObject();
      return client.removeDirectory(pathname);
    } finally {
      pool.returnObject(client);
    }
  }

  public boolean deleteFile(String pathname) throws Exception {
    FTPClient client = null;
    try {
      client = pool.borrowObject();
      return client.deleteFile(pathname);
    } finally {
      pool.returnObject(client);
    }
  }

  public boolean storeFile(String remote, InputStream local) throws Exception {
    FTPClient client = null;
    try {
      client = pool.borrowObject();
      return client.storeFile(remote, local);
    } finally {
      pool.returnObject(client);
    }
  }
}
