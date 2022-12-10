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

package org.ylzl.eden.spring.integration.xxljob.constant;

import lombok.experimental.UtilityClass;

/**
 * xxl-job-admin 路由
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class XxlJobAdminRoutes {

	public static final String JOBGROUP_LOAD_BY_ID = "/jobgroup/loadById";

	public static final String JOBGROUP_SAVE = "/jobgroup/save";

	public static final String JOBGROUP_SAVE_OR_UPDATE = "/jobgroup/saveOrUpdate";

	public static final String JOBGROUP_UPDATE = "/jobgroup/update";

	public static final String JOBGROUP_REMOVE = "/jobgroup/remove";

	public static final String JOBINFO_PAGELIST = "/jobinfo/pageList";

	public static final String JOBINFO_ADD = "/jobinfo/add";

	public static final String JOBINFO_ADD_OR_UPDATE = "/jobinfo/addOrUpdate";

	public static final String JOBINFO_UPDATE = "/jobinfo/update";

	public static final String JOBINFO_REMOVE = "/jobinfo/remove";

	public static final String JOBINFO_STOP = "/jobinfo/stop";

	public static final String JOBINFO_START = "/jobinfo/start";

	public static final String JOBINFO_TRIGGER = "/jobinfo/trigger";
}
