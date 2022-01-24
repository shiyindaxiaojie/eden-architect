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

package org.ylzl.eden.spring.integration.metrics.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.spring.integration.core.constant.SpringIntegrationConstants;

/**
 * Metrics 配置属性
 *
 * @author gyl
 * @since 2.4.x
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SpringIntegrationConstants.PROP_PREFIX + ".metrics")
public class MetricsProperties {

  private final Jmx jmx = new Jmx();

  private final Logs logs = new Logs();

  private final Prometheus prometheus = new Prometheus();

  @Getter
  @Setter
  public static class Jmx {

    private boolean enabled = MetricsDefaults.Jmx.enabled;
  }

  @Getter
  @Setter
  public static class Logs {

    private boolean enabled = MetricsDefaults.Logs.enabled;

    private long reportFrequency = MetricsDefaults.Logs.reportFrequency;
  }

  @Getter
  @Setter
  public static class Prometheus {

    private boolean enabled = MetricsDefaults.Prometheus.enabled;

    private String endpoint = MetricsDefaults.Prometheus.endpoint;
  }
}
