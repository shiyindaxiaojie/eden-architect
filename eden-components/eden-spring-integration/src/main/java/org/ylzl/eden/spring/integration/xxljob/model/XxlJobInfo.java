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

package org.ylzl.eden.spring.integration.xxljob.model;

import lombok.*;

import java.util.Date;

/**
 * XxlJob 任务
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class XxlJobInfo {

	private int id;

	private int jobGroup;

	private String jobCron;

	private String jobDesc;

	private Date addTime;

	private Date updateTime;

	private String author;

	private String alarmEmail;

	private String executorRouteStrategy;

	private String executorHandler;

	private String executorParam;

	private String executorBlockStrategy;

	private int executorTimeout;

	private int executorFailRetryCount;

	private String glueType;

	private String glueSource;

	private String glueRemark;

	private Date glueUpdateTime;

	private String childJobId;

	private int triggerStatus;

	private long triggerLastTime;

	private long triggerNextTime;
}
