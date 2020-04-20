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
package org.ylzl.eden.spring.boot.data.mybatis.factory;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.ylzl.eden.spring.boot.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 修复 SqlSessionFactoryBean
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class FixedSqlSessionFactoryBean extends SqlSessionFactoryBean {

    private static final String TYPE_ALIASES_PKG_SUFFIX = "/**/*.class";

    private static final String MSG_IO_EXCEPTION = "setTypeAliasesPackage caught IO exception: {}";

    private static final String MSG_CLASS_NOT_FOUND_EXCEPTION = "class not found, caught exception: {}";

    @Override
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
        try {
            return super.buildSqlSessionFactory();
        } catch (Exception e) {
            // 解决 Mybatis 找不到 Mapper 时不打印错误信息的问题
            log.error(e.getMessage(), e);
        } finally {
            ErrorContext.instance().reset();
        }
        return null;
    }

    @Override
    public void setTypeAliasesPackage(String typeAliasesPackage) {
        ResourcePatternResolver resolver = (ResourcePatternResolver) new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
        String resourcePath = ClassUtils.convertClassNameToResourcePath(typeAliasesPackage);

        // 低版本不支持多个包路径扫描
        typeAliasesPackage = StringUtils.join(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX, resourcePath, TYPE_ALIASES_PKG_SUFFIX);
        List<String> result = new ArrayList<String>();
        try {
            Resource[] resources = resolver.getResources(typeAliasesPackage);
            if (resources != null && resources.length > 0) {
                MetadataReader metadataReader = null;
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        result.add(Class.forName(metadataReader.getClassMetadata().getClassName()).getPackage().getName());
                    }
                }
            }
        } catch (IOException e) {
            log.error(MSG_IO_EXCEPTION, e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.error(MSG_CLASS_NOT_FOUND_EXCEPTION, e.getMessage(), e);
        }
        if (result.size() > 0) {
            super.setTypeAliasesPackage(StringUtils.join(result.toArray(), ","));
        }
    }
}
