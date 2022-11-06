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
