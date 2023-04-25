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
package org.ylzl.eden.commons.net;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.commons.lang.Chars;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * IP 配置工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class IpConfigUtils {

	private static final String X_FORWARDED_FOR = "X-Forwarded-For";

	private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";

	private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

	/**
	 * 获取IP地址
	 *
	 * @return IP地址
	 */
	public static String getIpAddress() {
		return getIpAddress(null);
	}

	/**
	 * 获取IP地址
	 *
	 * @param interfaceName 网卡名称
	 * @return IP地址
	 */
	public static String getIpAddress(String interfaceName) {
		try {
			List<String> ipList = getHostAddress(interfaceName);
			return CollectionUtils.isNotEmpty(ipList) ? ipList.get(0) : Strings.EMPTY;
		} catch (Exception ex) {
			return Strings.EMPTY;
		}
	}

	/**
	 * 获取已激活网卡的IP地址
	 *
	 * @param interfaceName 网卡名称
	 * @return 已激活网卡的IP地址
	 * @throws SocketException 套接字异常
	 */
	private static List<String> getHostAddress(String interfaceName) throws SocketException {
		List<String> ipList = new ArrayList<String>(5);
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();
			Enumeration<InetAddress> allAddress = networkInterface.getInetAddresses();
			while (allAddress.hasMoreElements()) {
				InetAddress address = allAddress.nextElement();
				if (address.isLoopbackAddress()) {
					continue;
				}
				if (address instanceof Inet6Address) {
					continue;
				}
				String hostAddress = address.getHostAddress();
				if (interfaceName == null) {
					ipList.add(hostAddress);
				} else if (networkInterface.getDisplayName().equals(interfaceName)) {
					ipList.add(hostAddress);
				}
			}
		}
		return ipList;
	}

	/**
	 * 获取客户端请求的 IP 地址
	 *
	 * @param request
	 * @return
	 */
	public static String parseIpAddress(@NonNull HttpServletRequest request) {
		String ip = request.getHeader(X_FORWARDED_FOR);
		if (StringUtils.isEmpty(ip) || IpConfig.UNKNOWN_IP.equalsIgnoreCase(ip)) {
			ip = request.getHeader(PROXY_CLIENT_IP);
		}
		if (StringUtils.isEmpty(ip) || IpConfig.UNKNOWN_IP.equalsIgnoreCase(ip)) {
			ip = request.getHeader(WL_PROXY_CLIENT_IP);
		}
		if (StringUtils.isEmpty(ip) || IpConfig.UNKNOWN_IP.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取 MAC 地址
	 *
	 * @return
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public static String getMacAddress() throws SocketException, UnknownHostException {
		return getMacAddress(InetAddress.getLocalHost());
	}

	/**
	 * 获取MAC地址
	 *
	 * @param inetAddress
	 * @return
	 * @throws SocketException
	 */
	public static String getMacAddress(@NonNull InetAddress inetAddress) throws SocketException {
		byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append(Chars.MINUS);
			}
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}
		return sb.toString().toUpperCase();
	}
}
