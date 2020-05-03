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

package org.ylzl.eden.spring.boot.support.repository.excel;

import org.springframework.beans.factory.annotation.Autowired;
import org.ylzl.eden.spring.boot.integration.easyexcel.event.AnalysisEventListenerAdapter;
import org.ylzl.eden.spring.boot.integration.easyexcel.read.EasyExcelReader;

import java.io.InputStream;
import java.util.List;

/**
 * Excel 数据读取适配器
 *
 * @author gyl
 * @since 0.0.1
 */
@SuppressWarnings("unchecked")
public abstract class ExcelReaderAdpater<T> implements ExcelReader<T> {

  private static final String EXP_NONE_IMPL = "未配置 Excel 数据读取器";

  @Autowired(required = false)
  private EasyExcelReader easyExcelReader;

  @Override
  public void read(InputStream inputStream, int batchSize, int... sheetIndexs) {
    if (easyExcelReader != null) {
      final ExcelReaderAdpater adpater = this;
      easyExcelReader.read(
          inputStream,
          new AnalysisEventListenerAdapter<T>(batchSize) {

            @Override
            public void readCallback(List<T> datas) {
              adpater.readCallback(datas);
            }
          },
          sheetIndexs);
      return;
    }

    throw new UnsupportedOperationException(EXP_NONE_IMPL);
  }
}
