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
package org.ylzl.eden.spring.boot.cloud.turbine.event;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * Turbine 端口应用监听器
 *
 * @author sion
 * @since 0.0.1
 */
public class TurbinePortApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        Integer serverPort = event.getEnvironment().getProperty("server.port", Integer.class);
        Integer managementPort = event.getEnvironment().getProperty("management.port", Integer.class);
        Integer turbinePort = event.getEnvironment().getProperty("turbine.stream.port", Integer.class);
        if (serverPort == null && managementPort == null) {
            return;
        }
        if (serverPort != Integer.valueOf(-1)) {
            Map<String, Object> ports = new HashMap<String, Object>();
            if (turbinePort == null) {
                ports.put("server.port", -1);
                if (serverPort != null) {
                    ports.put("turbine.stream.port", serverPort);
                }
            } else if (managementPort != null && managementPort != -1 && serverPort == null) {
                ports.put("server.port", managementPort);
            }
            event.getEnvironment().getPropertySources().addFirst(new MapPropertySource("ports", ports));
        }
    }
}
