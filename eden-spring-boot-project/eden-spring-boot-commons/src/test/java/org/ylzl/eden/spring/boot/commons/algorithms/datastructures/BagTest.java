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

package org.ylzl.eden.spring.boot.commons.algorithms.datastructures;

import org.junit.Test;
import org.ylzl.eden.spring.boot.commons.algorithms.datastructures.bags.Bag;

import static org.junit.Assert.*;

/**
 * 背包测试
 *
 * @author gyl
 * @since 0.0.1
 */
public class BagTest {

  @Test
  public void assertThatBag() {
    Bag<Integer> bag = new Bag<>();
    int limit = 1000000;
    for (int i = 1; i <= limit; i++) {
      bag.add(i);
    }
    assertEquals(limit, bag.size());
    assertFalse(bag.contains(-1));
    assertTrue(bag.contains(1));
  }
}
