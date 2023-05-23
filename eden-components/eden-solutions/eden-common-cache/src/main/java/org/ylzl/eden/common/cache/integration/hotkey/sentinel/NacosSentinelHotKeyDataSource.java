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

package org.ylzl.eden.common.cache.integration.hotkey.sentinel;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.api.PropertyKeyConst;
import org.ylzl.eden.common.cache.config.CacheConfig;

import java.util.List;
import java.util.Properties;

/**
 * Sentinel热key探测Nacos数据源
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class NacosSentinelHotKeyDataSource implements SentinelHotKeyDataSource {

	/**
	 * 注册数据源
	 *
	 * @param cacheConfig 缓存配置
	 */
	@Override
	public void registerDataSource(CacheConfig cacheConfig) {
		CacheConfig.HotKey.Sentinel sentinelConfig = cacheConfig.getHotKey().getSentinel();
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.SERVER_ADDR, sentinelConfig.getDataSource().getNacos().getServerAddr());
		properties.put(PropertyKeyConst.NAMESPACE, sentinelConfig.getDataSource().getNacos().getNamespace());
		ReadableDataSource<String, List<ParamFlowRule>> dataSource = new NacosDataSource<>(properties,
			sentinelConfig.getDataSource().getNacos().getGroupId(),
			sentinelConfig.getDataSource().getNacos().getDataId(),
			source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
			}));
		ParamFlowRuleManager.register2Property(dataSource.getProperty());
	}
}
