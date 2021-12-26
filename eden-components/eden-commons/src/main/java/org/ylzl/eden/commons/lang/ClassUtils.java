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

package org.ylzl.eden.commons.lang;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.env.CharsetConstants;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类工具集
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public class ClassUtils {

  public static final String SUB_PACKAGE_SCREEN_SUFFIX = ".*";

  public static final String SUB_PACKAGE_SCREEN_SUFFIX_RELACE = ".\\*";

  public static ClassLoader getClassLoader() {
    return Thread.currentThread().getContextClassLoader();
  }

  public static Class<?> loadClass(String className) throws ClassNotFoundException {
    return getClassLoader().loadClass(className);
  }

  public static URL getURLFromClassLoader() {
    return getURLFromResource(StringConstants.EMPTY);
  }

  public static String getPathFromResource(String relativeResource) {
    return getURLFromResource(relativeResource).getPath();
  }

  public static String getPathFromResource() {
    return getPathFromResource(StringConstants.EMPTY);
  }

  public static URL getURLFromResource(String relativeResource) {
    return getClassLoader().getResource(relativeResource);
  }

  public static InputStream getInputStreamFromResource(String relativeResource) {
    return getClassLoader().getResourceAsStream(relativeResource);
  }

  public static Set<Class<?>> getClasses(String pkg) throws IOException, ClassNotFoundException {
    boolean recursive = false;
    String[] pkgArr = {};

    if (pkg.lastIndexOf(SUB_PACKAGE_SCREEN_SUFFIX) != -1) {
      pkgArr = pkg.split(SUB_PACKAGE_SCREEN_SUFFIX_RELACE);
      if (pkgArr.length > 1) {
        pkg = pkgArr[0];
        for (int i = 0; i < pkgArr.length; i++) {
          pkgArr[i] = pkgArr[i].replace(SUB_PACKAGE_SCREEN_SUFFIX.substring(1), "");
        }
      } else {
        pkg = pkg.replace(SUB_PACKAGE_SCREEN_SUFFIX, "");
      }
      recursive = true;
    }

    Set<Class<?>> classSet = new LinkedHashSet<Class<?>>();
    String packageDirName = pkg.replace('.', CharConstants.SLASH);

    Enumeration<URL> dirs = getClassLoader().getResources(packageDirName);
    while (dirs.hasMoreElements()) {
      URL url = dirs.nextElement();
      String protocol = url.getProtocol();
      if ("file".equals(protocol)) {
        String filePath = URLDecoder.decode(url.getFile(), CharsetConstants.UTF_8_NAME);
        findClassesInPackage(pkg, pkgArr, recursive, classSet, filePath);
      } else if ("jar".equals(protocol)) {
        findClassesInPackage(pkg, pkgArr, recursive, classSet, url, packageDirName);
      }
    }
    return classSet;
  }

  private static void findClassesInPackage(
      String packageName,
      String[] packArr,
      final boolean recursive,
      Set<Class<?>> classes,
      String packagePath)
      throws ClassNotFoundException {
    File dir = new File(packagePath);
    if (!dir.exists() || !dir.isDirectory()) { // 如果不存在或者也不是目录直接返回
      return;
    }

    File[] dirfiles =
        dir.listFiles(
            new FileFilter() {
              public boolean accept(File file) { // 过滤子目录或以.class结尾的文件
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
              }
            });
    for (File file : dirfiles) {

      if (file.isDirectory()) { // 如果是目录 则继续扫描
        findClassesInPackage(
            packageName + "." + file.getName(),
            packArr,
            recursive,
            classes,
            file.getAbsolutePath());
      } else { // 如果是java类文件，去掉后面的.class 只留下类名
        String className = file.getName().substring(0, file.getName().length() - 6);
        String classUrl = packageName + StringConstants.DOT + className;
        if (classUrl.startsWith(StringConstants.DOT)) { // 判断是否是以.开头
          classUrl = classUrl.replaceFirst(StringConstants.DOT, StringConstants.EMPTY);
        }
        boolean flag = true;
        if (packArr.length > 1) {
          for (int i = 1; i < packArr.length; i++) {
            if (classUrl.indexOf(packArr[i]) <= -1) {
              flag = flag && false;
            } else {
              flag = flag && true;
            }
          }
        }
        if (flag) {
          classes.add(getClassLoader().loadClass(classUrl));
        }
      }
    }
  }

  private static void findClassesInPackage(
      String packageName,
      String[] packArr,
      final boolean recursive,
      Set<Class<?>> classes,
      URL url,
      String packageDirName)
      throws IOException, ClassNotFoundException {
    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
    Enumeration<JarEntry> entries = jar.entries();
    while (entries.hasMoreElements()) {
      JarEntry entry = entries.nextElement();
      String name = entry.getName();
      if (name.charAt(0) == CharConstants.SLASH) {
        name = name.substring(1);
      }

      if (name.startsWith(packageDirName)) {
        int idx = name.lastIndexOf(CharConstants.SLASH);
        if (idx != -1) {
          packageName = name.substring(0, idx).replace(CharConstants.SLASH, CharConstants.DOT);
        }

        if ((idx != -1) || recursive) {
          if (name.endsWith(".class") && !entry.isDirectory()) {
            String className = name.substring(packageName.length() + 1, name.length() - 6);

            boolean flag = true;
            if (packArr.length > 1) {
              for (int i = 1; i < packArr.length; i++) {
                if (packageName.indexOf(packArr[i]) <= -1) {
                  flag = flag && false;
                } else {
                  flag = flag && true;
                }
              }
            }

            if (flag) {
              classes.add(Class.forName(packageName + '.' + className));
            }
          }
        }
      }
    }
  }

  public static boolean isWrapClass(Class<?> clazz) {
    try {
      return ((Class<?>) clazz.getField("TYPE").get(null)).isPrimitive();
    } catch (Exception e) {
      return false;
    }
  }
}
