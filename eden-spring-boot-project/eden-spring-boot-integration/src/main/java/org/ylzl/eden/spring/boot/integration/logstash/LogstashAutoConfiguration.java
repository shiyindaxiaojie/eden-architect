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

package org.ylzl.eden.spring.boot.integration.logstash;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.boolex.OnMarkerEvaluator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.filter.EvaluatorFilter;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.FilterReply;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.composite.ContextJsonProvider;
import net.logstash.logback.composite.GlobalCustomFieldsJsonProvider;
import net.logstash.logback.composite.loggingevent.*;
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.commons.lang.time.DateFormatEnum;
import org.ylzl.eden.spring.boot.framework.core.FrameworkConstants;
import org.ylzl.eden.spring.boot.integration.core.IntegrationConstants;
import org.ylzl.eden.spring.boot.integration.metrics.MetricsLoggingAutoConfiguration;
import org.ylzl.eden.spring.boot.integration.metrics.MetricsProperties;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Logstash 自动配置
 *
 * @author gyl
 * @since 1.0.0
 */
@ConditionalOnClass(LogstashEncoder.class)
@ConditionalOnExpression(LogstashAutoConfiguration.EXP_LOGSTASH_ENABLED)
@EnableConfigurationProperties(LogstashProperties.class)
@Slf4j
@Configuration
public class LogstashAutoConfiguration {

  public static final String EXP_LOGSTASH_ENABLED =
      "${" + IntegrationConstants.PROP_PREFIX + ".logstash.enabled:true}";

  private static final String CONSOLE_APPENDER_NAME = "CONSOLE";

  private static final String LOGSTASH_APPENDER_NAME = "LOGSTASH";

  private static final String ASYNC_LOGSTASH_APPENDER_NAME = "ASYNC_LOGSTASH";

  private static final String KEY_APP_NAME = "app_name";

  private static final String KEY_SERVER_PORT = "server_port";

  private static final String KEY_VERSION = "verison";

  private static final String KEY_TIMESTAMP = "timestamp";

  private static final String MSG_AUTOWIRED_LOGSTASH_APPENDER = "Autowired Logstash Appender";

  private static final String MSG_AUTOWIRED_CONSOLE_APPENDER = "Autowired Console Appender";

  private static final String MSG_FILTER_METRICS_LOG =
      "Filtering metrics logs from all appenders except the {} appender";

  private static final String MSG_FILTER_METRICS_LOG_WITHOUT_LOGSTASH =
      "Filtering metrics logs from all appenders except the {} appender";

  @Value(FrameworkConstants.NAME_PATTERN)
  private String applicationName;

  private final LogstashProperties logstashProperties;

  public LogstashAutoConfiguration(
      LogstashProperties logstashProperties,
      ServerProperties serverProperties,
      BuildProperties buildProperties,
      ObjectMapper objectMapper,
      @Autowired(required = false) MetricsProperties metricsProperties)
      throws JsonProcessingException {
    this.logstashProperties = logstashProperties;
    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    Map<String, Object> map = new HashMap<>();
    map.put(KEY_APP_NAME, applicationName);
    map.put(KEY_SERVER_PORT, serverProperties.getPort());
    map.put(KEY_VERSION, buildProperties.getVersion());
    String customFields = objectMapper.writeValueAsString(map);

    if (logstashProperties.isUseJsonFormat()) {
      addJsonConsoleAppender(context, customFields);
    }

    if (logstashProperties.isEnabled()) {
      addLogstashTcpSocketAppender(context, customFields);
    }

    if (logstashProperties.isUseJsonFormat() || logstashProperties.isEnabled()) {
      addContextListener(context, customFields);
    }

    if (metricsProperties != null && metricsProperties.getLogs().isEnabled()) {
      setMetricsMarkerLogbackFilter(context, logstashProperties.isUseJsonFormat());
    }
  }

  private void addJsonConsoleAppender(LoggerContext context, String customFields) {
    log.info(MSG_AUTOWIRED_LOGSTASH_APPENDER);

    ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
    consoleAppender.setContext(context);
    consoleAppender.setEncoder(compositeJsonEncoder(context, customFields));
    consoleAppender.setName(CONSOLE_APPENDER_NAME);
    consoleAppender.start();

    context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(CONSOLE_APPENDER_NAME);
    context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(consoleAppender);
  }

  private void addLogstashTcpSocketAppender(LoggerContext context, String customFields) {
    log.info(MSG_AUTOWIRED_CONSOLE_APPENDER);

    LogstashTcpSocketAppender logstashAppender = new LogstashTcpSocketAppender();
    logstashAppender.addDestinations(
        new InetSocketAddress(logstashProperties.getHost(), logstashProperties.getPort()));
    logstashAppender.setContext(context);
    logstashAppender.setEncoder(logstashEncoder(customFields));
    logstashAppender.setName(ASYNC_LOGSTASH_APPENDER_NAME);
    logstashAppender.start();

    context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(logstashAppender);
  }

  private void addContextListener(LoggerContext context, String customFields) {
    LogbackLoggerContextListener loggerContextListener =
        new LogbackLoggerContextListener(customFields);
    loggerContextListener.setContext(context);
    context.addListener(loggerContextListener);
  }

