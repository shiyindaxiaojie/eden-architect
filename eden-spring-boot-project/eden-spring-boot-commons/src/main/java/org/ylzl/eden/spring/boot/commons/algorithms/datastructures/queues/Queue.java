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

package org.ylzl.eden.spring.boot.commons.algorithms.datastructures.queues;

/**
 * TODO
 *
 * @author gyl
 * @since 1.0.0
 */
public class Queue {

  private static final int DEFAULT_CAPACITY = 10;

  private int maxSize;

  private int[] queueArray;

  private int front;

  private int rear;

  private int nextItems;

  public Queue(int size) {
    maxSize = size;
    queueArray = new int[size];
    front = 0;
    rear = -1;
    nextItems = 0;
  }

  public boolean isEmpty() {
    return nextItems == 0;
  }

  public boolean isFull() {
    return nextItems == maxSize;
  }

  public boolean add(int x) {
    if (isFull()) {
      return false;
    }
    rear = (rear + 1) % maxSize;
    queueArray[rear] = x;
    nextItems++;
    return true;
  }

  public int remove() {
    if (isEmpty()) {
      return -1;
    }
    int temp = queueArray[front];
    front = (front + 1) % maxSize;
    nextItems--;
    return temp;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = front; ; i = ++i % maxSize) {
      sb.append(queueArray[i]).append(", ");
      if (i == rear) {
        break;
      }
    }
    sb.replace(sb.length() - 2, sb.length(), "]");
    return sb.toString();
  }
}
