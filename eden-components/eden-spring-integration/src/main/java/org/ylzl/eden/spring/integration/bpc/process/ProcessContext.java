package org.ylzl.eden.spring.integration.bpc.process;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.ylzl.eden.spring.integration.bpc.executor.DynamicProcessor;
import org.ylzl.eden.spring.integration.bpc.executor.Processor;
import org.ylzl.eden.spring.integration.bpc.executor.RollbackProcessor;

import java.util.Deque;
import java.util.Map;

/**
 * 流程上下文
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class ProcessContext {

	private final Map<String, Object> variables = Maps.newHashMapWithExpectedSize(16);

	private final Deque<RollbackProcessor> rollbackProcessors = Queues.newArrayDeque();

	private final ProcessDefinition processDefinition;

	private final AsyncTaskExecutor asyncTaskExecutor;

	public void set(String key, Object value) {
		variables.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) variables.get(key);
	}

	/**
	 * 启动流程
	 */
	public void start() {
		ProcessNode firstProcessNode = processDefinition.getFirstProcessNode();
		String nodeName = firstProcessNode.getName() ;
		log.debug("Started process node '{}}'", nodeName);
		process(firstProcessNode);
		log.debug("Finished process node '{}'", nodeName);
	}

	/**
	 * 执行流程
	 *
	 * @param processNode
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
					log.info("Rollback process node '{}' from '{}' success", rollbackNodeName, nodeName);
				} catch (Exception ex) {
					log.info("Rollback process node '{}' from '{}' failed, caught exception: {}",
						rollbackNodeName, nodeName, e.getMessage(), e);
				}
			}
			processor.onException(this, e);
			throw e;
		}

		// 当前节点执行完成后，根据调用链执行下一个节点
		Map<String, ProcessNode> nextNodes = processNode.getNextNodes();
		if (nextNodes.isEmpty()) {
			// 没有配置下一个节点，流程结束
			return;
		}

		// 动态流程节点处理
		ProcessNode nextProcessNode;
		if (processor instanceof DynamicProcessor) {
			DynamicProcessor dynamicProcessor = (DynamicProcessor) processor;
			String nextNode = dynamicProcessor.nextNode(this);
			if (!nextNodes.containsKey(nextNode)) {
				throw new ProcessNodeException("DynamicProcess can not find next node '" + nextNode + "'");
			}
			nextProcessNode = nextNodes.get(nextNode);
		} else {
			nextProcessNode = nextNodes.values().stream().findAny().get();
		}
		if (processNode.isSyncInvokeNextNode()) {
			process(nextProcessNode);
		} else {
			asyncTaskExecutor.execute(() -> process(nextProcessNode));
		}
	}
}
