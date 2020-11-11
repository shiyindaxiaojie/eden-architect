package org.ylzl.eden.spring.boot.framework.web.rest.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误标准化枚举
 *
 * @author gyl
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor
public enum ErrorEnum implements ErrorAssert {

	INTERNAL_SERVER_ERROR("Internal server error!"),
	CONCURRENCY_FAILURE("Concurrency failed!"),
	INVALID_CREDENTIALS("Invalid credentials!"),
	BAD_REQUEST_ALERT("Bad request!"),
	FORBIDDEN("Forbidden!"),
	UNAUTHORIZED("Unauthorized!"),
	METHOD_NOT_SUPPORTED("Method not supported!"),
	METHOD_ARGUMENT_NOT_VALID("Method argument not valid!"),
	ENTITY_NOT_FOUND("Entity not found!"),
	INVALID_PRIMARY_KEY("Invalid primary key!");

	private String message;

	@Override
	public String toString() {
		return message;
	}
}
