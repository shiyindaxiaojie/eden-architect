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

package old.oauth2.token;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;

/**
 * 令牌授权客户端接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface TokenGrantClient {

	/**
	 * 发送密码授权
	 *
	 * @param username 用户
	 * @param password 密码
	 * @return OAuth2 访问令牌
	 * @throws InvalidGrantException
	 */
	OAuth2AccessToken sendPasswordGrant(String username, String password)
		throws InvalidGrantException;

	/**
	 * 发送刷新授权
	 *
	 * @param refreshTokenValue 刷新令牌
	 * @return OAuth2 访问令牌
	 * @throws InvalidGrantException
	 */
	OAuth2AccessToken sendRefreshGrant(String refreshTokenValue) throws InvalidGrantException;

	/**
	 * 发送客户端授权
	 *
	 * @return OAuth2 访问令牌
	 * @throws InvalidGrantException
	 */
	OAuth2AccessToken sendClientCredentialsGrant() throws InvalidGrantException;
}
