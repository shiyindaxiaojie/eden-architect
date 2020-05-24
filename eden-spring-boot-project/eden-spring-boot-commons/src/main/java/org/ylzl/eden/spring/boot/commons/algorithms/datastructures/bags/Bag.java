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

package org.ylzl.eden.spring.boot.commons.algorithms.datastructures.bags;

import lombok.NonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 背包
 *
 * @author gyl
 * @since 0.0.1
 */
public class Bag<E> implements Iterable<E> {

  private Node<E> firstNode;

  private int size;

  private static class Node<E> {

    private E element;

    private Node<E> nextNode;
  }

  @Override
  public Iterator<E> iterator() {
    return new Iterator<E>() {

      private Node<E> currentNode = firstNode;

      @Override
      public boolean hasNext() {
        return firstNode != null;
      }

      @Override
      public E next() {
        if (!hasNext()) {
          throw new NoSuchElementException("背包没有元素可以取出");
        }
        E element = firstNode.element;
        currentNode = firstNode.nextNode;
        return element;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("不支持从背包移除元素");
      }
    };
  }

  /**
   * 背包元素个数
   *
   * @return
   */
  public int size() {
    return size;
  }

  /**
   * 背包是否为空
   *
   * @return
   */
  public boolean isEmpty() {
    return firstNode == null;
  }

  /**
   * 往背包添加元素
   *
   * @param element
   */
  public void add(@NonNull E element) {
    Node<E> oldNode = firstNode;
    firstNode = new Node<>();
    firstNode.element = element;
    firstNode.nextNode = oldNode;
    size++;
  }

  /**
   * 背包是否包含元素
   *
   * @param element
   * @return
   */
  public boolean contains(@NonNull E element) {
    Iterator<E> iterator = this.iterator();
    while (iterator.hasNext()) {
      if (iterator.next().equals(element)) {
        return true;
      }
    }
    return false;
  }
}
