package org.ylzl.eden.spring.cloud.nacos.ribbon;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;

import java.util.List;

/**
 * Balancer 扩展
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ExtendBalancer extends Balancer {

	/**
	 * 暴露 Balancer.getHostByRandomWeight()
	 *
	 * @param instances Instance List
	 * @return the chosen instance
	 */
	public static Instance getHostByRandomWeightExtend(List<Instance> instances) {
		return getHostByRandomWeight(instances);
	}
}
