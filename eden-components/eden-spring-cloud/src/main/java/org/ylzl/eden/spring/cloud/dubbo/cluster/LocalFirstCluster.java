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

package org.ylzl.eden.spring.cloud.dubbo.cluster;

import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.Cluster;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker;
import org.apache.dubbo.rpc.cluster.support.FailoverClusterInvoker;
import org.ylzl.eden.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 本地调用优先
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class LocalFirstCluster implements Cluster {

	public final static String NAME = "dev";

	@Override
	public <T> Invoker<T> join(Directory<T> directory, boolean buildFilterChain) throws RpcException {
		return new AbstractClusterInvoker<T>(directory) {

			@Override
			protected Result doInvoke(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance)
				throws RpcException {
				checkInvokers(invokers, invocation);

				String localhost = NetUtils.getLocalHost();
				List<String> invokersHosts = invokers.stream().map(i -> i.getUrl().getHost()).collect(Collectors.toList());
				if (CollectionUtils.isEmpty(invokersHosts) || !invokersHosts.contains(localhost)) {
					FailoverClusterInvoker<T> failoverClusterInvoker = new FailoverClusterInvoker<>(directory);
					return failoverClusterInvoker.doInvoke(invocation, invokers, loadbalance);
				}

				Invoker<T> invoked = invokers.stream()
					.filter(invoker -> invoker.getUrl().getHost().equals(localhost)).findFirst()
					.orElseThrow(() -> new RpcException("Failed to invoke the method"));

				return invoked.invoke(invocation);
			}
		};
	}
}
