package org.ylzl.eden.spring.framework.cola.exception;

/**
 * 客户端异常
 *
 * @author gyl
 * @since 2.4.x
 */
public class ClientException extends BaseException {

	public ClientException(ClientErrorType clientErrorType) {
		super(clientErrorType.getErrCode(), clientErrorType.getErrMessage(), clientErrorType.getHttpStatusCode());
	}

	public ClientException(String errCode, String errMessage, int httpStatusCode) {
		super(errCode, errMessage, httpStatusCode);
	}
}
