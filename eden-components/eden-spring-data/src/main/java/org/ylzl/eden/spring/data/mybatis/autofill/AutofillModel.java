package org.ylzl.eden.spring.data.mybatis.autofill;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 自动填充模型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AutofillModel<T extends Model<?>> extends Model<T> implements Serializable {

	/**
	 * 创建时间
	 */
	@TableField(value = "create_date", fill = FieldFill.INSERT)
	private LocalDateTime createDate;

	/**
	 * 最后修改时间
	 */
	@TableField(value = "last_modified_date", fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime lastModifiedDate;
}
