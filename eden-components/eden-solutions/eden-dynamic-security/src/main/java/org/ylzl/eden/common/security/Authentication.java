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

package org.ylzl.eden.common.security;

import java.time.Duration;
import java.util.Collection;

/**
 * 认证
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface Authentication {

	/**
	 * 认证
	 *
	 * @param id 认证ID
	 */
	void authenticate(Object id);

	/**
	 * 认证
	 *
	 * @param id     认证ID
	 * @param device 设备标识
	 */
	void authenticate(Object id, String device);

	/**
	 * 认证
	 *
	 * @param id         认证ID
	 * @param rememberMe 是否记住我
	 */
	void authenticate(Object id, boolean rememberMe);

	/**
	 * 认证
	 *
	 * @param id      认证ID
	 * @param timeout 登录超时
	 */
	void authenticate(Object id, Duration timeout);

	/**
	 * 获取用户认证凭据
	 *
	 * @return 用户认证凭据
	 */
	Object getCredentials();

	/**
	 * 获取用户身份信息
	 *
	 * @return 用户身份信息
	 */
	Object getPrincipal();

	/**
	 * 获取用户额外信息
	 *
	 * @return 用户额外信息
	 */
	Object getDetails();

	/**
	 * 获取用户权限
	 *
	 * @return 用户权限
	 */
	Collection<? extends GrantedAuthority> getAuthorities();

	/**
	 * 是否已认证
	 *
	 * @return 是否已认证
	 */
	boolean isAuthenticated();

	/**
	 * 检查是否已认证
	 * <br/> 如果未认证，抛出异常
	 */
	void checkAuthenticated();

	/**
	 * 踢人下线
	 *
	 * @param loginId 认证ID
	 */
	void kickout(Object loginId);

	/**
	 * 踢人下线
	 *
	 * @param loginId 认证ID
	 * @param device  设备标识
	 */
	void kickout(Object loginId, String device);

	/**
	 * 顶号上线
	 *
	 * @param loginId 认证ID
	 * @param device  设备标识
	 */
	void crowding(Object loginId, String device);
}
