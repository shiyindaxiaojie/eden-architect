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
package org.ylzl.eden.spring.boot.data.hibernate.factory;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.ylzl.eden.spring.boot.data.hibernate.cfg.HibernateConfiguration;

/**
 * Hibernate Session 工厂类
 *
 * @author gyl
 * @since 1.0.0
 */
public class HibernateSessionFactory {

  private final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<Session>();

  private SessionFactory sessionFactory;

  private Configuration configuration;

  public HibernateSessionFactory() {
    this.configuration = new HibernateConfiguration();
    configuration.configure();
    this.sessionFactory = this.buildSessionFactory(configuration);
  }

  public HibernateSessionFactory(Configuration configuration) {
    this.configuration = configuration;
    configuration.configure();
    this.sessionFactory = this.buildSessionFactory(configuration);
  }

  public SessionFactory getSessionFactory() {
    return this.sessionFactory;
  }

  public void rebuildSessionFactory() {
    if (configuration instanceof HibernateConfiguration) {
      HibernateConfiguration hibernateConfiguration = (HibernateConfiguration) this.configuration;
      this.configuration.configure(hibernateConfiguration.getHibernateConfigFile());
    } else {
      this.configuration.configure();
    }
    this.sessionFactory = this.buildSessionFactory(configuration);
  }

  public Session getSession() {
    Session session = this.sessionThreadLocal.get();
    if (session == null || (!session.isOpen())) {
      if (this.sessionFactory == null) {
        rebuildSessionFactory();
      }
      session = this.sessionFactory.openSession();
      this.sessionThreadLocal.set(session);
    }
    return session;
  }

  public void closeSession() throws HibernateException {
    Session session = this.sessionThreadLocal.get();
    if (session != null) {
      session.close();
    }
    this.sessionThreadLocal.set(null);
  }

  private SessionFactory buildSessionFactory(Configuration configuration) {
    ServiceRegistry serviceRegistry =
        new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
    return configuration.buildSessionFactory(serviceRegistry);
  }
}
