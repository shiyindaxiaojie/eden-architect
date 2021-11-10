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

package org.ylzl.eden.spring.boot.template.start;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;
import org.ylzl.eden.spring.boot.commons.lang.StringUtils;
import org.ylzl.eden.spring.boot.commons.lang.math.NumberUtils;
import org.ylzl.eden.spring.boot.framework.core.ProfileConstants;
import org.ylzl.eden.spring.boot.framework.core.util.SpringProfileUtils;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collection;

/**
 * 应用启动入口模板方法
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public abstract class SpringBootApplicationTemplate {

  public static final String WARN_RUNNING_IN_DEV_AND_PROD =
      "You have misconfigured your application! "
          + "It should not run with both the 'dev' and 'prod' profiles at the same time.";

  public static final String WARN_RUNNING_IN_DEV_AND_CLOUD =
      "You have misconfigured your application! "
          + "It should not run with both the 'dev' and 'cloud' profiles at the same time.";

  private final Environment env;

  public SpringBootApplicationTemplate(Environment env) {
    this.env = env;
  }

  protected static Environment run(SpringApplication app, String[] args) {
		System.setProperty("es.set.netty.runtime.available.processors", "false");
    SpringProfileUtils.addDefaultProfile(app);
    return app.run(args).getEnvironment();
  }

  protected static void logApplicationServerAfterRunning(Environment env) {
    String applicationName = StringUtils.trimToEmpty(env.getProperty("spring.application.name"));
    String contextPath = StringUtils.trimToEmpty(env.getProperty("server.servlet.context-path"));
    int serverPort = NumberUtils.toInt(env.getProperty("server.port"));
    String protocol = env.containsProperty("server.ssl.key-store") ? "https" : "http";
    String localhostAddress = "localhost";
    String hostAddress;
    try {
      hostAddress = InetAddress.getLocalHost().getHostAddress();
    } catch (Exception e) {
      hostAddress = localhostAddress;
      log.warn("The host name could not be determined, using `localhost` as fallback");
    }

    log.info(
        "\n----------------------------------------------------------\n\t"
            + "Application '{}' is running! \n\t"
            + "Profile(s): \t{}\n\t"
            + "Local Access URL: \t{}://{}:{}{}\n\t"
            + "External Access URL: \t{}://{}:{}{}"
            + "\n----------------------------------------------------------",
        applicationName,
        env.getActiveProfiles(),
        protocol,
        localhostAddress,
        serverPort,
        contextPath,
        protocol,
        hostAddress,
        serverPort,
        contextPath);
  }

  protected static void logConfigServerAfterRunning(Environment env) {
    String configServerStatus = env.getProperty("configserver.status");
    log.info(
        "\n----------------------------------------------------------\n\t"
            + "Config Server: \t{}\n"
            + "----------------------------------------------------------",
        configServerStatus == null
            ? "Not found or not setup for this application"
            : configServerStatus);
  }

  /** 初始化 */
  @PostConstruct
  public void init() {
    Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
    if (activeProfiles.contains(ProfileConstants.SPRING_PROFILE_DEVELOPMENT)) {
      if (activeProfiles.contains(ProfileConstants.SPRING_PROFILE_PRODUCTION)) {
        log.warn(WARN_RUNNING_IN_DEV_AND_PROD);
      } else if (activeProfiles.contains(ProfileConstants.SPRING_PROFILE_PRODUCTION)) {
        log.warn(WARN_RUNNING_IN_DEV_AND_CLOUD);
      }
    }
  }
}
