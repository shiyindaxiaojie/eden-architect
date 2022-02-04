package org.ylzl.eden.spring.framework.cola.exception;

import org.springframework.http.HttpStatus;

/**
 * 第三方服务断言
 *
 * <ul> 参考《阿里巴巴Java开发手册》错误码
 * <li>A____：表示错误来自于用户</li>
 * <li>B____：表示错误来自于当前系统</li>
 * <li>C____：表示错误来自于第三方服务</li>
 * </ul>
 *
 * @see ClientErrorType
 * @see ServerErrorType
 * @author gyl
 * @since 2.4.x
 */
public enum ThirdServiceErrorType implements ErrorAssert {

	C0001("调用第三方服务出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0100("中间件服务出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0110("RPC 服务出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0111("RPC 服务未找到", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0112("RPC 服务未注册", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0113("接口不存在", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0120("消息服务出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0121("消息投递出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0122("消息消费出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0123("消息订阅出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0124("消息分组未查到", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0130("缓存服务出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0131("key 长度超过限制", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0132("value 长度超过限制", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0133("存储容量已满", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0134("不支持的数据格式", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0140("配置服务出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0150("网络资源服务出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0151("VPN 服务出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0152("CDN 服务出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0153("域名解析服务出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0154("网关服务出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),

	C0200("第三方系统执行超时", HttpStatus.GATEWAY_TIMEOUT.value()),
	C0210("RPC 执行超时", HttpStatus.GATEWAY_TIMEOUT.value()),
	C0220("消息投递超时", HttpStatus.GATEWAY_TIMEOUT.value()),
	C0230("缓存服务超时", HttpStatus.GATEWAY_TIMEOUT.value()),
	C0240("配置服务超时", HttpStatus.GATEWAY_TIMEOUT.value()),
	C0250("数据库服务超时", HttpStatus.GATEWAY_TIMEOUT.value()),

	C0300("数据库服务出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0311("表不存在", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0312("列不存在", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0321("多表关联中存在多个相同名称的列", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0331("数据库死锁", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0341("主键冲突", HttpStatus.INTERNAL_SERVER_ERROR.value()),

	C0400("第三方容灾系统被触发", HttpStatus.TOO_MANY_REQUESTS.value()),
	C0401("第三方系统限流", HttpStatus.TOO_MANY_REQUESTS.value()),
	C0402("第三方功能降级", HttpStatus.TOO_MANY_REQUESTS.value()),

	C0500("通知服务出错", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0501("短信提醒服务失败", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0502("语音提醒服务失败", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	C0503("邮件提醒服务失败", HttpStatus.INTERNAL_SERVER_ERROR.value());

	private final String errCode;

	private final String errMessage;

	private final int httpStatusCode;

	ThirdServiceErrorType(String errMessage, int httpStatusCode) {
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
		throw new ThirdServiceException(this);
	}
}

