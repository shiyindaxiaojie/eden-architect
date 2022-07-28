package org.ylzl.eden.spring.integration.leaf;

/**
 * ID 生成器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface IDGen {

	/**
	 * 生成 UID
	 *
	 * @return
	 */
	long generateUID();

	/**
	 * 设置 Key
	 *
	 * @param key
	 */
	void setKey(String key);
}
