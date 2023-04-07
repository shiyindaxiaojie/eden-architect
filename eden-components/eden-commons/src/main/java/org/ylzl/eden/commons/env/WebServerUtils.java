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
import org.apache.commons.lang3.StringUtils;
import org.ylzl.eden.commons.env.webserver.WebServer;
import org.ylzl.eden.commons.lang.Strings;

/**
 * Web 服务器工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class WebServerUtils {

	public static boolean isTomcat() {
		return WebServer.TOMCAT == WebServer.parse();
	}

	public static boolean isUndertow() {
		return WebServer.UNDERTOW == WebServer.parse();
	}

	public static boolean isJetty() {
		return WebServer.JETTY == WebServer.parse();
	}

	public static boolean isJBoss() {
		return WebServer.JBOSS == WebServer.parse();
	}

	public static boolean isWebLogic() {
		return WebServer.WEBLOGIC == WebServer.parse();
	}

	public static boolean isWebSphere() {
		return WebServer.WEBSPHERE == WebServer.parse();
	}

	public static String getHome() {
		return System.getProperty(StringUtils.join(WebServer.parse().name(), "_HOME"), Strings.EMPTY);
	}

	public static String getLookup() {
		return WebServer.parse().getLookup();
	}
}
