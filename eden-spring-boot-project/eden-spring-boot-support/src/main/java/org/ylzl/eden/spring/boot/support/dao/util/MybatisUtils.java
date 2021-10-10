package org.ylzl.eden.spring.boot.support.dao.util;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.ylzl.eden.spring.boot.commons.lang.StringConstants;
import org.ylzl.eden.spring.boot.commons.safe.SqlSafeUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Mybatis 工具集
 *
 * @author gyl
 * @since 0.0.1
 */
@UtilityClass
public class MybatisUtils {

	/**
	 * 追加 Like %
	 *
	 * @param column 字段
	 * @return
	 */
	public static String appendLikeSuffix(String column) {
		return StringUtils.join(column, "%");
	}

	/**
	 * 追加 23:59:59
	 *
	 * @param column 字段
	 * @return
	 */
	public static String appendEndTimeSuffix(String column) {
		return StringUtils.join(column, " 23:59:59");
	}

	/**
	 * 追加 00:00:00
	 *
	 * @param column 字段
	 * @return
	 */
	public static String appendBeginTimeSuffix(String column) {
		return StringUtils.join(column, " 00:00:00");
	}

	/**
	 * 构建 Page 对象
	 *
	 * @param pageNum 页号
	 * @param pageSize 行数
	 * @param sortColumn 排序字段
	 * @param sortRule 排序规则
	 * @return
	 */
	public static <T> Page<T> buildPage(
		long pageNum, long pageSize, String sortColumn, String sortRule) {
		Page<T> page = new Page(pageNum, pageSize);
		if (StringUtils.isNotBlank(sortColumn) && StringUtils.isNotBlank(sortRule)) {
			String[] sortColumns = sortColumn.split(StringConstants.COLON);
			List<OrderItem> orderItems = SortRuleEnum.ASC.name().equalsIgnoreCase(sortRule)
					? OrderItem.ascs(sortColumns)
					: OrderItem.descs(sortColumns);
			page.addOrder(orderItems);
		}
		return page;
	}

	/**
	 * 获取表名
	 *
	 * @param clazz domain
	 * @return
	 */
	public static String getTableName(Class<?> clazz) {
		TableName tableName = clazz.getAnnotation(TableName.class);
		return tableName.value();
	}

	/**
	 * 获取排序 SQL
	 *
	 * @param sortColumn 排序字段
	 * @param sortRule 排序规则（asc/desc）
	 * @return
	 */
	public static String getOrderBySQL(String sortColumn, String sortRule) {
		checkSQLInjection(sortColumn);
		checkSQLInjection(sortRule);
		StringBuilder sql = new StringBuilder();
		if (StringUtils.isNotBlank(sortColumn) && StringUtils.isNotBlank(sortRule)) {
			sql.append(" ORDER BY ");
			String rule =
				Arrays.stream(SortRuleEnum.values())
					.filter(sortRuleEnum -> sortRuleEnum.name().equalsIgnoreCase(sortRule)).findFirst().get().name();
			String[] sortColumns = sortColumn.split(StringConstants.COLON);
			int len = sortColumns.length;
			int i = 0;
			for (String column : sortColumns) {
				sql.append(column).append(StringConstants.SPACE).append(rule);
				i++;
				if (i < len) {
					sql.append(StringConstants.COMMA);
				}
			}
		}
		return sql.toString();
	}

	/**
	 * 获取分页 SQL
	 *
	 * @param pageNum 页号
	 * @param pageSize 行数
	 * @return
	 */
	public static String getLimitSQL(long pageNum, long pageSize) {
		return " LIMIT " + ((pageNum - 1) * pageSize) + ", " + pageSize;
	}

	/**
	 * 检查是否有 SQL 注入
	 *
	 * @param str 输入
	 */
	private void checkSQLInjection(String str) {
		if (!SqlSafeUtils.isSQLInjectionSafe(str)) {
			throw new RuntimeException("检测到 SQL 注入：" + str);
		}
	}

	/**
	 * 受影响行数转换成布尔值
	 *
	 * @param effectiveRows
	 * @return
	 */
	public static boolean effectiveRowsToBoolean(int effectiveRows) {
		return effectiveRows > ApplicationConstants.DEFAULT_SUCCESS_VALUE;
	}
}
