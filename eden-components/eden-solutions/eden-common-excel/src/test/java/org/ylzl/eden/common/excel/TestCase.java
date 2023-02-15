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

package org.ylzl.eden.common.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;
import org.ylzl.eden.common.excel.integration.easyexcel.converter.SexConverter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 测试用例
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TestCase {

	@NotBlank(message = "用户名不能为空")
	@ExcelProperty("用户名")
	private String username;

	@NotBlank(message = "中文名不能为空")
	@ExcelProperty("中文名")
	private String chineseName;

	@Min(value = 0, message = "性别错误")
	@Max(value = 1, message = "性别错误")
	@ExcelProperty(value = "性别", converter = SexConverter.class)
	private Integer sex;

	@ExcelProperty("创建时间")
	private LocalDateTime createdDate;
}
