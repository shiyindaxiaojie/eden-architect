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

package org.ylzl.eden.spring.boot.integration.kafka.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.ylzl.eden.spring.boot.commons.lang.CharConstants;
import org.ylzl.eden.spring.boot.commons.lang.StringUtils;

/**
 * Kakfa 模板助手
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class KafkaTemplateHelper {

    public static final String MSG_WARN_CREATE_TOPIC = "Due to limitations in metric names, topics with a period ('.') or underscore ('_') could collide. " +
		"To avoid issues it is best to use either, but not both.";

    private final KafkaTemplate kafkaTemplate;

    public KafkaTemplateHelper(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 判断主题是否包含碰撞字符
     *
     * @param topic Kafka 主题
     * @return 是否包含碰撞字符
     */
    public boolean hasCollisionChars(String topic) {
        if (StringUtils.contains(topic, CharConstants.DOT) && StringUtils.contains(topic, CharConstants.UNDERLINE)) {
            log.warn(MSG_WARN_CREATE_TOPIC);
            return true;
        }
        return false;
    }
}
