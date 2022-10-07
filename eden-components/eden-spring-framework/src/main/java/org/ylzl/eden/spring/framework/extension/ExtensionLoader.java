//package org.ylzl.eden.spring.framework.extension;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.util.ClassUtils;
//import org.ylzl.eden.commons.collections.CollectionUtils;
//import org.ylzl.eden.commons.lang.ArrayUtils;
//import org.ylzl.eden.commons.lang.StringUtils;
//import org.ylzl.eden.spring.framework.extension.loader.LoadingStrategy;
//import org.ylzl.eden.spring.framework.extension.util.Holder;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//import static java.util.Arrays.asList;
//import static java.util.ServiceLoader.load;
//import static java.util.stream.StreamSupport.stream;
//
///**
// * 扩展点加载器
// *
// * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
// * @since 2.4.13
// */
//@RequiredArgsConstructor
//@Slf4j
//public class ExtensionLoader<T> {
//
//	private final Map<String, Set<String>> cachedActivateGroups = Collections.synchronizedMap(new LinkedHashMap<>());
//
//
//	private final ConcurrentMap<Class<?>, Object> extensionInstances = new ConcurrentHashMap<>(64);
//
//	private final ConcurrentMap<Class<?>, String> cachedNames = new ConcurrentHashMap<>();
//
//	private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();
//
//	private final Map<String, Object> cachedActivates = Collections.synchronizedMap(new LinkedHashMap<>());
//
//	private final Map<String, String[][]> cachedActivateValues = Collections.synchronizedMap(new LinkedHashMap<>());
//
//	private static volatile LoadingStrategy[] strategies = loadLoadingStrategies();
//
//	private AtomicBoolean destroyed = new AtomicBoolean();
//
//	private final Class<?> type;
//
//	private String cachedDefaultName;
//
//	/**
//	 * 获取扩展类对象
//	 *
//	 * @param name
//	 * @return
//	 */
//	public T getExtension(String name) {
//		T extension = getExtension(name, true);
//		if (extension == null) {
//			throw new IllegalArgumentException("Not find extension: " + name);
//		}
//		return extension;
//	}
//
//
//
//	public List<T> getActivateExtension(String[] values) {
//		return getActivateExtension(values, null);
//	}
//
//	public List<T> getActivateExtension(String[] values, String group) {
//		checkDestroyed();
//
//		List<String> names = values == null ? new ArrayList<>(0) : asList(values);
//		Set<String> namesSet = new HashSet<>(names);
//		// 如果没有匹配 `-default`，缓存所有扩展点
//		if (!namesSet.contains(ExtensionConstants.REMOVE_DEFAULT_KEY)) {
//			// 双重检查
//			if (cachedActivateGroups.size() == 0) {
//				synchronized (cachedActivateGroups) {
//					if (cachedActivateGroups.size() == 0) {
//						// 缓存扩展类
//						cacheExtensionClasses();
//
//						for (Map.Entry<String, Object> entry : cachedActivates.entrySet()) {
//							Object value = entry.getValue();
//							if (!(value instanceof Activate)) {
//								continue;
//							}
//
//							Activate activate = ((Activate) value);
//							String[] activateGroup = activate.group();
//							cachedActivateGroups.put(entry.getKey(), new HashSet<>(Arrays.asList(activateGroup)));
//
//							String[] activateValue = activate.value();
//							String[][] keyPairs = new String[activateValue.length][];
//
//
//							for (int i = 0; i < activateValue.length; i++) {
//								if (activateValue[i].contains(":")) {
//									keyPairs[i] = new String[2];
//									String[] arr = activateValue[i].split(":");
//									keyPairs[i][0] = arr[0];
//									keyPairs[i][1] = arr[1];
//								} else {
//									keyPairs[i] = new String[1];
//									keyPairs[i][0] = activateValue[i];
//								}
//							}
//							cachedActivateValues.put(entry.getKey(), keyPairs);
//						}
//					}
//				}
//			}
//
//			Map<Class<?>, T> activateExtensionsMap = new HashMap<>();
//			cachedActivateGroups.forEach((name, activateGroup) -> {
//				if (isMatchGroup(group, activateGroup)
//					&& !namesSet.contains(name)
//					&& !namesSet.contains(ExtensionConstants.REMOVE_VALUE_PREFIX + name)) {
//
//					activateExtensionsMap.put(getExtensionClass(name), getExtension(name));
//				}
//			});
//		}
//
//		if (namesSet.contains(DEFAULT_KEY)) {
//			// will affect order
//			// `ext1,default,ext2` means ext1 will happens before all of the default extensions while ext2 will after them
//			ArrayList<T> extensionsResult = new ArrayList<>(activateExtensionsMap.size() + names.size());
//			for (String name : names) {
//				if (name.startsWith(REMOVE_VALUE_PREFIX)
//					|| namesSet.contains(REMOVE_VALUE_PREFIX + name)) {
//					continue;
//				}
//				if (DEFAULT_KEY.equals(name)) {
//					extensionsResult.addAll(activateExtensionsMap.values());
//					continue;
//				}
//				if (containsExtension(name)) {
//					extensionsResult.add(getExtension(name));
//				}
//			}
//			return extensionsResult;
//		} else {
//			// add extensions, will be sorted by its order
//			for (String name : names) {
//				if (name.startsWith(REMOVE_VALUE_PREFIX)
//					|| namesSet.contains(REMOVE_VALUE_PREFIX + name)) {
//					continue;
//				}
//				if (DEFAULT_KEY.equals(name)) {
//					continue;
//				}
//				if (containsExtension(name)) {
//					activateExtensionsMap.put(getExtensionClass(name), getExtension(name));
//				}
//			}
//			return new ArrayList<>(activateExtensionsMap.values());
//		}
//	}
//
//	private Map<String, Class<?>> cacheExtensionClasses() {
//		Map<String, Class<?>> classes = cachedClasses.get();
//		if (classes == null) {
//			synchronized (cachedClasses) {
//				if (cachedClasses.get() == null) {
//					classes = loadExtensionClasses();
//					cachedClasses.set(classes);
//				}
//			}
//		}
//		return classes;
//	}
//
//	/**
//	 * 加载扩展类
//	 *
//	 * @return
//	 */
//	private Map<String, Class<?>> loadExtensionClasses() {
//		checkDestroyed();
//		cacheDefaultExtensionName();
//
//		Map<String, Class<?>> extensionClasses = new HashMap<>();
//
//		for (LoadingStrategy strategy : strategies) {
//			loadDirectory(extensionClasses, strategy, type.getName());
//		}
//
//		return extensionClasses;
//	}
//
//	/**
//	 * 加载扩展点所在目录
//	 *
//	 * @param extensionClasses
//	 * @param strategy
//	 * @param type
//	 */
//	private void loadDirectory(Map<String, Class<?>> extensionClasses, LoadingStrategy strategy, String type) {
//		loadDirectoryInternal(extensionClasses, strategy, type);
//		try {
//			String oldType = type.replace("org.apache", "com.alibaba");
//			if (oldType.equals(type)) {
//				return;
//			}
//			//if class not found,skip try to load resources
//			ClassUtils.forName(oldType);
//			loadDirectoryInternal(extensionClasses, strategy, oldType);
//		} catch (ClassNotFoundException classNotFoundException) {
//
//		}
//	}
//
//	private void loadDirectoryInternal(Map<String, Class<?>> extensionClasses, LoadingStrategy loadingStrategy, String type) {
//
//		String fileName = loadingStrategy.directory() + type;
//		try {
//			List<ClassLoader> classLoadersToLoad = new LinkedList<>();
//
//			// 尝试加载 ExtensionLoader 的类加载器
//			if (loadingStrategy.preferExtensionClassLoader()) {
//				ClassLoader extensionLoaderClassLoader = ExtensionLoader.class.getClassLoader();
//				if (ClassLoader.getSystemClassLoader() != extensionLoaderClassLoader) {
//					classLoadersToLoad.add(extensionLoaderClassLoader);
//				}
//			}
//
//			if (specialSPILoadingStrategyMap.containsKey(type)){
//				String internalDirectoryType = specialSPILoadingStrategyMap.get(type);
//				//skip to load spi when name don't match
//				if (!LoadingStrategy.ALL.equals(internalDirectoryType)
//					&& !internalDirectoryType.equals(loadingStrategy.getName())){
//					return;
//				}
//				classLoadersToLoad.clear();
//				classLoadersToLoad.add(ExtensionLoader.class.getClassLoader());
//			}else {
//				// load from scope model
//				Set<ClassLoader> classLoaders = scopeModel.getClassLoaders();
//
//				if (CollectionUtils.isEmpty(classLoaders)) {
//					Enumeration<java.net.URL> resources = ClassLoader.getSystemResources(fileName);
//					if (resources != null) {
//						while (resources.hasMoreElements()) {
//							loadResource(extensionClasses, null, resources.nextElement(), loadingStrategy.overridden(),
//								loadingStrategy.includedPackages(),
//								loadingStrategy.excludedPackages(),
//								loadingStrategy.onlyExtensionClassLoaderPackages());
//						}
//					}
//				} else {
//					classLoadersToLoad.addAll(classLoaders);
//				}
//			}
//
//			Map<ClassLoader, Set<java.net.URL>> resources = ClassLoaderResourceLoader.loadResources(fileName, classLoadersToLoad);
//			resources.forEach(((classLoader, urls) -> {
//				loadFromClass(extensionClasses, loadingStrategy.overridden(), urls, classLoader,
//					loadingStrategy.includedPackages(),
//					loadingStrategy.excludedPackages(),
//					loadingStrategy.onlyExtensionClassLoaderPackages());
//			}));
//		} catch (Throwable t) {
//			logger.error("Exception occurred when loading extension class (interface: " +
//				type + ", description file: " + fileName + ").", t);
//		}
//	}
//
//	/**
//	 * 缓存默认的扩展点名称
//	 */
//	private void cacheDefaultExtensionName() {
//		final SPI defaultAnnotation = type.getAnnotation(SPI.class);
//		if (defaultAnnotation == null) {
//			return;
//		}
//
//		String value = defaultAnnotation.value();
//		if ((value = value.trim()).length() > 0) {
//			String[] names = ExtensionConstants.COMMA_SPLIT_PATTERN.split(value);
//			if (names.length > 1) {
//				throw new IllegalStateException("More than 1 default extension name on extension "
//					+ type.getName() + ": " + Arrays.toString(names));
//			}
//			if (names.length == 1) {
//				cachedDefaultName = names[0];
//			}
//		}
//	}
//
//
//	/**
//	 * 匹配组名
//	 *
//	 * @param group
//	 * @param groups
//	 * @return
//	 */
//	private boolean isMatchGroup(String group, Set<String> groups) {
//		if (StringUtils.isEmpty(group)) {
//			return true;
//		}
//		if (CollectionUtils.isNotEmpty(groups)) {
//			return groups.contains(group);
//		}
//		return false;
//	}
//
//	public void destroy() {
//		if (!destroyed.compareAndSet(false, true)) {
//			return;
//		}
//	}
//
//	private void checkDestroyed() {
//		if (destroyed.get()) {
//			throw new IllegalStateException("ExtensionLoader is destroyed: " + type);
//		}
//	}
//
//	private static LoadingStrategy[] loadLoadingStrategies() {
//		return stream(load(LoadingStrategy.class).spliterator(), false)
//			.sorted()
//			.toArray(LoadingStrategy[]::new);
//	}
//}
