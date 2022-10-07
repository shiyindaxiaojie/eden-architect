package org.ylzl.eden.commons.lang;

import lombok.experimental.UtilityClass;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 类加载器工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class ClassLoaderUtils {

	private static final ClassLoaderWeakCache<Constructor<?>> CONSTRUCTOR_CACHE = new ClassLoaderWeakCache<>();

	private static final ClassLoaderWeakCache<Class<?>> CLASS_CACHE = new ClassLoaderWeakCache<>();

	private static final boolean CLASS_CACHE_DISABLED = Boolean.getBoolean("classloading.cache.disabled");

	private static final Map<String, Class<?>> PRIMITIVE_CLASSES;

	private static final int MAX_PRIM_CLASS_NAME_LENGTH = 7;

	static {
		final Map<String, Class<?>> primitives = new HashMap<>(10, 1.0f);
		primitives.put("boolean", boolean.class);
		primitives.put("byte", byte.class);
		primitives.put("int", int.class);
		primitives.put("long", long.class);
		primitives.put("short", short.class);
		primitives.put("float", float.class);
		primitives.put("double", double.class);
		primitives.put("char", char.class);
		primitives.put("void", void.class);
		PRIMITIVE_CLASSES = Collections.unmodifiableMap(primitives);
	}

	public static <T> T newInstance(final ClassLoader classLoaderHint, final String className) throws Exception {
		final Class<?> primitiveClass = tryPrimitiveClass(className);
		if (primitiveClass != null) {
			return (T) primitiveClass.newInstance();
		}

		ClassLoader classLoader = classLoaderHint;
		if (classLoader == null) {
			classLoader = getClassLoader();
		}

		if (classLoader != null) {
			Constructor<?> constructor = CONSTRUCTOR_CACHE.get(classLoader, className);
			if (constructor != null) {
				return (T) constructor.newInstance();
			}
		}
		return newInstance0(classLoader, className);
	}

	private static <T> T newInstance0(ClassLoader classLoader, String className) throws Exception {
		Class<?> klass = classLoader == null ? Class.forName(className) : tryLoadClass(className, classLoader);
		final Constructor<?> constructor = klass.getDeclaredConstructor();
		if (!constructor.isAccessible()) {
			constructor.setAccessible(true);
		}
		if (classLoader != null) {
			CONSTRUCTOR_CACHE.put(classLoader, className, constructor);
		}
		return (T) constructor.newInstance();
	}

	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader() != null?
			Thread.currentThread().getContextClassLoader() :
			ClassLoaderUtils.class.getClassLoader();
	}

	public static Class<?> loadClass(String className) throws ClassNotFoundException {
		return loadClass(getClassLoader(), className);
	}

	public static Class<?> loadClass(final ClassLoader classLoaderHint,
									 final String className) throws ClassNotFoundException {
		final Class<?> primitiveClass = tryPrimitiveClass(className);
		if (primitiveClass != null) {
			return primitiveClass;
		}

		ClassLoader classLoader = classLoaderHint;
		if (classLoader != null) {
			try {
				return tryLoadClass(className, classLoader);
			} catch (ClassNotFoundException ignore) {
				classLoader = getClassLoader();
			}
		}

		if (classLoader != null) {
			return tryLoadClass(className, classLoader);
		}
		return Class.forName(className);
	}

	private static Class<?> tryLoadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
		Class<?> clazz;
		if (!CLASS_CACHE_DISABLED) {
			clazz = CLASS_CACHE.get(classLoader, className);
			if (clazz != null) {
				return clazz;
			}
		}

		if (className.startsWith("[")) {
			clazz = Class.forName(className, false, classLoader);
		} else {
			clazz = classLoader.loadClass(className);
		}

		if (!CLASS_CACHE_DISABLED) {
			CLASS_CACHE.put(classLoader, className, clazz);
		}
		return clazz;
	}


	private static Class<?> tryPrimitiveClass(String className) {
		if (className.length() <= MAX_PRIM_CLASS_NAME_LENGTH && Character.isLowerCase(className.charAt(0))) {
			return PRIMITIVE_CLASSES.get(className);
		}
		return null;
	}

	private static final class ClassLoaderWeakCache<V> {

		private final ConcurrentMap<ClassLoader, ConcurrentMap<String, WeakReference<V>>> cache;

		private ClassLoaderWeakCache() {
			this.cache = new ConcurrentHashMap<>(16);
		}

		private void put(ClassLoader classLoader, String className, V value) {
			ClassLoader cl = classLoader == null ? ClassLoaderUtils.class.getClassLoader() : classLoader;
			ConcurrentMap<String, WeakReference<V>> innerCache = cache.get(cl);
			if (innerCache == null) {
				innerCache = new ConcurrentHashMap<>(100);
				ConcurrentMap<String, WeakReference<V>> old = cache.putIfAbsent(cl, innerCache);
				if (old != null) {
					innerCache = old;
				}
			}
			innerCache.put(className, new WeakReference<V>(value));
		}

		public V get(ClassLoader classloader, String className) {
			ConcurrentMap<String, WeakReference<V>> innerCache = cache.get(classloader);
			if (innerCache == null) {
				return null;
			}
			WeakReference<V> reference = innerCache.get(className);
			V value = reference == null ? null : reference.get();
			if (reference != null && value == null) {
				innerCache.remove(className);
			}
			return value;
		}
	}
}
