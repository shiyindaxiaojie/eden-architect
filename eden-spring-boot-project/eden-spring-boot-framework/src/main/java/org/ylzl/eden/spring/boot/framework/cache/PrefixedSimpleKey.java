/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
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

package org.ylzl.eden.spring.boot.framework.cache;

import lombok.NonNull;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 前缀简单 Key
 *
 * @author gyl
 * @since 2.0.0
 */
public class PrefixedSimpleKey implements Serializable {

  private static final long serialVersionUID = -2994839119346477993L;

  private final String prefix;

  private final Object[] params;

  private final String methodName;

  private int hashCode;

  public PrefixedSimpleKey(@NonNull String prefix, @NonNull String methodName, Object... elements) {
    this.prefix = prefix;
    this.methodName = methodName;
    this.params = new Object[elements.length];
    System.arraycopy(elements, 0, this.params, 0, elements.length);
    this.hashCode = prefix.hashCode();
    this.hashCode = 31 * this.hashCode + methodName.hashCode();
    this.hashCode = 31 * this.hashCode + Arrays.deepHashCode(this.params);
  }

  @Override
  public boolean equals(Object other) {
    return (this == other
        || (other instanceof PrefixedSimpleKey
            && this.prefix.equals(((PrefixedSimpleKey) other).prefix)
            && this.methodName.equals(((PrefixedSimpleKey) other).methodName)
            && Arrays.deepEquals(this.params, ((PrefixedSimpleKey) other).params)));
  }

  @Override
  public final int hashCode() {
    return this.hashCode;
  }

  @Override
  public String toString() {
    return this.prefix
        + " "
        + getClass().getSimpleName()
        + this.methodName
        + " ["
        + StringUtils.arrayToCommaDelimitedString(this.params)
        + "]";
  }
}
