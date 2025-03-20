/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.framework.logging.stacktrace;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

/**
 * 异常堆栈转换
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Plugin(name = "StackTraceConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"st", "stacktrace"})
public class StackTraceConverter extends LogEventPatternConverter {

	private static final int DEFAULT_CAUSE_DEPTH = 3;

	private static final int DEFAULT_STACK_DEPTH = 5;

	/** Cause by 深度 */
	private final int causeDepth;

	/** 堆栈深度 */
	private final int stackDepth;

	public StackTraceConverter(int causeDepth, int stackDepth) {
		super("StackTrace", "stacktrace");
		this.causeDepth = causeDepth > 0 ? causeDepth : DEFAULT_CAUSE_DEPTH;
		this.stackDepth = stackDepth > 0 ? stackDepth : DEFAULT_STACK_DEPTH;
	}

	public static StackTraceConverter newInstance(String[] options) {
		int causeDepth = parseOption(options, 0, DEFAULT_CAUSE_DEPTH);
		int stackDepth = parseOption(options, 1, DEFAULT_STACK_DEPTH);
		return new StackTraceConverter(causeDepth, stackDepth);
	}

	private static int parseOption(String[] options, int index, int defaultValue) {
		try {
			if (options != null && options.length > index) {
				return Integer.parseInt(options[index]);
			}
		} catch (NumberFormatException ignored) {
		}
		return defaultValue;
	}

	@Override
	public void format(LogEvent event, StringBuilder output) {
		Throwable throwable = event.getThrown();
		if (throwable != null) {
			ForwardCounter counter = new ForwardCounter();
			output.append("\n");
			recursiveReversePrint(throwable, output, counter);
		}
	}

	private void recursiveReversePrint(Throwable throwable, StringBuilder output,
									   ForwardCounter counter) {
		if (throwable == null || counter.value >= causeDepth) {
			return;
		}

		if (throwable.getCause() != null) {
			recursiveReversePrint(throwable.getCause(), output, counter);
		}

		if (counter.value++ < causeDepth) {
			printSingleStack(throwable, output);
		}
	}

	private void printSingleStack(Throwable throwable, StringBuilder output) {
		output.append(reduceClassName(throwable.getClass().getName()))
			.append(": ")
			.append(throwable.getMessage())
			.append("\n");

		StackTraceElement[] stack = throwable.getStackTrace();
		for (int i = 0; i < Math.min(stackDepth, stack.length); i++) {
			output.append("\tat ")
				.append(formatStackElement(stack[i]))
				.append("\n");
		}

		if (stack.length > stackDepth) {
			output.append("\t... ").append(stack.length - stackDepth)
				.append(" more\n");
		}
	}

	private static String formatStackElement(StackTraceElement element) {
		return reduceClassName(element.getClassName()) +
			"#" + element.getMethodName() +
			":" + element.getLineNumber();
	}

	private static String reduceClassName(String className) {
		String[] parts = className.split("\\.");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < parts.length - 1; i++) {
			sb.append(parts[i].charAt(0)).append('.');
		}
		sb.append(parts[parts.length - 1]);
		return sb.toString();
	}

	private static class ForwardCounter {
		private int value = 0;
	}
}
