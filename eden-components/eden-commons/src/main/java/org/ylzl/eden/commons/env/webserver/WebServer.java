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
package org.ylzl.eden.commons.env.webserver;

import lombok.Getter;

/**
 * Web 服务器枚举
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public enum WebServer {

	TOMCAT(WebServer.JNDI_ENC_EJB_11) {

		@Override
		public boolean isCurrentWebServer() {
			return detect("/org/apache/catalina/startup/Bootstrap.class") ||
				detect("/org/apache/catalina/startup/Embedded.class");
		}
	},
	UNDERTOW(WebServer.JNDI_ENC_EJB_11) {

		@Override
		public boolean isCurrentWebServer() {
			return detect("/io/undertow/Version.class");
		}
	},
	JBOSS("java:/") {

		@Override
		public boolean isCurrentWebServer() {
			return detect("/org/jboss/Main.class");
		}
	},
	JETTY(WebServer.JNDI_ENC_EJB_11) {

		@Override
		public boolean isCurrentWebServer() {
			return detect("/org/mortbay/jetty/Server.class");
		}
	},
	GERONIMO(WebServer.JNDI_ENC_EJB_11) {

		@Override
		public boolean isCurrentWebServer() {
			return detect("/org/apache/geronimo/system/main/Daemon.class");
		}
	},
	GLASSFISH(WebServer.JNDI_ENC_EJB_11) {

		@Override
		public boolean isCurrentWebServer() {
			return System.getProperty("com.sun.aas.instanceRoot") != null;
		}
	},
	GLASSFISH2(WebServer.JNDI_ENC_EJB_11) {

		@Override
		public boolean isCurrentWebServer() {
			return System.getProperty("com.sun.aas.instanceRoot") != null &&
				!("GlassFish/v3".equals(System.getProperty("product.name")));
		}
	},
	GLASSFISH3(WebServer.JNDI_ENC_EJB_11) {

		@Override
		public boolean isCurrentWebServer() {
			return System.getProperty("com.sun.aas.instanceRoot") != null &&
				"GlassFish/v3".equals(System.getProperty("product.name"));
		}
	},
	JONAS(WebServer.JNDI_ENC_EJB_11) {

		@Override
		public boolean isCurrentWebServer() {
			return detect("/org/objectweb/jonas/development/Server.class");
		}
	},
	OC4J(WebServer.JNDI_ENC_EJB_11) {

		@Override
		public boolean isCurrentWebServer() {
			return detect("oracle.oc4j.util.ClassUtils");
		}
	},
	RESIN(WebServer.JNDI_ENC_EJB_11) {

		@Override
		public boolean isCurrentWebServer() {
			return detect("/com/caucho/development/resin/Resin.class");
		}
	},
	WEBLOGIC(WebServer.JNDI_ENC_EJB_11) {

		@Override
		public boolean isCurrentWebServer() {
			return detect("/weblogic/Server.class");
		}
	},
	WEBSPHERE("java:comp/env/cas/") {

		@Override
		public boolean isCurrentWebServer() {
			return detect("/com/ibm/websphere/product/VersionInfo.class");
		}
	};

	/** 标准命名服务前缀，自 EJB 1.1 引入的规范 */
	private static final String JNDI_ENC_EJB_11 = "java:comp/env/";

	private static final String MSG_UNSUPPORTED_EXCEPTION = "Unsupported application container";

	@Getter
	private final String lookup;

	WebServer(String lookup) {
		this.lookup = lookup;
	}

	public abstract boolean isCurrentWebServer();

	public static WebServer parse() {
		for (WebServer webServer : WebServer.values()) {
			if (webServer.isCurrentWebServer()) {
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
}
