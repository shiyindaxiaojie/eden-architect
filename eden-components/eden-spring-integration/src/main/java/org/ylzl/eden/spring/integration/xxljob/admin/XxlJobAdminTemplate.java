/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.integration.xxljob.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.spring.framework.beans.util.MultiValueMapUtils;
import org.ylzl.eden.spring.integration.xxljob.constant.XxlJobAdminRoutes;
import org.ylzl.eden.spring.integration.xxljob.model.XxlJobGroup;
import org.ylzl.eden.spring.integration.xxljob.model.XxlJobInfo;

import java.util.Collections;

/**
 * XxlJob 操作模板
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class XxlJobAdminTemplate {

	private static final String LOGIN_IDENTITY_KEY = "XXL_JOB_LOGIN_IDENTITY";

	private final RestTemplate restTemplate;

	private final String accessToken;

	private final String addresses;

	private ResponseEntity<String> postForEntity(MultiValueMap<String, Object> params, String route) {
		HttpHeaders headers = new HttpHeaders();
		addAccessToken(headers);
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
		String url = this.buildAdminApiUrl(route);
		return this.restTemplate.postForEntity(url, request, String.class);
	}

	private <T> ResponseEntity<String> postForEntity(T object, String route) {
		MultiValueMap<String, Object> params = MultiValueMapUtils.toMap(object);
		return postForEntity(params, route);
	}

	private ResponseEntity<String> postForEntity(int id, String route) {
		HttpHeaders headers = new HttpHeaders();
		addAccessToken(headers);
		HttpEntity<Integer> request = new HttpEntity<>(id, headers);
		String url = this.buildAdminApiUrl(route);
		return this.restTemplate.postForEntity(url, request, String.class);
	}

	private ResponseEntity<String> postForEntityById(int id, String route) {
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.put("id", Collections.singletonList(String.valueOf(id)));
		return postForEntity(paramMap, route);
	}

	private ResponseEntity<String> postForEntityById(int id, String executorParam, String route) {
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.put("id", Collections.singletonList(String.valueOf(id)));
		paramMap.put("executorParam", Collections.singletonList(executorParam));
		return postForEntity(paramMap, route);
	}

	private void addAccessToken(HttpHeaders headers) {
		headers.add("Cookie", LOGIN_IDENTITY_KEY + "=" + accessToken);
	}

	public ResponseEntity<String> loadGroupById(int groupId) {
		return postForEntity(groupId, XxlJobAdminRoutes.JOBGROUP_LOAD_BY_ID);
	}

	public ResponseEntity<String> saveGroup(XxlJobGroup xxlJobGroup) {
		return postForEntity(xxlJobGroup, XxlJobAdminRoutes.JOBGROUP_SAVE);
	}

	public ResponseEntity<String> saveOrUpdateGroup(XxlJobGroup xxlJobGroup) {
		return postForEntity(xxlJobGroup, XxlJobAdminRoutes.JOBGROUP_SAVE_OR_UPDATE);
	}

	public ResponseEntity<String> updateGroup(XxlJobGroup xxlJobGroup) {
		return postForEntity(xxlJobGroup, XxlJobAdminRoutes.JOBGROUP_UPDATE);
	}

	public ResponseEntity<String> removeGroup(int groupId) {
		return postForEntity(groupId, XxlJobAdminRoutes.JOBGROUP_REMOVE);
	}

	public ResponseEntity<String> jobinfoPageList(int start, int length, int jobGroup, int triggerStatus,
												  String jobDesc, String executorHandler, String author) {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.put("start", Collections.singletonList(Math.max(0, start)));
		params.put("length", Collections.singletonList(Math.min(length, 5)));
		params.put("jobGroup", Collections.singletonList(jobGroup));
		params.put("triggerStatus", Collections.singletonList(triggerStatus));
		params.put("jobDesc", Collections.singletonList(jobDesc));
		params.put("executorHandler", Collections.singletonList(executorHandler));
		params.put("author", Collections.singletonList(author));
		return postForEntity(params, XxlJobAdminRoutes.JOBINFO_PAGELIST);
	}

	public ResponseEntity<String> jobinfoPageList(int start, int length, int jobGroup, int triggerStatus) {
		return this.jobinfoPageList(start, length, jobGroup, triggerStatus,
			Strings.EMPTY, Strings.EMPTY, Strings.EMPTY);
	}

	public ResponseEntity<String> addJob(XxlJobInfo xxlJobInfo) {
		return postForEntity(xxlJobInfo, XxlJobAdminRoutes.JOBINFO_ADD);
	}

	public ResponseEntity<String> addOrUpdateJob(XxlJobInfo xxlJobInfo) {
		return postForEntity(xxlJobInfo, XxlJobAdminRoutes.JOBINFO_ADD_OR_UPDATE);
	}

	public ResponseEntity<String> updateJob(XxlJobInfo xxlJobInfo) {
		return postForEntity(xxlJobInfo, XxlJobAdminRoutes.JOBINFO_UPDATE);
	}

	public ResponseEntity<String> removeJob(int jobId) {
		return postForEntityById(jobId, XxlJobAdminRoutes.JOBINFO_REMOVE);
	}

	public ResponseEntity<String> stopJob(int jobId) {
		return postForEntityById(jobId, XxlJobAdminRoutes.JOBINFO_STOP);
	}

	public ResponseEntity<String> startJob(int jobId) {
		return postForEntityById(jobId, XxlJobAdminRoutes.JOBINFO_START);
	}

	public ResponseEntity<String> triggerJob(int jobId, String executorParam) {
		return postForEntityById(jobId, executorParam, XxlJobAdminRoutes.JOBINFO_TRIGGER);
	}

	private String buildAdminApiUrl(String route) {
		return addresses + route;
	}
}
