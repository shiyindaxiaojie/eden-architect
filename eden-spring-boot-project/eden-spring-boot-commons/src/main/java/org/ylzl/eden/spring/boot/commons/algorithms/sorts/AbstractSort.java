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

package org.ylzl.eden.spring.boot.commons.algorithms.sorts;

import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * 排序算法抽象类
 *
 * @author gyl
 * @since 0.0.1
 */
public abstract class AbstractSort implements Sort {

  /**
   * 排序列表
   *
   * @param unsorted - 未排序的列表
   * @return 排序后的列表
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T extends Comparable<T>> List<T> sort(@NonNull List<T> unsorted) {
    return Arrays.asList(sort(unsorted.toArray((T[]) new Comparable[unsorted.size()])));
  }
}
