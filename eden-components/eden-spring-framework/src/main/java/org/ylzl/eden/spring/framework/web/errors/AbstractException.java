package org.ylzl.eden.spring.framework.web.errors;

import com.alibaba.cola.dto.Response;

/**
 * 异常抽象
 *
 * @author gyl
 * @since 2.4.x
 */
public abstract class AbstractException extends RuntimeException {

	private final String errCode;

	private final String errMessage;

	public AbstractException(ErrorEnum errorEnum) {
		super(errorEnum.getErrMessage());
		this.errCode = errorEnum.getErrCode();
		this.errMessage = errorEnum.getErrMessage();
	}

	public AbstractException(String errCode, String errMessage) {
		super(errMessage);
		this.errCode = errCode;
		this.errMessage = errMessage;
	}

	public Response getError() {
		return Response.buildFailure(errCode, errMessage);
	}

	public abstract int getStatusCode();
}
