package org.ylzl.eden.sample.config;

import lombok.experimental.UtilityClass;

/**
 * 应用常量定义
 *
 * @author gyl
 * @since 0.0.1
 */
@UtilityClass
public class ApplicationConstants {

    /**
     * 根目录包路径
     */
    public final String BASE_PACKAGE = "org.ylzl.eden.sample";

    /**
     * JPA 包路径
     */
    public static final String JPA_PACKAGE = BASE_PACKAGE + ".repository";
}
