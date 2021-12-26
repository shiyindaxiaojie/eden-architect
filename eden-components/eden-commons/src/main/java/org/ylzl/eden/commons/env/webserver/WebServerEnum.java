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
 * @author gyl
 * @since 1.0.0
 */
public enum WebServerEnum {

  // 常用
  TOMCAT(
      new Handler() {

        @Override
        public boolean isCurrentWebServer() {
          return detect("/org/apache/catalina/startup/Bootstrap.class")
              || detect("/org/apache/catalina/startup/Embedded.class");
        }
      },
      WebServerEnum.JNDI_ENC_EJB_11),
  UNDERTOW(
      new Handler() {

        @Override
        public boolean isCurrentWebServer() {
          return detect("/io/undertow/Version.class");
        }
      },
      WebServerEnum.JNDI_ENC_EJB_11),
  JBOSS(
      new Handler() {

        @Override
        public boolean isCurrentWebServer() {
          return detect("/org/jboss/Main.class");
        }
      },
      "java:/"),
  JETTY(
      new Handler() {

        @Override
        public boolean isCurrentWebServer() {
          return detect("/org/mortbay/jetty/Server.class");
        }
      },
      WebServerEnum.JNDI_ENC_EJB_11),
  // 不常用
  GERONIMO(
      new Handler() {

        @Override
        public boolean isCurrentWebServer() {
          return detect("/org/apache/geronimo/system/main/Daemon.class");
        }
      },
      WebServerEnum.JNDI_ENC_EJB_11),
  GLASSFISH(
      new Handler() {

        @Override
        public boolean isCurrentWebServer() {
          return System.getProperty("com.sun.aas.instanceRoot") != null;
        }
      },
      WebServerEnum.JNDI_ENC_EJB_11),
  GLASSFISH2(
      new Handler() {

        @Override
        public boolean isCurrentWebServer() {
          return System.getProperty("com.sun.aas.instanceRoot") != null
              && !("GlassFish/v3".equals(System.getProperty("product.name")));
        }
      },
      WebServerEnum.JNDI_ENC_EJB_11),
  GLASSFISH3(
      new Handler() {

        @Override
        public boolean isCurrentWebServer() {
          return System.getProperty("com.sun.aas.instanceRoot") != null
              && "GlassFish/v3".equals(System.getProperty("product.name"));
        }
      },
      WebServerEnum.JNDI_ENC_EJB_11),
  JONAS(
      new Handler() {

        @Override
        public boolean isCurrentWebServer() {
          return detect("/org/objectweb/jonas/development/Server.class");
        }
      },
      WebServerEnum.JNDI_ENC_EJB_11),
  OC4J(
      new Handler() {

        @Override
        public boolean isCurrentWebServer() {
          return detect("oracle.oc4j.util.ClassUtils");
        }
      },
      WebServerEnum.JNDI_ENC_EJB_11),
  RESIN(
      new Handler() {

        @Override
        public boolean isCurrentWebServer() {
          return detect("/com/caucho/development/resin/Resin.class");
        }
      },
      WebServerEnum.JNDI_ENC_EJB_11),
  WEBLOGIC(
      new Handler() {

        @Override
        public boolean isCurrentWebServer() {
          return detect("/weblogic/Server.class");
        }
      },
      WebServerEnum.JNDI_ENC_EJB_11),
  WEBSPHERE(
      new Handler() {

        @Override
        public boolean isCurrentWebServer() {
          return detect("/com/ibm/websphere/product/VersionInfo.class");
        }
      },
      "java:comp/env/cas/");

  /** 标准命名服务前缀，自 EJB 1.1 引入的规范 */
  private static final String JNDI_ENC_EJB_11 = "java:comp/env/";

  private static final String MSG_UNSUPPORTED_EXCEPTION = "Unsupported application container";

  @Getter private final Handler handler;

  @Getter private final String lookup;

  WebServerEnum(Handler handler, String lookup) {
    this.handler = handler;
    this.lookup = lookup;
  }

  public static WebServerEnum toWebServerEnum() {
    for (WebServerEnum webServerEnum : WebServerEnum.values()) {
      if (webServerEnum.getHandler().isCurrentWebServer()) {
        return webServerEnum;
      }
    }
    throw new UnsupportedOperationException(MSG_UNSUPPORTED_EXCEPTION);
  }

  private static boolean detect(String className) {
    try {
      ClassLoader.getSystemClassLoader().loadClass(className);
    } catch (ClassNotFoundException cnfe) {
      Class<?> c = WebServerEnum.class;
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
