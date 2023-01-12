package org.ylzl.eden.nacos.config.spring.cloud.util;

import com.alibaba.nacos.client.config.impl.LocalConfigInfoProcessor;
import com.alibaba.nacos.common.utils.StringUtils;

import java.io.File;

/**
 * LocalConfigInfoProcessor 方法暴露
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class LocalConfigInfoProcessorExporter extends LocalConfigInfoProcessor {

	public static final String SUFFIX = "_nacos";

	public static final String ENV_CHILD = "snapshot";

	public static final String FAILOVER_FILE_CHILD_1 = "data";

	public static final String FAILOVER_FILE_CHILD_2 = "config-data";

	public static final String FAILOVER_FILE_CHILD_3 = "config-data-tenant";

	public static final String SNAPSHOT_FILE_CHILD_1 = "snapshot";

	public static final String SNAPSHOT_FILE_CHILD_2 = "snapshot-tenant";

	public static File getFailoverFile(String serverName, String dataId, String group, String tenant) {
		File tmp = new File(LOCAL_SNAPSHOT_PATH, serverName + SUFFIX);
		tmp = new File(tmp, FAILOVER_FILE_CHILD_1);
		if (StringUtils.isBlank(tenant)) {
			tmp = new File(tmp, FAILOVER_FILE_CHILD_2);
		} else {
			tmp = new File(tmp, FAILOVER_FILE_CHILD_3);
			tmp = new File(tmp, tenant);
		}
		return new File(new File(tmp, group), dataId);
	}

	public static File getSnapshotFile(String envName, String dataId, String group, String tenant) {
		File tmp = new File(LOCAL_SNAPSHOT_PATH, envName + SUFFIX);
		if (StringUtils.isBlank(tenant)) {
			tmp = new File(tmp, SNAPSHOT_FILE_CHILD_1);
		} else {
			tmp = new File(tmp, SNAPSHOT_FILE_CHILD_2);
			tmp = new File(tmp, tenant);
		}
		return new File(new File(tmp, group), dataId);
	}
}
