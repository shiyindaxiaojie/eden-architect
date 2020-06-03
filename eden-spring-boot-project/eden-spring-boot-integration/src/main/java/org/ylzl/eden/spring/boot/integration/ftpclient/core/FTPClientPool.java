package org.ylzl.eden.spring.boot.integration.ftpclient.core;

import org.apache.commons.net.ftp.FTPClient;

/**
 * FTP 客户端连接池接口
 *
 * @author gyl
 * @since 2.0.0
 */
public interface FTPClientPool {

	FTPClient borrowObject() throws Exception;

	void returnObject(FTPClient ftpClient);
}
