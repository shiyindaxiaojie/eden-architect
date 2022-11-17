package org.ylzl.eden.idempotent.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ylzl.eden.idempotent.strategy.TokenIdempotentStrategy;
import org.ylzl.eden.spring.framework.web.extension.ResponseBuilder;

/**
 * 幂等请求令牌控制器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
@RestController
public class IdempotentTokenController {

	private final TokenIdempotentStrategy strategy;

	@GetMapping("/token")
	public Object generateToken() {
		return ResponseBuilder.builder().buildSuccess(strategy.generateToken());
	}
}
