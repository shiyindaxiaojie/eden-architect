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

DROP TABLE IF EXISTS `leaf_alloc`;

CREATE TABLE `leaf_alloc`
(
	`biz_tag`     varchar(128) NOT NULL DEFAULT '',
	`max_id`      bigint(20) NOT NULL DEFAULT 1,
	`step`        int(11) NOT NULL,
	`description` varchar(256)          DEFAULT NULL,
	`update_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`biz_tag`)
) ENGINE=InnoDB;
