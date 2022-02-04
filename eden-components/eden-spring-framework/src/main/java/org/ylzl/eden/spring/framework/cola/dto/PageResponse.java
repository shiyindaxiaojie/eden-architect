package org.ylzl.eden.spring.framework.cola.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 响应（分页查询结果集）
 *
 * @author Frank Zhang
 * @author gyl
 * @since 2.4.x
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PageResponse<T> extends Response {

	private static final long serialVersionUID = 1L;

	private int totalCount = 0;

	private int pageSize = 1;

	private int pageIndex = 1;

	private Collection<T> data;

	public void setPageSize(int pageSize) {
		this.pageSize = Math.max(pageSize, 1);
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = Math.max(pageIndex, 1);
	}

	public void setData(Collection<T> data) {
		this.data = null == data ? Collections.emptyList() : new ArrayList<>(data);
	}

	public int getTotalPages() {
		return this.totalCount % this.pageSize == 0 ? this.totalCount / this.pageSize : (this.totalCount / this.pageSize) + 1;
	}

	public boolean isEmpty() {
		return data == null || data.isEmpty();
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}

	public static PageResponse buildSuccess() {
		PageResponse response = new PageResponse();
		response.setSuccess(true);
		return response;
	}

	public static PageResponse buildFailure(String errCode, String errMessage) {
		PageResponse response = new PageResponse();
		response.setSuccess(false);
		response.setErrCode(errCode);
		response.setErrMessage(errMessage);
		return response;
	}

	public static <T> PageResponse<T> of(int pageSize, int pageIndex) {
		PageResponse<T> response = new PageResponse<>();
		response.setSuccess(true);
		response.setData(Collections.emptyList());
		response.setTotalCount(0);
		response.setPageSize(pageSize);
		response.setPageIndex(pageIndex);
		return response;
	}

	public static <T> PageResponse<T> of(Collection<T> data, int totalCount, int pageSize, int pageIndex) {
		PageResponse<T> response = new PageResponse<>();
		response.setSuccess(true);
		response.setData(data);
		response.setTotalCount(totalCount);
		response.setPageSize(pageSize);
		response.setPageIndex(pageIndex);
		return response;
	}
}
