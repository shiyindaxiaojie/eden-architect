//package org.ylzl.eden.spring.framework.extension;
//
//import lombok.RequiredArgsConstructor;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.atomic.AtomicBoolean;
//
///**
// * TODO
// *
// * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
// * @since 2.4.x
// */
//@RequiredArgsConstructor
//public class ExtensionLoaderManager {
//
//	private final ConcurrentMap<Class<?>, ExtensionLoader<?>> extensionLoadersMap = new ConcurrentHashMap<>(64);
//
//	private final AtomicBoolean destroyed = new AtomicBoolean();
//
//	private final ExtensionLoaderManager parent;
//
//
//	public <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
//		checkDestroyed();
//		checkType(type);
//
//		// 查找本地缓存
//		ExtensionLoader<T> loader = (ExtensionLoader<T>) extensionLoadersMap.get(type);
//		if (loader != null) {
//			return loader;
//		}
//
//		// 查找上级扩展点加载器
//		if (this.parent != null) {
//			loader = this.parent.getExtensionLoader(type);
//			if (loader != null) {
//				return loader ;
//			}
//		}
//
//		// 如果找不到，直接创建
//		return createExtensionLoader(type);
//	}
//
//	private <T> ExtensionLoader<T> createExtensionLoader(Class<T> type) {
//		ExtensionLoader<T> loader = null;
//		if (isScopeMatched(type)) {
//			// if scope is matched, just create it
//			loader = createExtensionLoader0(type);
//		}
//		return loader;
//	}
//
//	@SuppressWarnings("unchecked")
//	private <T> ExtensionLoader<T> createExtensionLoader0(Class<T> type) {
//		checkDestroyed();
//		ExtensionLoader<T> loader;
//		extensionLoadersMap.putIfAbsent(type, new ExtensionLoader<T>(type, this));
//		loader = (ExtensionLoader<T>) extensionLoadersMap.get(type);
//		return loader;
//	}
//
//	private <T> void checkType(Class<T> type) {
//		if (type == null) {
//			throw new IllegalArgumentException("Extension type is null");
//		}
//
//		if (!type.isInterface()) {
//			throw new IllegalArgumentException("Extension type `" + type + "` is not an interface");
//		}
//
//		if (!type.isAnnotationPresent(SPI.class)) {
//			throw new IllegalArgumentException("Extension type `" + type + "` is not annotated with @"
//				+ SPI.class.getSimpleName() + "");
//		}
//	}
//
//	private void checkDestroyed() {
//		if (destroyed.get()) {
//			throw new IllegalStateException("ExtensionDirector is destroyed");
//		}
//	}
//}
