package org.ylzl.eden.spring.integration.bpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ylzl.eden.spring.integration.bpc.executor.StandardProcessor;
import org.ylzl.eden.spring.integration.bpc.process.ProcessContext;

/**
 * 标准流程测试
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
@Component
public class StandardProcessorTest extends StandardProcessor {

	/**
	 * 执行流程
	 *
	 * @param context 上下文
	 */
	@Override
	protected void process(ProcessContext context) {
		System.out.println("StandardProcessor execute, id: " + context.get("id"));
	}
}
