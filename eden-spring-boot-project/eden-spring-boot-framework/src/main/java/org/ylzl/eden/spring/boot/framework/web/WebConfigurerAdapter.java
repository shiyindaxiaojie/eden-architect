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

package org.ylzl.eden.spring.boot.framework.web;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlets.MetricsServlet;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.AbstractEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.ylzl.eden.spring.boot.commons.lang.StringConstants;
import org.ylzl.eden.spring.boot.commons.lang.StringUtils;
import org.ylzl.eden.spring.boot.framework.core.FrameworkConstants;
import org.ylzl.eden.spring.boot.framework.core.FrameworkProperties;
import org.ylzl.eden.spring.boot.framework.core.ProfileConstants;
import org.ylzl.eden.spring.boot.framework.web.filter.CachingHttpHeadersFilter;

import javax.servlet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.EnumSet;

/**
 * Web 配置适配器
 *
 * @author gyl
 * @since 0.0.1
 */
@EnableRestErrorAdvice
@Slf4j
public class WebConfigurerAdapter
    implements EmbeddedServletContainerCustomizer, ServletContextInitializer {

  private static final String MSG_INJECT_CACHE_HTTP_HEADER_FILTER =
      "Inject cached HttpHeaders filter";

  private static final String MSG_INJECT_METRICSR_REGISTRY = "Inject Metrics Registry";

  private static final String MSG_INJECT_METRICSR_FILTER = "Inject Metrics Filter";

  private static final String MSG_INJECT_METRICS_SERVLET = "Inject Metrics Servlet";

  private static final String MSG_UNSUPPORTED_CONTAINER = "Unsupported container";

  private static final String DEFAULT_OUTPUT_DIR = "target/";

  @Setter
  @Autowired(required = false)
  private MetricRegistry metricRegistry;

  private final FrameworkProperties frameworkProperties;

  private final Environment environment;

  public WebConfigurerAdapter(FrameworkProperties frameworkProperties, Environment environment) {
    this.frameworkProperties = frameworkProperties;
    this.environment = environment;
  }

  @Override
  public void customize(ConfigurableEmbeddedServletContainer container) {
    this.setMimeMappings(container);
    this.setLocationForStaticAssets(container);
  }

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    EnumSet<DispatcherType> disps =
        EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
    if (environment.acceptsProfiles(ProfileConstants.SPRING_PROFILE_PRODUCTION)) {
      this.initCachingHttpHeadersFilter(servletContext, disps, "/i18n/*", "/content/*", "/app/*");
    }
    if (metricRegistry != null) {
      this.initMetrics(servletContext, disps);
    }
  }

  protected void initCachingHttpHeadersFilter(
      ServletContext servletContext, EnumSet<DispatcherType> disps, String... urlPatterns) {
    log.debug(MSG_INJECT_CACHE_HTTP_HEADER_FILTER);
    FilterRegistration.Dynamic cachingHttpHeadersFilter =
        servletContext.addFilter(
            "cachingHttpHeadersFilter", new CachingHttpHeadersFilter(frameworkProperties));
    for (String urlPattern : urlPatterns) {
      cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, urlPattern);
    }
    cachingHttpHeadersFilter.setAsyncSupported(true);
  }

  protected void initMetrics(ServletContext servletContext, EnumSet<DispatcherType> disps) {
    log.debug(MSG_INJECT_METRICSR_REGISTRY);
    servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE, metricRegistry);
    servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY, metricRegistry);

    log.debug(MSG_INJECT_METRICSR_FILTER);
    FilterRegistration.Dynamic metricsFilter =
        servletContext.addFilter("webappMetricsFilter", new InstrumentedFilter());
    metricsFilter.addMappingForUrlPatterns(disps, true, "/*");
    metricsFilter.setAsyncSupported(true);

    log.debug(MSG_INJECT_METRICS_SERVLET);
    ServletRegistration.Dynamic metricsServlet =
        servletContext.addServlet("metricsServlet", new MetricsServlet());
    metricsServlet.addMapping("/metrics/*");
    metricsServlet.setAsyncSupported(true);
    metricsServlet.setLoadOnStartup(2);
  }

  protected void setMimeMappings(ConfigurableEmbeddedServletContainer container) {
    if (container instanceof UndertowEmbeddedServletContainerFactory) {
      MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
      mappings.add(
          "html", MediaType.TEXT_HTML_VALUE + ";charset=" + FrameworkConstants.DEFAULT_ENCODING);
      mappings.add(
          "json", MediaType.TEXT_HTML_VALUE + ";charset=" + FrameworkConstants.DEFAULT_ENCODING);

      UndertowEmbeddedServletContainerFactory undertow =
          (UndertowEmbeddedServletContainerFactory) container;
      undertow.setMimeMappings(mappings);
    }
  }

  protected void setLocationForStaticAssets(ConfigurableEmbeddedServletContainer container) {
    String pathPrefix = resolvePathPrefix();
    String webappPath = StringUtils.join(pathPrefix, DEFAULT_OUTPUT_DIR, "classes/static/");
    File root = new File(webappPath);
    if (root.exists() || root.isDirectory()) {
      this.setDocumentRoot(container, root);
    }
  }

  private void setDocumentRoot(ConfigurableEmbeddedServletContainer container, File root) {
    AbstractEmbeddedServletContainerFactory factory = null;
    if (container instanceof UndertowEmbeddedServletContainerFactory) {
      factory = (UndertowEmbeddedServletContainerFactory) container;
    } else if (container instanceof TomcatEmbeddedServletContainerFactory) {
      factory = (TomcatEmbeddedServletContainerFactory) container;
    } else if (container instanceof JettyEmbeddedServletContainerFactory) {
      factory = (JettyEmbeddedServletContainerFactory) container;
    } else {
      throw new UnsupportedOperationException(MSG_UNSUPPORTED_CONTAINER);
    }
    factory.setDocumentRoot(root);
  }

  private String resolvePathPrefix() {
    String fullExecutablePath;
    try {
      fullExecutablePath =
          URLDecoder.decode(
              this.getClass().getResource(StringConstants.EMPTY).getPath(),
              StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      fullExecutablePath = this.getClass().getResource(StringConstants.EMPTY).getPath();
    }

    String rootPath = Paths.get(StringConstants.DOT).toUri().normalize().getPath();
    String extractedPath = fullExecutablePath.replace(rootPath, StringConstants.EMPTY);
    int extractionEndIndex = extractedPath.indexOf(DEFAULT_OUTPUT_DIR);
    if (extractionEndIndex <= 0) {
      return StringConstants.EMPTY;
    }
    return extractedPath.substring(0, extractionEndIndex);
  }
}
