package org.ylzl.eden.commons.io;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.lang.ClassLoaderUtils;
import org.ylzl.eden.commons.lang.StringConstants;

import java.io.InputStream;
import java.net.URL;

/**
 * 资源操作工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class ResourceUtils {

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
		return ClassLoaderUtils.getClassLoader().getResource(relativeResource);
	}

	public static InputStream getInputStreamFromResource(String relativeResource) {
		return ClassLoaderUtils.getClassLoader().getResourceAsStream(relativeResource);
	}
}
