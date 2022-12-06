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
package org.ylzl.eden.commons.env.webserver;

import lombok.Getter;

/**
 * Web 服务器枚举
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public enum WebServer {

	// 常用
	TOMCAT(() -> detect("/org/apache/catalina/startup/Bootstrap.class")
		|| detect("/org/apache/catalina/startup/Embedded.class"),
		WebServer.JNDI_ENC_EJB_11),
	UNDERTOW(() -> detect("/io/undertow/Version.class"),
		WebServer.JNDI_ENC_EJB_11),
	JBOSS(() -> detect("/org/jboss/Main.class"),
		"java:/"),
	JETTY(() -> detect("/org/mortbay/jetty/Server.class"),
		WebServer.JNDI_ENC_EJB_11),
	// 不常用
	GERONIMO(() -> detect("/org/apache/geronimo/system/main/Daemon.class"),
		WebServer.JNDI_ENC_EJB_11),
	GLASSFISH(() -> System.getProperty("com.sun.aas.instanceRoot") != null,
		WebServer.JNDI_ENC_EJB_11),
	GLASSFISH2(() -> System.getProperty("com.sun.aas.instanceRoot") != null
		&& !("GlassFish/v3".equals(System.getProperty("product.name"))),
		WebServer.JNDI_ENC_EJB_11),
	GLASSFISH3(() -> System.getProperty("com.sun.aas.instanceRoot") != null
		&& "GlassFish/v3".equals(System.getProperty("product.name")),
		WebServer.JNDI_ENC_EJB_11),
	JONAS(() -> detect("/org/objectweb/jonas/development/Server.class"),
		WebServer.JNDI_ENC_EJB_11),
	OC4J(() -> detect("oracle.oc4j.util.ClassUtils"),
		WebServer.JNDI_ENC_EJB_11),
	RESIN(() -> detect("/com/caucho/development/resin/Resin.class"),
		WebServer.JNDI_ENC_EJB_11),
	WEBLOGIC(() -> detect("/weblogic/Server.class"),
		WebServer.JNDI_ENC_EJB_11),
	WEBSPHERE(() -> detect("/com/ibm/websphere/product/VersionInfo.class"),
		"java:comp/env/cas/");

	/**
	 * 标准命名服务前缀，自 EJB 1.1 引入的规范
	 */
	private static final String JNDI_ENC_EJB_11 = "java:comp/env/";

	private static final String MSG_UNSUPPORTED_EXCEPTION = "Unsupported application container";

	@Getter
	private final Handler handler;

	@Getter
	private final String lookup;

	WebServer(Handler handler, String lookup) {
		this.handler = handler;
		this.lookup = lookup;
	}

	public static WebServer parse() {
		for (WebServer webServer : WebServer.values()) {
			if (webServer.getHandler().isCurrentWebServer()) {
				return webServer;
			}
		}
		throw new UnsupportedOperationException(MSG_UNSUPPORTED_EXCEPTION);
	}

	private static boolean detect(String className) {
		try {
			ClassLoader.getSystemClassLoader().loadClass(className);
		} catch (ClassNotFoundException cnfe) {
			Class<?> c = WebServer.class;
			if (c.getResource(className) == null) {
				return false;
			}
		}
		return true;
	}

	public interface Handler {

		boolean isCurrentWebServer();
	}
}
