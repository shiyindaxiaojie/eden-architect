package org.ylzl.eden.commons.net;

import lombok.experimental.UtilityClass;

import javax.net.ServerSocketFactory;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Random;

/**
 * TCP 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class TcpUtils {

	private static final Random RANDOM = new Random(System.currentTimeMillis());

	/**
	 * 检查端口是否可用
	 *
	 * @param port 端口
	 * @return 可用状态
	 */
	public static boolean isPortAvailable(int port) {
		try {
			ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(port, 1,
				InetAddress.getByName(IpConfig.LOCALHOST));
			serverSocket.close();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 查找可用的端口
	 *
	 * @param minPort 最小端口
	 * @param maxPort 最大端口
	 * @return 可用状态
	 */
	public static int findAvailablePort(int minPort, int maxPort) {
		int portRange = maxPort - minPort;
		int candidatePort;
		int searchCounter = 0;
		do {
			if (searchCounter > portRange) {
				throw new IllegalStateException(
					String.format("Could not find an available tcp port in the range [%d, %d] after %d attempts",
						minPort, maxPort, searchCounter));
			}
			candidatePort = getRandomPort(minPort, maxPort);
			searchCounter++;
		} while (!isPortAvailable(candidatePort));
		return candidatePort;
	}

	/**
	 * 获取随机端口
	 *
	 * @param minPort 最小端口
	 * @param maxPort 最大端口
	 * @return 随机端口
	 */
	public static int getRandomPort(int minPort, int maxPort) {
		int portRange = maxPort - minPort;
		return minPort + RANDOM.nextInt(portRange + 1);
	}
}
