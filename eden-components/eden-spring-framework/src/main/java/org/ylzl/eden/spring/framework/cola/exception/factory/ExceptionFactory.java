package org.ylzl.eden.spring.framework.cola.exception.factory;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.spring.framework.cola.exception.*;

/**
 * 异常工厂
 *
 * @author gyl
 * @since 2.4.x
 */
@UtilityClass
public class ExceptionFactory {

	public static ClientException clientException(ClientErrorType clientErrorType) {
		return new ClientException(clientErrorType);
	}

	public static ServerException serverException(ServerErrorType serverErrorType) {
		return new ServerException(serverErrorType);
	}

	public static ThirdServiceException thirdServiceException(ThirdServiceErrorType thirdServiceErrorType) {
		return new ThirdServiceException(thirdServiceErrorType);
	}
}
