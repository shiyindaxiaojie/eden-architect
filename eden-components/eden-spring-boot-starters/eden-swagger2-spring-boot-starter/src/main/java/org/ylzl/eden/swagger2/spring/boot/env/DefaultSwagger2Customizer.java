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

package org.ylzl.eden.swagger2.spring.boot.env;

import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;
import org.ylzl.eden.spring.integration.swagger2.customizer.Swagger2Customizer;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * 默认的 Swagger 自定义实现
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class DefaultSwagger2Customizer implements Swagger2Customizer, Ordered {

	public static final int DEFAULT_ORDER = 0;
	private final Swagger2Properties properties;
	private int order = DEFAULT_ORDER;

	public DefaultSwagger2Customizer(Swagger2Properties properties) {
		this.properties = properties;
	}

	@Override
	public void customize(Docket docket) {
		Contact contact =
			new Contact(
				properties.getContactName(), properties.getContactUrl(), properties.getContactEmail());

		ApiInfo apiInfo =
			new ApiInfo(
				properties.getTitle(),
				properties.getDescription(),
				properties.getVersion(),
				properties.getTermsOfServiceUrl(),
				contact,
				properties.getLicense(),
				properties.getLicenseUrl(),
				new ArrayList<>());

		docket
			.host(properties.getHost())
			.protocols(new HashSet<>(Arrays.asList(properties.getProtocols())))
			.apiInfo(apiInfo)
			.useDefaultResponseMessages(properties.isUseDefaultResponseMessages())
			.forCodeGeneration(true)
			.directModelSubstitute(ByteBuffer.class, String.class)
			.genericModelSubstitutes(ResponseEntity.class)
			.select()
			.paths(regex(properties.getDefaultIncludePattern()))
			.build();
	}

	@Override
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
