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

package org.ylzl.eden.spring.boot.data.mybatis.mapper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import tk.mybatis.mapper.autoconfigure.MapperProperties;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.util.StringUtil;
import tk.mybatis.spring.mapper.MapperFactoryBean;

import java.util.Set;

/**
 * 修复 FixedClassPathMapperScanner
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class FixedClassPathMapperScanner extends org.mybatis.spring.mapper.ClassPathMapperScanner {

    @Getter
    private MapperHelper mapperHelper = new MapperHelper();

    public FixedClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        doAfterScan(beanDefinitions);
        return beanDefinitions;
    }

    protected void doAfterScan(Set<BeanDefinitionHolder> beanDefinitions) {
        //如果没有注册过接口，就注册默认的Mapper接口
        this.mapperHelper.ifEmptyRegisterDefaultInterface();
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            if (StringUtil.isNotEmpty(definition.getBeanClassName())
                && definition.getBeanClassName().equals("org.mybatis.spring.mapper.MapperFactoryBean")) {
                definition.setBeanClass(MapperFactoryBean.class);
                definition.getPropertyValues().add("mapperHelper", this.mapperHelper);
            }
        }
    }

    public void setMapperProperties(MapperProperties mapperProperties) {
        if (mapperProperties != null) {
            mapperHelper.setConfig(mapperProperties);
        }
    }
}
