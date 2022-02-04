package org.ylzl.eden.spring.framework.cola.exception;

/**
 * 服务端异常
 *
 * @author gyl
 * @since 2.4.x
 */
public class ServerException extends BaseException {

	public ServerException(ServerErrorType serverErrorType) {
		super(serverErrorType.getErrCode(), serverErrorType.getErrMessage(), serverErrorType.getHttpStatusCode());
	}

	public ServerException(String errCode, String errMessage, int httpStatusCode) {
		super(errCode, errMessage, httpStatusCode);
	}
}
