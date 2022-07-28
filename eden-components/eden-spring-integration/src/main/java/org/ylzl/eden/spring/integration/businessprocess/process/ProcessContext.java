package org.ylzl.eden.spring.integration.businessprocess.process;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.StopWatch;
import org.ylzl.eden.spring.integration.businessprocess.executor.DynamicProcessor;
import org.ylzl.eden.spring.integration.businessprocess.executor.Processor;
import org.ylzl.eden.spring.integration.businessprocess.executor.RollbackProcessor;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 流程上下文
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class ProcessContext<T> {

	private T data;

	private final Map<String, Object> variables = Maps.newHashMapWithExpectedSize(16);

	private final Deque<RollbackProcessor> rollbackProcessors = Queues.newArrayDeque();

	private final ProcessDefinition processDefinition;

	private final AsyncTaskExecutor asyncTaskExecutor;

	private final CountDownLatch countDownLatch = new CountDownLatch(1);

	private static final String STOP_WATCH_ID = "business-process";

	/**
	 * 启动流程
	 */
	public void start() {
		ProcessNode firstProcessNode = processDefinition.getFirstProcessNode();
		String nodeName = firstProcessNode.getName();
		log.debug("Started process node '{}'", nodeName);
		StopWatch watch = new StopWatch(STOP_WATCH_ID);
		watch.start();
		process(firstProcessNode);
		await();
		watch.stop();
		log.debug("Finished process node '{}', cost {} milliseconds", nodeName, watch.getTotalTimeMillis());
	}

	/**
	 * 等待流程流程
	 */
	private void await() {
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 执行流程
	 *
	 * @param processNode 流程节点
	 */
	private void process(@NonNull ProcessNode processNode) {
		String nodeName = processNode.getName();
		Processor processor = processNode.getProcessor();
		try {
			if (processor instanceof RollbackProcessor) {
				rollbackProcessors.push((RollbackProcessor) processor);
			}
			processor.execute(this);
		} catch (Exception e) {
			log.error("Execute process node '{}' failed, context: {}, caught exception: {}",
				nodeName, variables.values(), e.getMessage(), e);

			RollbackProcessor rollbackProcessor;
			while (!rollbackProcessors.isEmpty()) {
				rollbackProcessor = rollbackProcessors.pop();
				String rollbackNodeName = rollbackProcessor.getName();
				try {
					rollbackProcessor.rollback(this);
					if (rollbackNodeName.equals(nodeName)) {
						log.info("Rollback process node '{}' success", rollbackNodeName);
					} else {
						log.info("Rollback process node '{}' from '{}' success", rollbackNodeName, nodeName);
					}
				} catch (Exception ex) {
					log.info("Rollback process node '{}' from '{}' failed, caught exception: {}",
						rollbackNodeName, nodeName, e.getMessage(), e);
				}
			}
			processor.onException(this, e);
		}

		// 当前节点执行完成后，根据调用链执行下一个节点
		Map<String, ProcessNode> nextNodes = processNode.getNextNodes();
		if (nextNodes.isEmpty()) {
			// 没有配置下一个节点，流程结束
			countDownLatch.countDown();
			return;
		}

		// 动态流程节点处理
		if (processor instanceof DynamicProcessor) {
			DynamicProcessor dynamicProcessor = (DynamicProcessor) processor;
			String nextNode = dynamicProcessor.nextNode(this);
			if (!nextNodes.containsKey(nextNode)) {
				throw new ProcessNodeException("DynamicProcess can not find next node '" + nextNode + "'");
			}
			execute(nextNodes.get(nextNode));
		} else {
			nextNodes.values().forEach(this::execute);
		}
	}

	private void execute(ProcessNode nextProcessNode) {
		if (nextProcessNode.isAsync()) {
			asyncTaskExecutor.execute(() -> process(nextProcessNode));
		} else {
			process(nextProcessNode);
		}
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setVariables(String key, Object value) {
		variables.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <V> V getVariables(String key) {
		return (V) variables.get(key);
	}
}
