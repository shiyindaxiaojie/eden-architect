package org.ylzl.eden.spring.framework.cola.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 响应（多条记录）
 *
 * @author Frank Zhang
 * @author gyl
 * @since 2.4.x
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MultiResponse<T> extends Response {

	private static final long serialVersionUID = 1L;

	private Collection<T> data;

	public List<T> getData() {
		return null == data ? Collections.emptyList() : new ArrayList<>(data);
	}

	public void setData(Collection<T> data) {
		this.data = data;
	}

	public boolean isEmpty() {
		return data == null || data.isEmpty();
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}

	public static MultiResponse buildSuccess() {
		MultiResponse response = new MultiResponse();
		response.setSuccess(true);
		return response;
	}

	public static MultiResponse buildFailure(String errCode, String errMessage) {
		MultiResponse response = new MultiResponse();
		response.setSuccess(false);
		response.setErrCode(errCode);
		response.setErrMessage(errMessage);
		return response;
	}

	public static <T> MultiResponse<T> of(Collection<T> data) {
		MultiResponse<T> response = new MultiResponse<>();
		response.setSuccess(true);
		response.setData(data);
		return response;
	}
}
