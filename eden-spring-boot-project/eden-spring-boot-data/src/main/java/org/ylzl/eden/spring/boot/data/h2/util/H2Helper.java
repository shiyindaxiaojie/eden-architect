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
package org.ylzl.eden.spring.boot.data.h2.util;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * H2 配置助手
 *
 * @author gyl
 * @since 0.0.1
 */
public class H2Helper {

    public static Object createServer() throws SQLException {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> serverClass = Class.forName("org.h2.tools.Server", true, loader);
            Method createServer = serverClass.getMethod("createTcpServer", String[].class);
            return createServer.invoke(null, new Object[]{new String[]{"-tcp", "-tcpAllowOthers"}});
        } catch (ClassNotFoundException | LinkageError e) {
            throw new RuntimeException("无法加载和初始化 org.h2.tools.Server", e);
        } catch (SecurityException | NoSuchMethodException e) {
            throw new RuntimeException("无法获得方法 org.h2.tools.Server.createTcpServer()", e);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new RuntimeException("无法调用 org.h2.tools.Server.createTcpServer()", e);
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            if (t instanceof SQLException) {
                throw (SQLException) t;
            }
            throw new RuntimeException("未经检查的例外情况 org.h2.tools.Server.createTcpServer()", t);
        }
    }

    public static void initH2Console(ServletContext servletContext) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> servletClass = Class.forName("org.h2.server.web.WebServlet", true, loader);
            Servlet servlet = (Servlet) servletClass.newInstance();

            ServletRegistration.Dynamic h2ConsoleServlet = servletContext.addServlet("H2Console", servlet);
            h2ConsoleServlet.addMapping("/h2-console/*");
            h2ConsoleServlet.setInitParameter("-properties", "src/main/resources/");
            h2ConsoleServlet.setLoadOnStartup(1);
        } catch (ClassNotFoundException | LinkageError e) {
            throw new RuntimeException("无法加载和初始化 org.h2.server.web.WebServlet", e);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("无法实例化 org.h2.server.web.WebServlet", e);
        }
    }
}
