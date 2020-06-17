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

package org.ylzl.eden.spring.boot.integration.infinispan;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.spring.boot.framework.core.FrameworkConstants;

/**
 * Infinispan 配置属性
 *
 * @author gyl
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = FrameworkConstants.PROP_PREFIX + ".infinispan")
public class InfinispanProperties {

  private String configFile = InfinispanDefaults.configFile;

  private boolean statsEnabled = InfinispanDefaults.statsEnabled;

  private final Distributed distributed = new Distributed();

  private final Local local = new Local();

  private final Replicated replicated = new Replicated();

  @Getter
  @Setter
  public static class Distributed {

    private int instanceCount = InfinispanDefaults.Distributed.instanceCount;

    private long maxEntries = InfinispanDefaults.Distributed.maxEntries;

    private long timeToLiveSeconds = InfinispanDefaults.Distributed.timeToLiveSeconds;
  }

  @Getter
  @Setter
  public static class Local {

    private long maxEntries = InfinispanDefaults.Local.maxEntries;

    private long timeToLiveSeconds = InfinispanDefaults.Local.timeToLiveSeconds;
  }

  @Getter
  @Setter
  public static class Replicated {

    private long maxEntries = InfinispanDefaults.Replicated.maxEntries;

    private long timeToLiveSeconds = InfinispanDefaults.Replicated.timeToLiveSeconds;
  }
}
