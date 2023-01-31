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

package org.ylzl.eden.spring.test.embedded.zookeeper;

import org.apache.zookeeper.server.embedded.ZooKeeperServerEmbedded;
import org.junit.rules.ExternalResource;

/**
 * 嵌入式的 Zookeeper Server
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class EmbeddedZooKeeperServer extends ExternalResource {

    private ZooKeeperServerEmbedded zooKeeperServer;

    private boolean closed = true;

    @Override
    public void before() {
        try {
            this.zooKeeperServer = ZooKeeperServerEmbedded.builder().build();
            this.zooKeeperServer.start();
            closed = false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void after() {
        if (!isOpen()) {
            return;
        }
        try {
            this.zooKeeperServer.close();
            closed = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isOpen() {
        return !closed;
    }
}
