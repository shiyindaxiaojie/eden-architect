package org.ylzl.eden.spring.framework.beans.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 * MultiValueMap 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class MultiValueMapUtils {

	public static MultiValueMap<String, Object> toMap(Object obj) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		Field[] fields = obj.getClass().getDeclaredFields();
		int i = 0;
		for (int len = fields.length; i < len; ++i) {
			String varName = fields[i].getName();
			boolean accessFlag = fields[i].isAccessible();
			fields[i].setAccessible(true);
			Object o;
			try {
				o = fields[i].get(obj);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			if (o != null) {
				map.put(varName, Collections.singletonList(o.toString()));
			}
			fields[i].setAccessible(accessFlag);
		}
		return map;
	}
}
