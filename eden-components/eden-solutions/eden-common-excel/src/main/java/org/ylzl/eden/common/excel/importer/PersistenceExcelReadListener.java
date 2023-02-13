package org.ylzl.eden.common.excel.importer;

import com.alibaba.excel.util.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.common.excel.ExcelLine;
import org.ylzl.eden.common.excel.model.ValidationErrors;
import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.commons.validation.ValidatorUtils;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Excel 读取事件监听持久化
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public abstract class PersistenceExcelReadListener<T> implements ExcelReadListener<T> {

	/**
	 * 批次处理大小
	 */
	private int batchSize = 100;

	/**
	 * 缓存读取的数据
	 */
	private List<T> cache = ListUtils.newArrayListWithExpectedSize(batchSize);

	/**
	 * 读取过程中产生的错误信息
	 */
	private final List<ValidationErrors> errors = new ArrayList<>();

	/**
	 * 当前读取所在的行数
	 */
	private int rowNumber = 1;

	/**
	 * 每读取一行调用一次
	 *
	 * @param data    读取到的数据
	 * @param context 读取上下文
	 */
	@Override
	public void read(Object data, ExcelReadContext context) {
		rowNumber++;

		Set<ConstraintViolation<Object>> violations = ValidatorUtils.validate(data);
		if (CollectionUtils.isNotEmpty(violations)) {
			Set<String> messageSet = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
			errors.add(new ValidationErrors(rowNumber, messageSet));
			return;
		}

		Field[] fields = data.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(ExcelLine.class) && field.getType() == Integer.class) {
				try {
					field.setAccessible(true);
					field.set(data, rowNumber);
				} catch (IllegalAccessException e) {
					log.error(e.getMessage(), e);
					return;
				}
			}
		}

		cache.add((T) data);
		if (cache.size() >= batchSize) {
			batchSaveData(cache);
			cache.clear();
			cache = ListUtils.newArrayListWithExpectedSize(batchSize);
		}

	}

	/**
	 * 获取所有校验错误信息
	 *
	 * @return 错误信息清单
	 */
	@Override
	public List<ValidationErrors> getErrors() {
		return errors;
	}

	/**
	 * 根据批次大小读取相应的数据
	 *
	 * @return 数据
	 */
	@Override
	public List<T> getBatchData() {
		return cache;
	}

	/**
	 * 设置批次大小
	 *
	 * @param batchSize 批次大小
	 */
	@Override
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
		this.cache = ListUtils.newArrayListWithExpectedSize(batchSize);
	}

	/**
	 * 分批保存数据
	 *
	 * @param data 缓存的数据
	 */
	public abstract void batchSaveData(List<T> data);
}
