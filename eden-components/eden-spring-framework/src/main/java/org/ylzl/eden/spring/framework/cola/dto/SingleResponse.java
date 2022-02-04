package org.ylzl.eden.spring.framework.cola.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 响应（单条记录）
 *
 * @author Frank Zhang
 * @author gyl
 * @since 2.4.x
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SingleResponse<T> extends Response {

	private static final long serialVersionUID = 1L;

	private T data;

	public static SingleResponse buildSuccess() {
		SingleResponse response = new SingleResponse();
		response.setSuccess(true);
		return response;
	}

	public static SingleResponse buildFailure(String errCode, String errMessage) {
		SingleResponse response = new SingleResponse();
		response.setSuccess(false);
		response.setErrCode(errCode);
		response.setErrMessage(errMessage);
		return response;
	}

	public static <T> SingleResponse<T> of(T data) {
		SingleResponse<T> response = new SingleResponse<>();
		response.setSuccess(true);
		response.setData(data);
		return response;
	}
}
