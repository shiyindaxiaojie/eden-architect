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

package org.ylzl.eden.spring.framework.web.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.ylzl.eden.commons.env.CharsetConstants;

import java.util.List;

/**
 * REST 自动装配
 *
 * @author gyl
 * @since 2.4.x
 */
@Slf4j
@Configuration
public class RestTemplateAutoConfiguration {

	private static final String MSG_AUTOWIRED_REST_TEMPLATE = "Autowired RestTemplate";

	private static final String MSG_AUTOWIRED_ASYNC_REST_TEMPLATE = "Autowired AsyncRestTemplate";

	@ConditionalOnMissingBean
	@Bean
	public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
		log.debug(MSG_AUTOWIRED_REST_TEMPLATE);
		RestTemplate restTemplate = new RestTemplate(factory);
		this.setDefaultCharset(restTemplate.getMessageConverters());
		return restTemplate;
	}

	@ConditionalOnMissingBean
	@Bean
	public AsyncRestTemplate asyncRestTemplate(AsyncClientHttpRequestFactory factory) {
		log.debug(MSG_AUTOWIRED_ASYNC_REST_TEMPLATE);
		AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate(factory);
		this.setDefaultCharset(asyncRestTemplate.getMessageConverters());
		return asyncRestTemplate;
	}

	private void setDefaultCharset(List<HttpMessageConverter<?>> httpMessageConverters) {
		for (HttpMessageConverter<?> httpMessageConverter : httpMessageConverters) {
			if (httpMessageConverter instanceof StringHttpMessageConverter) {
				((StringHttpMessageConverter) httpMessageConverter)
					.setDefaultCharset(CharsetConstants.UTF_8);
				break;
			}
		}
	}

	@ConditionalOnClass(ObjectMapper.class)
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(
		RestTemplate restTemplate) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		List<MediaType> mediaTypes = Lists.newArrayList();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		mediaTypes.add(MediaType.APPLICATION_ATOM_XML);
		mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
		mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
		mediaTypes.add(MediaType.APPLICATION_PDF);
		mediaTypes.add(MediaType.APPLICATION_RSS_XML);
		mediaTypes.add(MediaType.APPLICATION_XHTML_XML);
		mediaTypes.add(MediaType.APPLICATION_XML);
		mediaTypes.add(MediaType.IMAGE_GIF);
		mediaTypes.add(MediaType.IMAGE_JPEG);
		mediaTypes.add(MediaType.IMAGE_PNG);
		mediaTypes.add(MediaType.TEXT_EVENT_STREAM);
		mediaTypes.add(MediaType.TEXT_HTML);
		mediaTypes.add(MediaType.TEXT_MARKDOWN);
		mediaTypes.add(MediaType.TEXT_PLAIN);
		mediaTypes.add(MediaType.TEXT_XML);
		converter.setSupportedMediaTypes(mediaTypes);
		List<HttpMessageConverter<?>> messageConverters = Lists.newArrayList();
		messageConverters.add(converter);
		restTemplate.setMessageConverters(messageConverters);
		return converter;
	}
}
