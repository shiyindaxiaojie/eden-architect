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

package org.ylzl.eden.commons.algorithms.datastructures.trees;

import lombok.Data;
import lombok.NonNull;

/**
 * 二叉树
 *
 * @author gyl
 * @since 1.0.0
 */
public class BinaryTree<T extends Comparable<T>> {

  private Node root;

  /**
   * 查找树节点
   *
   * @param key
   * @return
   */
  public Node find(@NonNull T key) {
    Node current = root;
    while (current != null) {
      int temp = key.compareTo(current.getData());
      if (temp == 0) { // 刚好命中
        return current;
      }
      if (temp < 0) {
        if (current.getLeft() == null) { // 左边没有节点，表示不存在
          return null;
        }
        current = current.getLeft();
      } else {
        if (current.right == null) { // 右边没有节点，表示不存在
          return null;
        }
        current = current.right;
      }
    }
    return null;
  }

  /**
   * 新增节点
   *
   * @param value
   */
  public void put(@NonNull T value) {
    Node newNode = new Node(value);
    if (root == null) {
      root = newNode;
    } else {
      Node current = root;
      while (current != null) {
        int temp = value.compareTo(current.getData());
        if (temp == 0) { // 刚好命中
          return;
        }
        if (temp < 0) {
          if (current.getLeft() == null) { // 左边没有节点，往左边新增
            newNode.setParent(current);
            current.setLeft(newNode);
            return;
          }
          current = current.getLeft();
        } else {
          if (current.right == null) { // 右边没有节点，往右边新增
            newNode.setParent(current);
            current.setRight(newNode);
            return;
          }
          current = current.right;
        }
      }
    }
  }

  public Node findSuccessor(Node node) {
    if (node.getRight() == null) {
      return node;
    }
    Node current = node.getRight();
    Node parent = node.getRight();
    while (current != null) {
      parent = current;
      current = current.getLeft();
    }
    return parent;
  }

  @Data
  class Node {

    private Node left;

    private Node right;

    private Node parent;

    private T data;

    public Node(T data) {
      this.data = data;
    }
  }
}
