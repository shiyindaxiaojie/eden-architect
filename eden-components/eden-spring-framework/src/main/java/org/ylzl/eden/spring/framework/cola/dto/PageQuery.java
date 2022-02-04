package org.ylzl.eden.spring.framework.cola.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 分页查询
 *
 * @author Frank Zhang
 * @author gyl
 * @since 2.4.x
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public abstract class PageQuery extends Query {

	private static final long serialVersionUID = 1L;

	public static final String ASC = "ASC";

	public static final String DESC = "DESC";

	private static final int DEFAULT_PAGE_INDEX = 1;

	private static final int DEFAULT_PAGE_SIZE = 10;

	private int pageIndex = DEFAULT_PAGE_INDEX;

	private int pageSize = DEFAULT_PAGE_SIZE;

	private String orderBy;

	private String orderDirection = DESC;

	private String groupBy;

	private boolean needTotalCount = true;

	public int getOffset() {
		return (getPageIndex() - 1) * getPageSize();
	}

	public void setPageIndex() {
		this.pageIndex = Math.max(pageIndex, DEFAULT_PAGE_INDEX);
	}

	public void setPageSize() {
		this.pageSize = Math.max(pageSize, DEFAULT_PAGE_SIZE);
	}

	public void setOrderDirection(String orderDirection) {
		if (ASC.equalsIgnoreCase(orderDirection) || DESC.equalsIgnoreCase(orderDirection)) {
			this.orderDirection = orderDirection;
		}
	}
}

