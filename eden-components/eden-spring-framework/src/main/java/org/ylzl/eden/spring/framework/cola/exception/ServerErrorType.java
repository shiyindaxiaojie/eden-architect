package org.ylzl.eden.spring.framework.cola.exception;

import org.springframework.http.HttpStatus;

/**
 * 服务端断言
 *
 * <ul> 参考《阿里巴巴Java开发手册》错误码
 * <li>A____：表示错误来自于用户</li>
 * <li>B____：表示错误来自于当前系统</li>
 * <li>C____：表示错误来自于第三方服务</li>
 * </ul>
 *
 * @see ClientErrorType
 * @see ThirdServiceErrorType
 * @author gyl
 * @since 2.4.x
 */
public enum ServerErrorType implements ErrorAssert {

	B0001("系统执行出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	B0100("系统执行超时", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	B0101("系统订单处理超时", HttpStatus.INTERNAL_SERVER_ERROR.value()),

	B0200("系统容灾功能被触发", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	B0210("系统限流", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	B0220("系统功能降级", HttpStatus.INTERNAL_SERVER_ERROR.value()),

	B0300("系统资源异常", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	B0310("系统资源耗尽", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	B0311("系统磁盘空间耗尽", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	B0312("系统内存耗尽", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	B0313("文件句柄耗尽", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	B0314("系统连接池耗尽", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	B0315("系统线程池耗尽", HttpStatus.INTERNAL_SERVER_ERROR.value()),

	B0320("系统资源访问异常", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	B0321("系统读取磁盘文件失败", HttpStatus.INTERNAL_SERVER_ERROR.value());

	private final String errCode;

	private final String errMessage;

	private final int httpStatusCode;

	ServerErrorType(String errMessage, int httpStatusCode) {
		this.errCode = this.name();
		this.errMessage = errMessage;
		this.httpStatusCode = httpStatusCode;
	}

	@Override
	public String getErrCode() {
		return errCode;
	}

	@Override
	public String getErrMessage() {
		return errMessage;
	}

	@Override
	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	@Override
	public String toString() {
		return errMessage;
	}

	@Override
	public void throwNewException(Throwable e) {
		throw new ServerException(this);
	}
}
