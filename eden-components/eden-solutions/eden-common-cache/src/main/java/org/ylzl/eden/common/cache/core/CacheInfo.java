package org.ylzl.eden.common.cache.core;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 缓存信息
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Accessors(chain = true)
@Getter
@Setter
public class CacheInfo implements Serializable {

	/** 缓存实例ID */
	private String instanceId;

	/** 缓存名称 */
	private String name;

	/** 缓存类型 */
	private String type;

	/** 缓存key */
	private String key;

	/** 是否刷新 */
	private boolean refresh;

	/** 是否清除 */
	private boolean clear;
}
