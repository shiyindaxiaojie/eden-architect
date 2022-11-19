package org.ylzl.eden.cola.extension.executor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.cola.extension.ExtensionRepository;

/**
 * 扩展点执行器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class ExtensionExecutor {

	private static final String EXTENSION_NOT_FOUND = "extension_not_found";

	private final ExtensionRepository extensionRepository;


}
