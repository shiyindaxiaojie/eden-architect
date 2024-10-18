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

package org.ylzl.eden.data.filter.integration.masking.charsscan;

import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.chars.scan.util.InnerCharsScanPropertyBuilder;
import lombok.RequiredArgsConstructor;
import org.ylzl.eden.data.filter.MaskingFilter;
import org.ylzl.eden.data.filter.builder.AbstractMaskingFilterBuilder;
import org.ylzl.eden.data.filter.builder.MaskingFilterBuilder;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * CharsScan 组件构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class CharsScanFilterBuilder extends AbstractMaskingFilterBuilder implements MaskingFilterBuilder {

	private CharsScanBs charsScanBs;

	private static final AtomicBoolean BUILD_STATE = new AtomicBoolean(false);

	@Override
	public MaskingFilter build() {
		if (BUILD_STATE.compareAndSet(false, true)) {
			charsScanBs = InnerCharsScanPropertyBuilder.buildCharsScanBs(
				getConfig().getCharsScan().getPrefix(),
				getConfig().getCharsScan().getScanList(),
				getConfig().getCharsScan().getReplaceList(),
				getConfig().getCharsScan().getDefaultReplace(),
				getConfig().getCharsScan().getReplaceHash(),
				getConfig().getCharsScan().getWhiteList()
			);
		}
		return new CharsScanFilter(charsScanBs);
	}
}
