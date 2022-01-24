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
package org.ylzl.eden.spring.data.hibernate.cfg;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.ylzl.eden.commons.env.WebServerUtils;
import org.ylzl.eden.commons.lang.StringUtils;

/**
 * Hibernate 配置
 *
 * @author gyl
 * @since 2.4.x
 */
public class HibernateConfiguration extends Configuration {

	private String hibernateConfigFile = StandardServiceRegistryBuilder.DEFAULT_CFG_RESOURCE_NAME;

	public HibernateConfiguration() {
	}

	public HibernateConfiguration(
		String dialect, String url, String driverClass, String username, String password) {
		super.configure(hibernateConfigFile);
		super.setProperty(Environment.DIALECT, dialect);
		super.setProperty(Environment.URL, url);
		super.setProperty(Environment.DRIVER, driverClass);
		super.setProperty(Environment.USER, username);
		super.setProperty(Environment.PASS, password);
	}

	public HibernateConfiguration(String dialect, String datasourceName) {
		super.configure(hibernateConfigFile);
		super.setProperty(Environment.DIALECT, dialect);
		super.setProperty(
			Environment.DATASOURCE, StringUtils.join(WebServerUtils.getLookup(), datasourceName));
	}

	public String getHibernateConfigFile() {
		return hibernateConfigFile;
	}

	public void setHibernateConfigFile(String hibernateConfigFile) {
		this.hibernateConfigFile = hibernateConfigFile;
	}
}
