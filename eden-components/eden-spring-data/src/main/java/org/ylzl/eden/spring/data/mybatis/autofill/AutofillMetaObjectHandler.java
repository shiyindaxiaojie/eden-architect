package org.ylzl.eden.spring.data.mybatis.autofill;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 自动填充元数据对象处理器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class AutofillMetaObjectHandler implements MetaObjectHandler {

	private final String createDateFieldName;

	private final String lastModifiedDateFieldName;

	/**
	 * 插入元对象字段填充（用于插入时对公共字段的填充）
	 *
	 * @param metaObject 元对象
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		LocalDateTime now = LocalDateTime.now();
		this.strictInsertFill(metaObject, createDateFieldName, LocalDateTime.class, now);
		this.strictInsertFill(metaObject, lastModifiedDateFieldName, LocalDateTime.class, now);
	}

	/**
	 * 更新元对象字段填充（用于更新时对公共字段的填充）
	 *
	 * @param metaObject 元对象
	 */
	@Override
	public void updateFill(MetaObject metaObject) {
		LocalDateTime now = LocalDateTime.now();
		this.strictInsertFill(metaObject, lastModifiedDateFieldName, LocalDateTime.class, now);
	}
}
