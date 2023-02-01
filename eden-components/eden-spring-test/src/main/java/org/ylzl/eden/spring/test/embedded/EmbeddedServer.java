package org.ylzl.eden.spring.test.embedded;

import org.ylzl.eden.extension.SPI;

/**
 * 嵌入式服务
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SPI
public interface EmbeddedServer {

	/**
	 * 设置用户
	 *
	 * @param username 用户
	 * @return this
	 */
	default EmbeddedServer username(String username) {
		return this;
	}

	/**
	 * 设置密码
	 *
	 * @param password 密码
	 * @return this
	 */
	default EmbeddedServer password(String password) {
		return this;
	}

	/**
	 * 设置端口
	 *
	 * @param port 端口
	 * @return this
	 */
	default EmbeddedServer port(int port) {
		return this;
	}

	/**
	 * 启动
	 */
	void startup();

	/**
	 * 关闭
	 */
	void shutdown();

	/**
	 * 是否在运行中
	 *
	 * @return 是否在运行中
	 */
	boolean isRunning();
}
