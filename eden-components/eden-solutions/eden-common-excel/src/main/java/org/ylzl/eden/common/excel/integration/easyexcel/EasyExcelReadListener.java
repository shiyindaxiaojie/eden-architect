package org.ylzl.eden.common.excel.integration.easyexcel;

import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.common.excel.ExcelLine;
import org.ylzl.eden.common.excel.importer.ExcelReadContext;
import org.ylzl.eden.common.excel.importer.ExcelReadListener;
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
 * EasyExcel 读取事件监听
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class EasyExcelReadListener implements ExcelReadListener<Object> {

	/**
	 * 保存读取的数据
	 */
	private final List<Object> datas = new ArrayList<>();

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
		} else {
			Field[] fields = data.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(ExcelLine.class) && field.getType() == Integer.class) {
					try {
						field.setAccessible(true);
						field.set(data, rowNumber);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			datas.add(data);
		}
	}

	/**
	 * 获取读取的数据
	 *
	 * @return 数据
	 */
	@Override
	public List<Object> getDatas() {
		return datas;
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
}
