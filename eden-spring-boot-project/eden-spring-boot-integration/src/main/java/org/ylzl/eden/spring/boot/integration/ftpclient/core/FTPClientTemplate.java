package org.ylzl.eden.spring.boot.integration.ftpclient.core;

import lombok.Setter;
import org.apache.commons.net.ftp.FTPClient;
import org.ylzl.eden.spring.boot.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * FTPClient 模板
 *
 * @author gyl
 * @since 2.0.0
 */
public class FTPClientTemplate {

  @Setter private FTPClientPool pool;

  @Setter private FTPClient client;

  public boolean makeDirectory(final String pathname) throws Exception {
    return new Template<Boolean>() {

      @Override
      Boolean exec(FTPClient client) throws IOException {
        return client.makeDirectory(pathname);
      }
    }.exec();
  }

  public boolean removeDirectory(final String pathname) throws Exception {
    return new Template<Boolean>() {

      @Override
      Boolean exec(FTPClient client) throws IOException {
        return client.removeDirectory(pathname);
      }
    }.exec();
  }

  public boolean deleteFile(final String pathname) throws Exception {
    return new Template<Boolean>() {

      @Override
      Boolean exec(FTPClient client) throws IOException {
        return client.deleteFile(pathname);
      }
    }.exec();
  }

  public boolean storeFile(final String remote, final InputStream local) throws Exception {
    return new Template<Boolean>() {

      @Override
      Boolean exec(FTPClient client) throws IOException {
        return client.storeFile(remote, local);
      }
    }.exec();
  }

  public byte[] retrieveFileStream(final String remote) throws Exception {
    return new Template<byte[]>() {

      @Override
      byte[] exec(FTPClient client) throws IOException {
        try (InputStream in = client.retrieveFileStream(remote); ) {
          if (in != null) {
            return IOUtils.toByteArray(in);
          }
        }
        return null;
      }
    }.exec();
  }

  private abstract class Template<T> {

    private static final String MSG_REQUIRE_SETTER =
        "FTPClientPool and FTPClient must provide at least one";

    private static final String MSG_REQUIRE_CONNECTED = "FTPClient must be connected";

    public T exec() throws Exception {
      FTPClient client = null;
      try {
        client = get();
        return exec(client);
      } finally {
        close(client);
      }
    }

    private FTPClient get() throws Exception {
      if (pool != null) {
        return pool.borrowObject();
      }
      if (client == null) {
        throw new RuntimeException(MSG_REQUIRE_SETTER);
      }
      if (!client.isConnected()) {
        throw new RuntimeException(MSG_REQUIRE_CONNECTED);
      }
      return client;
    }

    private void close(FTPClient client) throws Exception {
      if (pool != null) {
        pool.returnObject(client);
      }
      if (client != null && client.isConnected()) {
		  client.disconnect();
      }
    }

    abstract T exec(FTPClient client) throws IOException;
  }
}
