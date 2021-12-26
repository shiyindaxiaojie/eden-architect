package org.ylzl.eden.spring.data.redis.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Set;

/**
 * Jedis 集群
 *
 * @author gyl
 * @since 1.0.0
 */
public class FixedJedisCluster extends JedisCluster {

  public FixedJedisCluster(
      Set<HostAndPort> jedisClusterNode,
      int connectionTimeout,
      int soTimeout,
      int maxAttempts,
      String password,
      GenericObjectPoolConfig poolConfig) {
    super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
    super.connectionHandler =
        new FixedJedisSlotBasedConnectionHandler(
            jedisClusterNode, poolConfig, connectionTimeout, soTimeout, password);
  }

  public FixedJedisSlotBasedConnectionHandler getConnectionHandler() {
    return (FixedJedisSlotBasedConnectionHandler) this.connectionHandler;
  }

  public void refreshCluster() { // 刷新集群信息，当集群信息发生变更时调用
    connectionHandler.renewSlotCache();
  }
}