  private void setMetricsMarkerLogbackFilter(LoggerContext context, boolean useJsonFormat) {
    log.info(MSG_FILTER_METRICS_LOG_WITHOUT_LOGSTASH, LOGSTASH_APPENDER_NAME);
    OnMarkerEvaluator onMarkerEvaluator = new OnMarkerEvaluator();
    onMarkerEvaluator.setContext(context);
    onMarkerEvaluator.addMarker(MetricsLoggingAutoConfiguration.LOGGER_NAME);
    onMarkerEvaluator.start();
    EvaluatorFilter<ILoggingEvent> metricsFilter = new EvaluatorFilter<>();
    metricsFilter.setContext(context);
    metricsFilter.setEvaluator(onMarkerEvaluator);
    metricsFilter.setOnMatch(FilterReply.DENY);
    metricsFilter.start();

    context
        .getLoggerList()
        .forEach(
            logger ->
                logger
                    .iteratorForAppenders()
                    .forEachRemaining(
                        appender -> {
                          if (!appender.getName().equals(ASYNC_LOGSTASH_APPENDER_NAME)
                              && !(appender.getName().equals(CONSOLE_APPENDER_NAME)
                                  && useJsonFormat)) {
                            log.debug(MSG_FILTER_METRICS_LOG, appender.getName());
                            appender.setContext(context);
                            appender.addFilter(metricsFilter);
                            appender.start();
                          }
                        }));
  }

  private LoggingEventCompositeJsonEncoder compositeJsonEncoder(
      LoggerContext context, String customFields) {
    final LoggingEventCompositeJsonEncoder compositeJsonEncoder =
        new LoggingEventCompositeJsonEncoder();
    compositeJsonEncoder.setContext(context);
    compositeJsonEncoder.setProviders(jsonProviders(context, customFields));
    compositeJsonEncoder.start();
    return compositeJsonEncoder;
  }

  private LogstashEncoder logstashEncoder(String customFields) {
    final LogstashEncoder logstashEncoder = new LogstashEncoder();
    logstashEncoder.setThrowableConverter(throwableConverter());
    logstashEncoder.setCustomFields(customFields);
    return logstashEncoder;
  }

  private LoggingEventJsonProviders jsonProviders(LoggerContext context, String customFields) {
    final LoggingEventJsonProviders jsonProviders = new LoggingEventJsonProviders();
    jsonProviders.addArguments(new ArgumentsJsonProvider());
    jsonProviders.addContext(new ContextJsonProvider<ILoggingEvent>());
    jsonProviders.addGlobalCustomFields(customFieldsJsonProvider(customFields));
    jsonProviders.addLogLevel(new LogLevelJsonProvider());
    jsonProviders.addLoggerName(loggerNameJsonProvider());
    jsonProviders.addMdc(new MdcJsonProvider());
    jsonProviders.addMessage(new MessageJsonProvider());
    jsonProviders.addPattern(new LoggingEventPatternJsonProvider());
    jsonProviders.addStackTrace(stackTraceJsonProvider());
    jsonProviders.addThreadName(new ThreadNameJsonProvider());
    jsonProviders.addTimestamp(timestampJsonProvider());
    jsonProviders.setContext(context);
    return jsonProviders;
  }

  private GlobalCustomFieldsJsonProvider<ILoggingEvent> customFieldsJsonProvider(
      String customFields) {
    final GlobalCustomFieldsJsonProvider<ILoggingEvent> customFieldsJsonProvider =
        new GlobalCustomFieldsJsonProvider<>();
    customFieldsJsonProvider.setCustomFields(customFields);
    return customFieldsJsonProvider;
  }

  private LoggerNameJsonProvider loggerNameJsonProvider() {
    final LoggerNameJsonProvider loggerNameJsonProvider = new LoggerNameJsonProvider();
    loggerNameJsonProvider.setShortenedLoggerNameLength(20);
    return loggerNameJsonProvider;
  }

  private StackTraceJsonProvider stackTraceJsonProvider() {
    StackTraceJsonProvider stackTraceJsonProvider = new StackTraceJsonProvider();
    stackTraceJsonProvider.setThrowableConverter(throwableConverter());
    return stackTraceJsonProvider;
  }

  private ShortenedThrowableConverter throwableConverter() {
    final ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
    throwableConverter.setRootCauseFirst(true);
    return throwableConverter;
  }

  private LoggingEventFormattedTimestampJsonProvider timestampJsonProvider() {
    final LoggingEventFormattedTimestampJsonProvider timestampJsonProvider =
        new LoggingEventFormattedTimestampJsonProvider();
    timestampJsonProvider.setTimeZone(DateFormatEnum.DEFAULT_TIME_ZONE);
    timestampJsonProvider.setFieldName(KEY_TIMESTAMP);
    return timestampJsonProvider;
  }

  private class LogbackLoggerContextListener extends ContextAwareBase
      implements LoggerContextListener {

    private final String customFields;

    private LogbackLoggerContextListener(String customFields) {
      this.customFields = customFields;
    }

    @Override
    public boolean isResetResistant() {
      return true;
    }

    @Override
    public void onStart(LoggerContext context) {
      if (logstashProperties.isUseJsonFormat()) {
        addJsonConsoleAppender(context, customFields);
      }
      if (logstashProperties.isEnabled()) {
        addLogstashTcpSocketAppender(context, customFields);
      }
    }

    @Override
    public void onReset(LoggerContext context) {
      if (logstashProperties.isUseJsonFormat()) {
        addJsonConsoleAppender(context, customFields);
      }
      if (logstashProperties.isEnabled()) {
        addLogstashTcpSocketAppender(context, customFields);
      }
    }

    @Override
    public void onStop(LoggerContext context) {}

    @Override
    public void onLevelChange(Logger logger, Level level) {}
  }
}
