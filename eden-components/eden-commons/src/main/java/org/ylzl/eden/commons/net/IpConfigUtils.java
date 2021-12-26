/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import org.ylzl.eden.commons.lang.CharConstants;
import org.ylzl.eden.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * IP 配置工具集
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public class IpConfigUtils {

  /**
   * 获取客户端请求的 IP 地址
   *
   * @param request
   * @return
   */
  public static String getIpAddress(@NonNull HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (StringUtils.isEmpty(ip) || IpConfigConstants.UNKNOWN_IP.equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (StringUtils.isEmpty(ip) || IpConfigConstants.UNKNOWN_IP.equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (StringUtils.isEmpty(ip) || IpConfigConstants.UNKNOWN_IP.equalsIgnoreCase(ip)) {
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
        sb.append(CharConstants.MINUS);
      }
      String s = Integer.toHexString(mac[i] & 0xFF);
      sb.append(s.length() == 1 ? 0 + s : s);
    }
    return sb.toString().toUpperCase();
  }
}
