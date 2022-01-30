package org.ylzl.eden.spring.data.h2.util;

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
 * @since 2.4.x
 */
public class H2ConfigurationHelper {

	public static Object createServer(int port) throws SQLException {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Class<?> serverClass = Class.forName("org.h2.tools.Server", true, loader);
			Method createServer = serverClass.getMethod("createTcpServer", String[].class);
			return createServer.invoke(null, new Object[]{new String[]{"-tcp", "-tcpAllowOthers", "-tcpPort",
				String.valueOf(port)}});
		} catch (ClassNotFoundException | LinkageError e) {
			throw new RuntimeException("Failed to load and initialize org.h2.tools.Server", e);
		} catch (SecurityException | NoSuchMethodException e) {
			throw new RuntimeException("Failed to get method org.h2.tools.Server.createTcpServer()", e);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new RuntimeException("Failed to invoke org.h2.tools.Server.createTcpServer()", e);
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			if (t instanceof SQLException) {
				throw (SQLException) t;
			}
			throw new RuntimeException("Unchecked exception in org.h2.tools.Server.createTcpServer()", t);
		}
	}

	public static void initH2Console(String propertiesLocation) {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Class<?> serverClass = Class.forName("org.h2.tools.Server", true, loader);
			Method createWebServer = serverClass.getMethod("createWebServer", String[].class);
			Method start = serverClass.getMethod("start");
			Object server = createWebServer.invoke(null, new Object[]{
				new String[]{"-properties", propertiesLocation}});
			start.invoke(server);
		} catch (Exception  e) {
			throw new RuntimeException("Failed to start h2 webserver console", e);
		}
	}

	public static void initH2Console(ServletContext servletContext) {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Class<?> servletClass = Class.forName("org.h2.server.web.WebServlet", true, loader);
			Servlet servlet = (Servlet) servletClass.getDeclaredConstructor().newInstance();
			ServletRegistration.Dynamic h2ConsoleServlet = servletContext.addServlet("H2Console", servlet);
			h2ConsoleServlet.addMapping("/h2-console/*");
			h2ConsoleServlet.setInitParameter("-properties", "src/main/resources/");
			h2ConsoleServlet.setLoadOnStartup(1);
		} catch (ClassNotFoundException | LinkageError | NoSuchMethodException | InvocationTargetException e) {
			throw new RuntimeException("Failed to load and initialize org.h2.server.web.WebServlet", e);

		} catch (IllegalAccessException | InstantiationException e) {
			throw new RuntimeException("Failed to instantiate org.h2.server.web.WebServlet", e);
		}
	}
}
