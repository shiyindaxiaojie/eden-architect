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

package org.ylzl.eden.spring.framework.cola.catchlog.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.framework.cola.catchlog.aop.CatchLogAspect;

/**
 * 日志切面自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ComponentScan(basePackageClasses = CatchLogAspect.class)
//@Slf4j
@Configuration
public class CatchLogAutoConfiguration {
//  FIXME: 使用 Bean 声明会产生循环依赖
//	public static final String AUTOWIRED_CATCH_LOG_ASPECT = "Autowired CatchLogAspect";
//
//	@ConditionalOnMissingBean(CatchLogAspect.class)
//	@Bean
//	public CatchLogAspect catchLogAspect() {
//		log.debug(AUTOWIRED_CATCH_LOG_ASPECT);
//		return new CatchLogAspect();
//	}
}
