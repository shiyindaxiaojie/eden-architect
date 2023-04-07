/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ylzl.eden.commons.env;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.env.os.OSArchitect;
import org.ylzl.eden.commons.env.os.OS;
import org.ylzl.eden.commons.lang.Strings;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

/**
 * 操作系统工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class OSUtils {

	private static final String WINDOWS_CPU_CMD = "wmic cpu get processorid";

	private static final String WINDOWS_MAINBOARD_CMD = "wmic baseboard get serialnumber";

	private static final String[] LINUX_CPU_SHELL = {
		"/bin/bash", "-c", "dmidecode -t processor | grep 'ID' | awk -F ':' '{print $2}' | head -n 1"
	};

	private static final String[] LINUX_MAINBOARD_SHELL = {
		"/bin/bash", "-c", "dmidecode | grep 'Serial Number' | awk -F ':' '{print $2}' | head -n 1"
	};

	public static boolean is32bit() {
		OSArchitect osArchitect = getOSArchEnum();
		switch (osArchitect) {
			case I386:
			case I686:
			case X86:
				return true;
		}
		return false;
	}

	public static boolean is64bit() {
		OSArchitect osArchitect = getOSArchEnum();
		switch (osArchitect) {
			case AMD64:
			case X86_64:
				return true;
		}
		return false;
	}

	public static boolean isWindows() {
		OS os = getOSEnum();
		return os == OS.WINDOWS;
	}

	public static boolean isMacOS() {
		OS os = getOSEnum();
		return Objects.requireNonNull(os) == OS.OSX;
	}

	public static boolean isLinux() {
		OS os = getOSEnum();
		return os == OS.LINUX;
	}

	public static OS getOSEnum() {
		return OS.parse(System.getProperty(SystemProperties.OS_NAME));
	}

	public static OSArchitect getOSArchEnum() {
		return OSArchitect.parse(System.getProperty(SystemProperties.OS_ARCH));
	}

	/**
	 * 获取 CPU 序列号
	 *
	 * @return
	 * @throws IOException
	 */
	public static String getCpuSerial() throws IOException {
		return getSerialNumber(WINDOWS_CPU_CMD, LINUX_CPU_SHELL);
	}

	/**
	 * 获取主板序列号
	 *
	 * @return
	 * @throws IOException
	 */
	public static String getMainBoardSerial() throws IOException {
		return getSerialNumber(WINDOWS_MAINBOARD_CMD, LINUX_MAINBOARD_SHELL);
	}

	/**
	 * 获取 IP 地址列表
	 *
	 * @return
	 * @throws SocketException
	 */
	public static List<String> getIpAddresses() throws SocketException {
		List<String> ipAddresses = new ArrayList<>();
		List<InetAddress> inetAddresses = getLocalAllInetAddresses();
		if (inetAddresses.size() > 0) {
			for (InetAddress inetAddress : inetAddresses) {
				if (!ipAddresses.contains(inetAddress.getHostAddress())) {
					ipAddresses.add(inetAddress.getHostAddress().toLowerCase());
				}
			}
		}
		return ipAddresses;
	}

	/**
	 * 获取 MAC 地址列表
	 *
	 * @return
	 * @throws SocketException
	 */
	public static List<String> getMacAddresses() throws SocketException {
		List<String> macAddresses = new ArrayList<>();
		List<InetAddress> inetAddresses = getLocalAllInetAddresses();
		if (inetAddresses.size() > 0) {
			for (InetAddress inetAddress : inetAddresses) {
				String macAddress = getMacAddress(inetAddress);
				if (!macAddresses.contains(macAddress)) {
					macAddresses.add(macAddress);
				}
			}
		}
		return macAddresses;
	}

	private static String getSerialNumber(String windowsMainboardCmd, String[] linuxMainboardShell)
		throws IOException {
		String serialNumber = null;
		Runtime runtime = Runtime.getRuntime();
		Process process =
			isWindows() ? runtime.exec(windowsMainboardCmd) : runtime.exec(linuxMainboardShell);
		process.getOutputStream().close();
		Scanner scanner = new Scanner(process.getInputStream());
		if (scanner.hasNext()) {
			scanner.next();
		}
		if (scanner.hasNext()) {
			serialNumber = scanner.next().trim();
		}
		scanner.close();
		return serialNumber;
	}

	/**
	 * 获取某个网络接口的 MAC 地址
	 *
	 * @param inetAddress
	 * @return
	 * @throws SocketException
	 */
	private static String getMacAddress(InetAddress inetAddress) throws SocketException {
		String joinStr = isWindows() ? Strings.MINUS : Strings.COLON;
		byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				stringBuilder.append(joinStr);
			}
			// 将十六进制字节转化为字符串
			String temp = Integer.toHexString(mac[i] & 0xff);
			if (temp.length() == 1) {
				stringBuilder.append("0" + temp);
			} else {
				stringBuilder.append(temp);
			}
		}
		return stringBuilder.toString().toUpperCase();
	}

	/**
	 * 获取当前服务器所有符合条件的 InetAddress
	 *
	 * @return
	 * @throws SocketException
	 */
	private static List<InetAddress> getLocalAllInetAddresses() throws SocketException {
		List<InetAddress> result = new ArrayList<>(4);

		// 遍历所有的网络接口
		for (Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			 networkInterfaces.hasMoreElements(); ) {
			NetworkInterface iface = networkInterfaces.nextElement();
			// 遍历 IP
			for (Enumeration<InetAddress> inetAddresses = iface.getInetAddresses();
				 inetAddresses.hasMoreElements(); ) {
				InetAddress inetAddr = inetAddresses.nextElement();

				// 排除 LoopbackAddress、SiteLocalAddress、LinkLocalAddress、MulticastAddress 类型的 IP 地址
				if (!inetAddr.isLoopbackAddress()
					&& !inetAddr.isLinkLocalAddress()
					&& !inetAddr.isMulticastAddress()) {
					result.add(inetAddr);
				}
			}
		}
		return result;
	}
}
