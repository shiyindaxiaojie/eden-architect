package org.ylzl.eden.spring.boot.data.redis.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSlotBasedConnectionHandler;
import redis.clients.jedis.exceptions.JedisNoReachableClusterNodeException;

import java.util.Set;

/**
 * Jedis 槽位连接处理器
 *
 * @author gyl
 * @since 1.0.0
 */
public class FixedJedisSlotBasedConnectionHandler extends JedisSlotBasedConnectionHandler {

  public FixedJedisSlotBasedConnectionHandler(
      Set<HostAndPort> nodes,
      GenericObjectPoolConfig poolConfig,
      int connectionTimeout,
      int soTimeout,
      String password) {
    super(nodes, poolConfig, connectionTimeout, soTimeout, password);
  }

  public JedisPool getJedisPoolFromSlot(int slot) {
    JedisPool connectionPool = cache.getSlotPool(slot);
    if (connectionPool != null) {
      return connectionPool;
    } else {
      renewSlotCache();
      connectionPool = cache.getSlotPool(slot);
      if (connectionPool != null) {
        return connectionPool;
      } else {
        throw new JedisNoReachableClusterNodeException(
            "No reachable node in cluster for slot " + slot);
      }
    }
  }
}
