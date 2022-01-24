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

package org.ylzl.eden.commons.io;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;

/**
 * IO 常量
 *
 * @author gyl
 * @since 2.4.x
 */
@UtilityClass
public class IOConstants {

  public static final char DIR_SEPARATOR_UNIX = IOUtils.DIR_SEPARATOR_UNIX;

  public static final char DIR_SEPARATOR_WINDOWS = IOUtils.DIR_SEPARATOR_WINDOWS;

  public static final char DIR_SEPARATOR = IOUtils.DIR_SEPARATOR;

  public static final String DIR_SEPARATOR_UNIX_STR = String.valueOf(IOUtils.DIR_SEPARATOR_UNIX);

  public static final String DIR_SEPARATOR_WINDOWS_STR =
      String.valueOf(IOUtils.DIR_SEPARATOR_WINDOWS);

  public static final String DIR_SEPARATOR_STR = String.valueOf(IOUtils.DIR_SEPARATOR);

  public static final String LINE_SEPARATOR_UNIX = IOUtils.LINE_SEPARATOR_UNIX;

  public static final String LINE_SEPARATOR_WINDOWS = IOUtils.LINE_SEPARATOR_WINDOWS;

  public static final String PARENT_DIR_SEPARATOR = ".." + DIR_SEPARATOR;

  public static final String RAF_MODE_READ = "r";

  public static final String RAF_MODE_WRTIE = "w";

  public static final String RAF_MODE_READ_WRTIE = "rw";
}
