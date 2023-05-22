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

package org.ylzl.eden.spring.cloud.dubbo.loadbalance;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.loadbalance.RandomLoadBalance;
import org.ylzl.eden.spring.cloud.dubbo.DubboAttachments;

import java.util.List;

/**
 * 本地调用优先
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class LocalCallFirstLoadBalance extends RandomLoadBalance {

	@Override
	public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
		String ip = RpcContext.getContext().getAttachment(DubboAttachments.IP);
		if (ip == null) {
			ip = NetUtils.getLocalHost();
		}
		RpcContext.getContext().setAttachment(DubboAttachments.IP, ip);
		for (Invoker<T> invoker : invokers) {
			if (invoker.getUrl().getIp().equals(ip)) {
				log.debug("LocalCallFirst LoadBalance select ip: {}", ip);
				return invoker;
			}
		}
		return super.select(invokers, url, invocation);
	}
}
