package org.ylzl.eden.common.excel.integration.fastexcel.i18n;

import cn.idev.excel.metadata.Head;
import cn.idev.excel.write.handler.CellWriteHandler;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.metadata.holder.WriteTableHolder;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.PropertyPlaceholderHelper;
import org.ylzl.eden.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 单元格写入国际化处理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class I18nCellWriteHandler implements CellWriteHandler {

	private final PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("{", "}");

	private final MessageSource messageSource;

	private final PropertyPlaceholderHelper.PlaceholderResolver placeholderResolver;

	public I18nCellWriteHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
		this.placeholderResolver = placeholderName -> this.messageSource.getMessage(placeholderName, null,
			LocaleContextHolder.getLocale());
	}

	@Override
	public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
								 Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
		if (isHead) {
			List<String> headNameList = head.getHeadNameList();
			if (CollectionUtils.isNotEmpty(headNameList)) {
				List<String> i18nHeadNames = headNameList.stream()
					.map(headName -> propertyPlaceholderHelper.replacePlaceholders(headName, placeholderResolver))
					.collect(Collectors.toList());
				head.setHeadNameList(i18nHeadNames);
			}
		}
	}
}
