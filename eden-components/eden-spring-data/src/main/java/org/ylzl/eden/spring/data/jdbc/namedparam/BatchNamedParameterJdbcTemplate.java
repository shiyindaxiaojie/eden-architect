/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.data.jdbc.namedparam;

import lombok.NonNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.namedparam.*;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.reflect.ReflectionUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * BatchNamedParameterJdbcTemplate
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class BatchNamedParameterJdbcTemplate extends NamedParameterJdbcTemplate {

	private static final int[] EMPTY_EFFECTIVE_ROWS = {};

	private static final String BPS_INSERT_PATTERN = "INSERT INTO {0} ({1}) VALUES ({2})";

	private static final String BPS_UPDATE_PATTERN = "UPDATE {0} SET {1} WHERE {2} = {3}";

	private static final String BPS_DELETE_PATTERN = "DELETE FROM {0} WHERE {1} = {2}";

	public BatchNamedParameterJdbcTemplate(DataSource dataSource) {
		super(dataSource);
	}

	public <T> int[] batchInsert(@NonNull final Collection<T> datas, final int executeBatchSize) {
		if (datas.isEmpty()) {
			return EMPTY_EFFECTIVE_ROWS;
		}

		T data = getClass(datas);
		String tableName = this.getTableName(data.getClass());

		final List<String> columns = new ArrayList<>();
		final List<String> namedColumns = new ArrayList<>();

		List<Field> fields = ReflectionUtils.getDeclaredFields(data.getClass());
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				if (ObjectUtils.isNull(column) || !column.insertable()) {
					continue;
				}

				columns.add(column.name());
				namedColumns.add(StringUtils.join(Strings.COLON, field.getName()));
			}
		}

		String sql =
			MessageFormat.format(
				BPS_INSERT_PATTERN,
				tableName,
				StringUtils.join(columns, Strings.COMMA),
				StringUtils.join(namedColumns, Strings.COMMA));
		return this.batchUpdate(sql, datas, executeBatchSize);
	}

	public <T> int[] batchUpdate(@NonNull final Collection<T> datas, final int executeBatchSize) {
		if (datas.isEmpty()) {
			return EMPTY_EFFECTIVE_ROWS;
		}

		T data = getClass(datas);
		String tableName = this.getTableName(data.getClass());

		final List<String> columnAndNamedColumns = new ArrayList<>();
		String pkColumnName = null;
		String pkNamedColumn = null;

		List<Field> fields = ReflectionUtils.getDeclaredFields(data.getClass());
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				if (ObjectUtils.isNull(column)) {
					continue;
				}

				if (field.isAnnotationPresent(Id.class)) {
					pkColumnName = column.name();
					pkNamedColumn = field.getName();
					continue;
				}

				if (!column.updatable()) {
					continue;
				}
				columnAndNamedColumns.add(
					StringUtils.join(
						column.name(), Strings.EQ, Strings.COLON, field.getName()));
			}
		}

		String sql =
			MessageFormat.format(
				BPS_UPDATE_PATTERN,
				tableName,
				StringUtils.join(columnAndNamedColumns, Strings.COMMA),
				pkColumnName,
				StringUtils.join(Strings.COLON, pkNamedColumn));
		return this.batchUpdate(sql, datas, executeBatchSize);
	}

	public <T> int[] batchDelete(@NonNull final Collection<T> datas, final int executeBatchSize) {
		if (datas.isEmpty()) {
			return EMPTY_EFFECTIVE_ROWS;
		}

		T data = getClass(datas);
		String tableName = this.getTableName(data.getClass());

		String pkColumnName = null;
		String pkNamedColumn = null;

		List<Field> fields = ReflectionUtils.getDeclaredFields(data.getClass());
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				if (ObjectUtils.isNull(column)) {
					continue;
				}

				if (field.isAnnotationPresent(Id.class)) {
					pkColumnName = column.name();
					pkNamedColumn = field.getName();
					break;
				}
			}
		}

		String sql =
			MessageFormat.format(
				BPS_DELETE_PATTERN,
				tableName,
				pkColumnName,
				StringUtils.join(Strings.COLON, pkNamedColumn));
		return this.batchUpdate(sql, datas, executeBatchSize);
	}

	public <T> int[] batchUpdate(
		@NonNull final String sql, @NonNull final Collection<T> datas, final int executeBatchSize) {
		if (datas.isEmpty()) {
			return EMPTY_EFFECTIVE_ROWS;
		}

		final ParsedSql parsedSql = getParsedSql(sql);
		final SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(datas.toArray());
		final String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, batchArgs[0]);
		return getJdbcOperations()
			.batchUpdate(
				sqlToUse,
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						Object[] values =
							NamedParameterUtils.buildValueArray(parsedSql, batchArgs[i], null);
						int[] columnTypes = NamedParameterUtils.buildSqlTypeArray(parsedSql, batchArgs[i]);
						setStatementParameters(values, ps, columnTypes);
						if (executeBatchSize != 0 && i % executeBatchSize == 0) {
							ps.executeBatch();
						}
					}

					@Override
					public int getBatchSize() {
						return batchArgs.length;
					}
				});
	}

	private <T> T getClass(@NonNull Collection<T> datas) {
		return datas.iterator().next();
	}

	private <T> String getTableName(Class<T> clazz) {
		if (!clazz.isAnnotationPresent(Table.class)) {
			throw new RuntimeException("java.persistence.@Table not found");
		}

		Table table = clazz.getAnnotation(Table.class);
		if (ObjectUtils.isNull(table)) {
			throw new RuntimeException("java.persistence.@Table's value must be given");
		}
		return table.name();
	}

	private void setStatementParameters(Object[] values, PreparedStatement ps, int[] columnTypes)
		throws SQLException {
		int paramIndex = 0;
		for (Object value : values) {
			paramIndex++;
			if (value instanceof SqlParameterValue) {
				SqlParameterValue paramValue = (SqlParameterValue) value;
				StatementCreatorUtils.setParameterValue(ps, paramIndex, paramValue, paramValue.getValue());
			} else {
				int colType;
				if (columnTypes == null || columnTypes.length < paramIndex) {
					colType = SqlTypeValue.TYPE_UNKNOWN;
				} else {
					colType = columnTypes[paramIndex - 1];
				}
				StatementCreatorUtils.setParameterValue(ps, paramIndex, colType, value);
			}
		}
	}
}
