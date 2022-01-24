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
package org.ylzl.eden.spring.framework.web.rest.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.ylzl.eden.commons.io.FileUtils;
import org.ylzl.eden.commons.lang.StringConstants;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.framework.core.constant.SpringFrameworkConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;

/**
 * Http 响应工具集
 *
 * @author gyl
 * @since 2.4.x
 */
@SuppressWarnings("unchecked")
@UtilityClass
public final class ResponseUtils {

  public static final String ACCEPT_RANGES = "bytes";

  public static final String CONTENT_DISPOSITION_ATTACH = "attachment;filename={0}";

  public static <X> ResponseEntity<X> wrapOrNotFound(X maybeResponse) {
    return wrapOrNotFound(maybeResponse, null);
  }

  public static <X> ResponseEntity<X> wrapOrNotFound(X maybeResponse, HttpHeaders header) {
    if (maybeResponse == null) {
      return new ResponseEntity<X>(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok().headers(header).body(maybeResponse);
  }

  /**
   * 请求头 <br>
   * Range：bytes=0-100 <br>
   *
   * <p>响应头 <br>
   * Content-Range：bytes 0-100/1234 <br>
   * Content-Length：101 <br>
   *
   * @param request
   * @param response
   * @param file
   * @return
   * @throws IOException
   */
  public static void download(HttpServletRequest request, HttpServletResponse response, File file)
      throws IOException {
    String fileName = file.getName();
    response.setHeader(
        HttpHeaders.CONTENT_DISPOSITION,
        MessageFormat.format(
            CONTENT_DISPOSITION_ATTACH,
            URLEncoder.encode(fileName, SpringFrameworkConstants.DEFAULT_ENCODING)));

    String contentType = request.getServletContext().getMimeType(fileName);
    response.setContentType(contentType);
    response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);

    long fileLength = file.length();
    if (StringUtils.isNotBlank(resolveRange(request))) {
      long startByte = resolveStartBytesFromRange(request); // 开始下载位置
      long endByte = resolveEndBytesFromRange(request); // 结束下载位置
      if (endByte == 0) {
        endByte = fileLength - 1;
      }
      response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
      long contentLength = endByte - startByte + 1;
      response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
      response.setHeader(HttpHeaders.ACCEPT_RANGES, ACCEPT_RANGES);
      response.setHeader(
          HttpHeaders.CONTENT_RANGE,
          StringUtils.join(
              ACCEPT_RANGES,
              StringConstants.SPACE,
              startByte,
              StringConstants.MINUS,
              endByte,
              StringConstants.SLASH,
              fileLength));

      FileUtils.seek(file, response.getOutputStream(), startByte, endByte);
      response.flushBuffer();
    }
    response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength));
    FileUtils.allocateDirect(file.getAbsolutePath(), response.getOutputStream());
  }

  public static String resolveRange(HttpServletRequest request) {
    String range = request.getHeader(HttpHeaders.RANGE);
    if (range != null
        && range.contains(StringUtils.join(ACCEPT_RANGES, StringConstants.EQ))
        && range.contains(StringConstants.MINUS)) {
      return range;
    }
    return null;
  }

  public static long resolveStartBytesFromRange(HttpServletRequest request)
      throws NumberFormatException {
    String range = resolveRange(request);
    if (StringUtils.isBlank(range)) {
      return -1;
    }
    long startByte = 0;
    String[] ranges =
        range
            .substring(range.lastIndexOf(StringConstants.EQ) + 1)
            .trim()
            .split(StringConstants.MINUS);
    if (ranges.length == 1) {
      if (range.endsWith(StringConstants.MINUS)) { // bytes=1234-
        startByte = Long.parseLong(ranges[0]);
      }
    } else if (ranges.length == 2) { // bytes=1234-9999
      startByte = Long.parseLong(ranges[0]);
    }
    return startByte;
  }

  public static long resolveEndBytesFromRange(HttpServletRequest request)
      throws NumberFormatException {
    String range = resolveRange(request);
    if (StringUtils.isBlank(range)) {
      return -1;
    }
    long endByte = 0;
    String[] ranges =
        range
            .substring(range.lastIndexOf(StringConstants.EQ) + 1)
            .trim()
            .split(StringConstants.MINUS);
    if (ranges.length == 1) {
      if (range.startsWith(StringConstants.MINUS)) { // bytes=-1234
        endByte = Long.parseLong(ranges[0]);
      }
    } else if (ranges.length == 2) { // bytes=1234-9999
      endByte = Long.parseLong(ranges[1]);
    }
    return endByte;
  }
}
