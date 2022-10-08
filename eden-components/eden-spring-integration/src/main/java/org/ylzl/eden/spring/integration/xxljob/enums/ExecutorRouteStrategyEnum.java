package org.ylzl.eden.spring.integration.xxljob.enums;

/**
 * 执行器路由策略
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public enum ExecutorRouteStrategyEnum {

	FIRST, // 固定选择第一个机器
	LAST, // 固定选择最后一个机器
	ROUND, // 轮询选择在线的机器
	RANDOM, // 随机选择在线的机器
	CONSISTENT_HASH, // 根据一致性哈希算法选择机器
	LEAST_FREQUENTLY_USED, // 使用频率最低的机器优先被选举
	LEAST_RECENTLY_USED, // 最久未使用的机器优先被选举
	FAILOVER, // 故障转移，按照顺序依次进行心跳检测，第一个心跳检测成功的机器选定为目标执行器并发起调度
	BUSYOVER, // 忙碌转移，按照顺序依次进行空闲检测，第一个空闲检测成功的机器选定为目标执行器并发起调度
	SHARDING_BROADCAST; // 分片广播，集群中所有机器根据系统传递的分片参数并行执行一次任务
}
