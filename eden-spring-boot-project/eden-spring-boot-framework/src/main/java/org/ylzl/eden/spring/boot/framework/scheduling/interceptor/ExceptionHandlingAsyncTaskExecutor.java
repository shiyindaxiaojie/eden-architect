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

package org.ylzl.eden.spring.boot.framework.scheduling.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 异步任务执行异常处理类
 *
 * @author gyl
 * @since 0.0.1
 */
@Slf4j
public class ExceptionHandlingAsyncTaskExecutor
    implements AsyncTaskExecutor, InitializingBean, DisposableBean {

  private static final String MSG_ASYNC_EXCEPTION = "AsyncTaskExecutor exec caught exception: {}";

  private final AsyncTaskExecutor executor;

  public ExceptionHandlingAsyncTaskExecutor(AsyncTaskExecutor executor) {
    this.executor = executor;
  }

  @Override
  public void execute(Runnable task) {
    executor.execute(createWrappedRunnable(task));
  }

  @Override
  public void execute(Runnable task, long startTimeout) {
    executor.execute(createWrappedRunnable(task), startTimeout);
  }

  @SuppressWarnings("unchecked")
  private <T> Callable<T> createCallable(final Callable<T> task) {
    return new Callable() {

      @Override
      public Object call() throws Exception {
        try {
          return task.call();
        } catch (Exception e) {
          handle(e);
        }
        return null;
      }
    };
  }

  private Runnable createWrappedRunnable(final Runnable task) {
    return new Runnable() {

      @Override
      public void run() {
        try {
          task.run();
        } catch (Exception e) {
          handle(e);
        }
      }
    };
  }

  protected void handle(Exception e) {
    log.error(MSG_ASYNC_EXCEPTION, e.getMessage(), e);
  }

  @Override
  public Future<?> submit(Runnable task) {
    return executor.submit(createWrappedRunnable(task));
  }

  @Override
  public <T> Future<T> submit(Callable<T> task) {
    return executor.submit(createCallable(task));
  }

  @Override
  public void destroy() throws Exception {
    if (executor instanceof DisposableBean) {
      DisposableBean bean = (DisposableBean) executor;
      bean.destroy();
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (executor instanceof InitializingBean) {
      InitializingBean bean = (InitializingBean) executor;
      bean.afterPropertiesSet();
    }
  }
}
