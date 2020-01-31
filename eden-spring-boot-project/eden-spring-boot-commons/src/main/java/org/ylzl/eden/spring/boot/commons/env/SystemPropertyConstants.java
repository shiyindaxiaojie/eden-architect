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
package org.ylzl.eden.spring.boot.commons.env;

import lombok.experimental.UtilityClass;

/**
 * Java 内置配置属性常量
 *
 * @author gyl
 * @since 0.0.1
 */
@UtilityClass
public class SystemPropertyConstants {

    /**
     * Java 运行时环境版本
     */
    public static final String JAVA_VERSION = "java.version";

    /**
     * Java 运行时环境供应商
     */
    public static final String JAVA_VENDOR = "java.vendor";

    /**
     * Java 供应商的 URL
     */
    public static final String JAVA_VENDOR_URL = "java.vendor.url";

    /**
     * Java 安装目录
     */
    public static final String JAVA_HOME = "java.home";

    /**
     * Java 虚拟机实现版本
     */
    public static final String JAVA_VM_VERSION = "java.vm.version";

    /**
     * Java 虚拟机实现名称
     */
    public static final String JAVA_VM_NAME = "java.vm.name";

    /**
     * Java 虚拟机实现供应商
     */
    public static final String JAVA_VM_VENDER = "java.vm.vendor";

    /**
     * Java 虚拟机规范版本
     */
    public static final String JAVA_VM_SPEC_VERSION = "java.vm.specification.version";

    /**
     * Java 虚拟机规范名称
     */
    public static final String JAVA_VM_SPEC_NAME = "java.vm.specification.name";

    /**
     * Java 虚拟机规范供应商
     */
    public static final String JAVA_VM_SPEC_VENDER = "java.vm.specification.vendor";

    /**
     * Java 运行时环境规范版本
     */
    public static final String JAVA_SPEC_VERSION = "java.specification.version";

    /**
     * Java 运行时环境规范名称
     */
    public static final String JAVA_SPEC_NAME = "java.specification.name";

    /**
     * Java 运行时环境规范供应商
     */
    public static final String JAVA_SPEC_VENDER = "java.specification.vendor";

    /**
     * Java 类格式版本号
     */
    public static final String JAVA_CLASS_VERSION = "java.class.version";

    /**
     * Java 类路径
     */
    public static final String JAVA_CLASS_PATH = "java.class.path";

    /**
     * Java 加载库时搜索的路径列表
     */
    public static final String JAVA_LIBRARY_PATH = "java.library.path";

    /**
     * Java 默认的临时文件路径
     */
    public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

    /**
     * Java 使用的 JIT 编译器的名称
     */
    public static final String JAVA_COMPILER = "java.compiler";

    /**
     * Java 扩展目录的路径
     */
    public static final String JAVA_EXT_DIRS = "java.ext.dirs";

    /**
     * 操作系统的名称
     */
    public static final String OS_NAME = "os.name";

    /**
     * 操作系统的架构
     */
    public static final String OS_ARCH = "os.arch";

    /**
     * 操作系统的版本
     */
    public static final String OS_VERSION = "os.version";

    /**
     * 文件分隔符
     */
    public static final String FILE_SEPARATOR = "file.separator";

    /**
     * 路径分隔符
     */
    public static final String PATH_SEPARATOR = "path.separator";

    /**
     * 行分隔符
     */
    public static final String LINE_SEPARATOR = "line.separator";

    /**
     * 用户的账户名称
     */
    public static final String USER_NAME = "user.name";

    /**
     * 用户的主目录
     */
    public static final String USER_HOME = "user.home";

    /**
     * 用户的当前工作目录
     */
    public static final String USER_DIR = "user.dir";
}
