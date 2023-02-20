package org.ylzl.eden.spring.integration.cat.integration.redis;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ClassUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.spring.integration.cat.extension.CatConstants;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * RedisTemplate 集成 CAT
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class CatStringRedisTemplate extends StringRedisTemplate {

	@Override
	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		Class<?>[] interfacesForClass = ClassUtils.getAllInterfacesForClass(connection.getClass(),
			this.getClass().getClassLoader());
		connection = (RedisConnection) Proxy.newProxyInstance(connection.getClass().getClassLoader(),
			interfacesForClass, new CatRedisConnection(connection));
		return super.preProcessConnection(connection, existingConnection);
	}

	private static class CatRedisConnection implements InvocationHandler {

		private final RedisConnection target;

		protected CatRedisConnection(RedisConnection target) {
			this.target = target;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String name = proxy.getClass().getSuperclass().getName() + Strings.DOT + method.getName();
			Transaction transaction = Cat.newTransaction(CatConstants.TYPE_CACHE, name);
			Cat.logEvent(CatConstants.TYPE_CACHE_REDIS, "");
			try {
				transaction.setStatus(Transaction.SUCCESS);
				return method.invoke(this.target, args);
			} finally {
				transaction.complete();
			}
		}
	}
}
