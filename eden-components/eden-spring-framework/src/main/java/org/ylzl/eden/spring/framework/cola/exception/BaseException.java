package org.ylzl.eden.spring.framework.cola.exception;

import lombok.Getter;
import lombok.Setter;
import org.ylzl.eden.spring.framework.cola.dto.Response;

/**
 * 异常抽象
 *
 * @author gyl
 * @since 2.4.x
 */
@Setter
@Getter
public class BaseException extends RuntimeException {

	private String errCode;

	private String errMessage;

	private int httpStatusCode;

	public BaseException(String errCode, String errMessage, int httpStatusCode) {
		super(errMessage);
		this.errCode = errCode;
		this.errMessage = errMessage;
		this.httpStatusCode = httpStatusCode;
	}

	public Response getResponse() {
		return Response.buildFailure(errCode, errMessage);
	}
}
