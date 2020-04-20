/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.boot.integration.easyexcel;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.integration.core.IntegrationConstants;
import org.ylzl.eden.spring.boot.integration.easyexcel.read.EasyExcelReader;
import org.ylzl.eden.spring.boot.integration.easyexcel.write.EasyExcelWriter;

/**
 * EasyExcel 配置
 *
 * @author gyl
 * @since 1.0.0
 */
@ConditionalOnClass(EasyExcel.class)
@ConditionalOnExpression(EasyExcelAutoConfiguration.EXPS_EASY_EXCEL_ENABLED)
@Slf4j
@Configuration
public class EasyExcelAutoConfiguration {

	public static final String EXPS_EASY_EXCEL_ENABLED = "${" + IntegrationConstants.PROP_PREFIX + ".easy-excel.enabled:true}";

    @ConditionalOnMissingBean
    @Bean
    public EasyExcelReader easyExcelReader() {
        return new EasyExcelReader();
    }

    @ConditionalOnMissingBean
    @Bean
    public EasyExcelWriter easyExcelWriter() {
        return new EasyExcelWriter();
    }
}
