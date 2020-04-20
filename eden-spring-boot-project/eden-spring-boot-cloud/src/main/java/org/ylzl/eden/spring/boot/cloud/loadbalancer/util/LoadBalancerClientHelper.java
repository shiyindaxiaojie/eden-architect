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

package org.ylzl.eden.spring.boot.cloud.loadbalancer.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 负载均衡客户端助手
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class LoadBalancerClientHelper {

    private static final String MSG_RECONSTRUCT_URI_UNREGIST_SERVER = "Cannot reconstruct uri due to serviceId {} is started not yet";

    private static final String MSG_RECONSTRUCT_URI = "Reconstruct uri from LoadBalancerClient：{}，serviceId: {}，serviceUri: {}";

    private static final String MSG_RECONSTRUCT_URI_EXP = "Reconstruct uri from LoadBalancerClient caught exception: {}";

    private final LoadBalancerClient loadBalancerClient;

    public LoadBalancerClientHelper(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    public String reconstructURI(String serviceId, String serviceUri)  {
        ServiceInstance serviceInstance = loadBalancerClient.choose(serviceId);
        if (serviceInstance == null) {
            log.warn(MSG_RECONSTRUCT_URI_UNREGIST_SERVER, serviceId);
            return null;
        }

        String loadBalancerUri;
        try {
            loadBalancerUri = loadBalancerClient.reconstructURI(serviceInstance, new URI(serviceUri)).toString();
        } catch (URISyntaxException e) {
            log.error(MSG_RECONSTRUCT_URI_EXP, e.getMessage(), e);
            return null;
        }
        log.debug(MSG_RECONSTRUCT_URI, loadBalancerUri, serviceId, serviceUri);
        return loadBalancerUri;
    }
}
