package org.ylzl.eden.spring.integration.bpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ylzl.eden.spring.integration.bpc.executor.DynamicProcessor;
import org.ylzl.eden.spring.integration.bpc.process.ProcessContext;

/**
 * 动态流程测试
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
@Component
public class DynamicProcessorTest extends DynamicProcessor {

	/**
	 * 执行流程
	 *
	 * @param context 上下文
	 */
	@Override
	protected void process(ProcessContext context) {
		System.out.println("DynamicProcess execute, id: " + context.get("id"));
	}

	/**
	 * 指定下一个节点
	 *
	 * @param context
	 * @return
	 */
	@Override
	public String nextNode(ProcessContext context) {
		return "node4";
	}
}
