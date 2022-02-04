package org.ylzl.eden.spring.framework.cola.exception;

/**
 * 第三方服务异常
 *
 * @author gyl
 * @since 2.4.x
 */
public class ThirdServiceException extends BaseException {

	public ThirdServiceException(ThirdServiceErrorType thirdServiceErrorType) {
		super(thirdServiceErrorType.getErrCode(), thirdServiceErrorType.getErrMessage(), thirdServiceErrorType.getHttpStatusCode());
	}

	public ThirdServiceException(String errCode, String errMessage, int httpStatusCode) {
		super(errCode, errMessage, httpStatusCode);
	}
}
