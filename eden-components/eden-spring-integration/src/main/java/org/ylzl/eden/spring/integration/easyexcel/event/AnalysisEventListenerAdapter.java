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

package org.ylzl.eden.spring.integration.easyexcel.event;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellData;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 读取数据分析事件监听适配器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public abstract class AnalysisEventListenerAdapter<T> extends AnalysisEventListener<T> {

	/**
	 * 批处理大小
	 */
	private int batchSize = 1000;

	/**
	 * 发生异常时是否中断
	 */
	private boolean isStopOnException = true;

	private List<T> datas = new ArrayList<>();

	public AnalysisEventListenerAdapter() {
	}

	public AnalysisEventListenerAdapter(int batchSize) {
		this.batchSize = batchSize;
	}

	public AnalysisEventListenerAdapter(int batchSize, boolean isStopOnException) {
		this.batchSize = batchSize;
		this.isStopOnException = isStopOnException;
	}

	@Override
	public void invoke(T data, AnalysisContext analysisContext) {
		datas.add(data);

		if (datas.size() >= batchSize) {
			readCallback(datas);
			datas.clear();
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		readCallback(datas);
		datas.clear();
	}

	@Override
	public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
		super.invokeHead(headMap, context);
	}

	@Override
	public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
		super.invokeHeadMap(headMap, context);
	}

	@Override
	public void onException(Exception exception, AnalysisContext context) throws Exception {
		log.error("读取 Excel 的数据发生异常：{}", exception.getMessage(), exception);
		if (exception instanceof ExcelDataConvertException) {
			ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
			log.error(
				"读取 Excel 发生异常的数据为第 {} 行，第 {} 列，内容为：{}",
				excelDataConvertException.getRowIndex(),
				excelDataConvertException.getColumnIndex(),
				excelDataConvertException.getCellData());
		}

		if (isStopOnException) {
			super.onException(exception, context);
		}
	}

	@Override
	public boolean hasNext(AnalysisContext context) {
		return super.hasNext(context);
	}

	public abstract void readCallback(List<T> datas);
}
