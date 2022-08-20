package org.ylzl.eden.spring.cloud.sentinel.prometheus;

import com.alibaba.csp.sentinel.metric.extension.MetricExtension;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;

/**
 * Sentinel 监控扩展点实现
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class PrometheusMetricExtension implements MetricExtension {

	private SentinelCollectorRegistry registry;

	private SentinelCollectorRegistry getRegistry() {
		if (registry != null) {
			return registry;
		}
		this.registry = ApplicationContextHelper.getBean(SentinelCollectorRegistry.class);
		return registry;
	}

	/**
	 * Add current pass count of the resource name.
	 *
	 * @param resource resource name
	 * @param n        count to add
	 * @param args     additional arguments of the resource, eg. if the resource is a method name,
	 *                 the args will be the parameters of the method.
	 */
	@Override
	public void addPass(String resource, int n, Object... args) {
		getRegistry().getPassRequests().labels(resource).inc(n);
	}

	/**
	 * Add current block count of the resource name.
	 *
	 * @param resource       resource name
	 * @param n              count to add
	 * @param origin         the original invoker.
	 * @param ex 			 block exception related.
	 * @param args           additional arguments of the resource, eg. if the resource is a method name,
	 *                       the args will be the parameters of the method.
	 */
	@Override
	public void addBlock(String resource, int n, String origin, BlockException ex, Object... args) {
		getRegistry().getBlockRequests().labels(resource, ex.getClass().getSimpleName(), ex.getRuleLimitApp(), origin).inc(n);
	}

	/**
	 * Add current completed count of the resource name.
	 *
	 * @param resource resource name
	 * @param n        count to add
	 * @param args     additional arguments of the resource, eg. if the resource is a method name,
	 *                 the args will be the parameters of the method.
	 */
	@Override
	public void addSuccess(String resource, int n, Object... args) {
		getRegistry().getSuccessRequests().labels(resource).inc(n);
	}

	/**
	 * Add current exception count of the resource name.
	 *
	 * @param resource  resource name
	 * @param n         count to add
	 * @param throwable exception related.
	 */
	@Override
	public void addException(String resource, int n, Throwable throwable) {
		getRegistry().getExceptionRequests().labels(resource).inc(n);
	}

	/**
	 * Add response time of the resource name.
	 *
	 * @param resource resource name
	 * @param rt       response time in millisecond
	 * @param args     additional arguments of the resource, eg. if the resource is a method name,
	 *                 the args will be the parameters of the method.
	 */
	@Override
	public void addRt(String resource, long rt, Object... args) {
		getRegistry().getRtHist().labels(resource).observe(((double)rt) / 1000);
	}

	/**
	 * Increase current thread count of the resource name.
	 *
	 * @param resource resource name
	 * @param args     additional arguments of the resource, eg. if the resource is a method name,
	 *                 the args will be the parameters of the method.
	 */
	@Override
	public void increaseThreadNum(String resource, Object... args) {
		getRegistry().getCurrentThreads().labels(resource).inc();
	}

	/**
	 * Decrease current thread count of the resource name.
	 *
	 * @param resource resource name
	 * @param args     additional arguments of the resource, eg. if the resource is a method name,
	 *                 the args will be the parameters of the method.
	 */
	@Override
	public void decreaseThreadNum(String resource, Object... args) {
		getRegistry().getCurrentThreads().labels(resource).dec();
	}
}
