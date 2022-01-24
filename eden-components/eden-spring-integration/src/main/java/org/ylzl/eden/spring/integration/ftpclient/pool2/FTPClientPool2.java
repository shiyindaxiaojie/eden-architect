package org.ylzl.eden.spring.integration.ftpclient.pool2;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.ylzl.eden.spring.integration.ftpclient.core.FTPClientPool;

/**
 * FTP 客户端连接池
 *
 * @author gyl
 * @since 2.0.0
 */
public class FTPClientPool2 extends GenericObjectPool<FTPClient> implements FTPClientPool {

	public FTPClientPool2(PooledObjectFactory<FTPClient> factory) {
		super(factory);
	}

	public FTPClientPool2(
		PooledObjectFactory<FTPClient> factory, GenericObjectPoolConfig<FTPClient> config) {
		super(factory, config);
	}

	public FTPClientPool2(
		PooledObjectFactory<FTPClient> factory,
		GenericObjectPoolConfig<FTPClient> config,
		AbandonedConfig abandonedConfig) {
		super(factory, config, abandonedConfig);
	}
}
