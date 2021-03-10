package org.ylzl.eden.spring.boot.integration.logstash.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.boolex.OnMarkerEvaluator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.filter.EvaluatorFilter;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.FilterReply;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.composite.ContextJsonProvider;
import net.logstash.logback.composite.GlobalCustomFieldsJsonProvider;
import net.logstash.logback.composite.loggingevent.*;
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.ylzl.eden.spring.boot.integration.logstash.LogstashProperties;

import java.net.InetSocketAddress;

/**
 * Logstash 工具集
 *
 * @author gyl
 * @since 0.0.1
 */
@Slf4j
@UtilityClass
public class LogstashUtils {

  private static final String CONSOLE_APPENDER_NAME = "CONSOLE";

  private static final String ASYNC_LOGSTASH_APPENDER_NAME = "ASYNC_LOGSTASH";

  private static final String METRICS_MARKER_NAME = "metrics";

  private static final String INIT_LOGSTASH_CONSOLE_APPENDER =
      "Initializing Logstash Console Appender";

  private static final String INIT_LOGSTASH_TCP_SOCKET_APPENDER =
      "Initializing Logstash TCP Socket Appender";

  public static void addJsonConsoleAppender(LoggerContext context, String customFields) {
    log.debug(INIT_LOGSTASH_CONSOLE_APPENDER);

    ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
    consoleAppender.setContext(context);
    consoleAppender.setEncoder(compositeJsonEncoder(context, customFields));
    consoleAppender.setName(CONSOLE_APPENDER_NAME);
    consoleAppender.start();

    context
        .getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME)
        .detachAppender(CONSOLE_APPENDER_NAME);
    context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).addAppender(consoleAppender);
  }

  public static void addLogstashTcpSocketAppender(
      LoggerContext context, String customFields, LogstashProperties logstashProperties) {
    log.debug(INIT_LOGSTASH_TCP_SOCKET_APPENDER);

    LogstashTcpSocketAppender logstashAppender = new LogstashTcpSocketAppender();
    logstashAppender.addDestinations(
        new InetSocketAddress(logstashProperties.getHost(), logstashProperties.getPort()));
    logstashAppender.setContext(context);
    logstashAppender.setEncoder(logstashEncoder(customFields));
    logstashAppender.setName(ASYNC_LOGSTASH_APPENDER_NAME);
    logstashAppender.setQueueSize(logstashProperties.getQueueSize());
    logstashAppender.start();

    context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).addAppender(logstashAppender);
  }

  public static void addContextListener(
      LoggerContext context, String customFields, LogstashProperties properties) {
    LogbackLoggerContextListener loggerContextListener =
        new LogbackLoggerContextListener(properties, customFields);
    loggerContextListener.setContext(context);
    context.addListener(loggerContextListener);
  }

  public static void setMetricsMarkerLogbackFilter(LoggerContext context, boolean useJsonFormat) {
    OnMarkerEvaluator onMarkerMetricsEvaluator = new OnMarkerEvaluator();
    onMarkerMetricsEvaluator.setContext(context);
    onMarkerMetricsEvaluator.addMarker(METRICS_MARKER_NAME);
    onMarkerMetricsEvaluator.start();
    EvaluatorFilter<ILoggingEvent> metricsFilter = new EvaluatorFilter();
    metricsFilter.setContext(context);
    metricsFilter.setEvaluator(onMarkerMetricsEvaluator);
    metricsFilter.setOnMatch(FilterReply.DENY);
    metricsFilter.start();
    context
        .getLoggerList()
        .forEach(
            (logger) -> {
              logger
                  .iteratorForAppenders()
                  .forEachRemaining(
                      (appender) -> {
                        if (!appender.getName().equals(ASYNC_LOGSTASH_APPENDER_NAME)
                            && (!appender.getName().equals(CONSOLE_APPENDER_NAME)
                                || !useJsonFormat)) {
                          appender.setContext(context);
                          appender.addFilter(metricsFilter);
                          appender.start();
                        }
                      });
            });
  }

  private static LoggingEventCompositeJsonEncoder compositeJsonEncoder(
      LoggerContext context, String customFields) {
    final LoggingEventCompositeJsonEncoder compositeJsonEncoder =
        new LoggingEventCompositeJsonEncoder();
    compositeJsonEncoder.setContext(context);
    compositeJsonEncoder.setProviders(jsonProviders(context, customFields));
    compositeJsonEncoder.start();
    return compositeJsonEncoder;
  }

  private static LogstashEncoder logstashEncoder(String customFields) {
    final LogstashEncoder logstashEncoder = new LogstashEncoder();
    logstashEncoder.setThrowableConverter(throwableConverter());
    logstashEncoder.setCustomFields(customFields);
    return logstashEncoder;
  }

  private static LoggingEventJsonProviders jsonProviders(
      LoggerContext context, String customFields) {
    final LoggingEventJsonProviders jsonProviders = new LoggingEventJsonProviders();
    jsonProviders.addArguments(new ArgumentsJsonProvider());
    jsonProviders.addContext(new ContextJsonProvider<>());
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

  private static GlobalCustomFieldsJsonProvider<ILoggingEvent> customFieldsJsonProvider(
      String customFields) {
    final GlobalCustomFieldsJsonProvider<ILoggingEvent> customFieldsJsonProvider =
        new GlobalCustomFieldsJsonProvider<>();
    customFieldsJsonProvider.setCustomFields(customFields);
    return customFieldsJsonProvider;
  }

  private static LoggerNameJsonProvider loggerNameJsonProvider() {
    final LoggerNameJsonProvider loggerNameJsonProvider = new LoggerNameJsonProvider();
    loggerNameJsonProvider.setShortenedLoggerNameLength(20);
    return loggerNameJsonProvider;
  }

  private static StackTraceJsonProvider stackTraceJsonProvider() {
    StackTraceJsonProvider stackTraceJsonProvider = new StackTraceJsonProvider();
    stackTraceJsonProvider.setThrowableConverter(throwableConverter());
    return stackTraceJsonProvider;
  }

  private static ShortenedThrowableConverter throwableConverter() {
    final ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
    throwableConverter.setRootCauseFirst(true);
    return throwableConverter;
  }

  private static LoggingEventFormattedTimestampJsonProvider timestampJsonProvider() {
    final LoggingEventFormattedTimestampJsonProvider timestampJsonProvider =
        new LoggingEventFormattedTimestampJsonProvider();
    timestampJsonProvider.setTimeZone("UTC");
    timestampJsonProvider.setFieldName("timestamp");
    return timestampJsonProvider;
  }

  private static class LogbackLoggerContextListener extends ContextAwareBase
      implements LoggerContextListener {

    private final LogstashProperties logstashProperties;

    private final String customFields;

    private LogbackLoggerContextListener(
        LogstashProperties logstashProperties, String customFields) {
      this.logstashProperties = logstashProperties;
      this.customFields = customFields;
    }

    @Override
    public boolean isResetResistant() {
      return true;
    }

    @Override
    public void onStart(LoggerContext context) {
      if (this.logstashProperties.isUseJsonFormat()) {
        addJsonConsoleAppender(context, customFields);
      }
      if (this.logstashProperties.isEnabled()) {
        addLogstashTcpSocketAppender(context, customFields, logstashProperties);
      }
    }

    @Override
    public void onReset(LoggerContext context) {
      if (this.logstashProperties.isUseJsonFormat()) {
        addJsonConsoleAppender(context, customFields);
      }
      if (this.logstashProperties.isEnabled()) {
        addLogstashTcpSocketAppender(context, customFields, logstashProperties);
      }
    }

    @Override
    public void onStop(LoggerContext context) {}

    @Override
    public void onLevelChange(ch.qos.logback.classic.Logger logger, Level level) {}
  }
}
