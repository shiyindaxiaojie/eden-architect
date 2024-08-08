package org.ylzl.eden.arthas.spring.boot.attach;

import com.taobao.arthas.agent.attach.ArthasAgent;
import com.taobao.arthas.agent.attach.AttachArthasClassloader;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.zeroturnaround.zip.ZipUtil;

import java.arthas.SpyAPI;
import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 自定义 ArthasAgent
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class CustomArthasAgent extends ArthasAgent {

	private static final int TEMP_DIR_ATTEMPTS = 10000;

	private static final String ARTHAS_CORE_JAR = "arthas-core.jar";
	private static final String ARTHAS_BOOTSTRAP = "com.taobao.arthas.core.server.ArthasBootstrap";
	private static final String GET_INSTANCE = "getInstance";
	private static final String IS_BIND = "isBind";

	private static final Pattern ARTHAS_DIR_PATTERN = Pattern.compile("^arthas-(\\d{13})-.*$");
	private static final String ARTHAS_BIN_ZIP = "arthas-bin.zip";
	private static final String DESTROY = "destroy";

	private String errorMessage;

	private Map<String, String> configMap = new HashMap<String, String>();
	private String arthasHome;
	private boolean slientInit;
	private Instrumentation instrumentation;

	private Class<?> bootstrapClass;
	private Object bootstrap;

	public CustomArthasAgent() {
		this(null, null, false, null);
	}

	public CustomArthasAgent(Map<String, String> configMap) {
		this(configMap, null, false, null);
	}

	public CustomArthasAgent(String arthasHome) {
		this(null, arthasHome, false, null);
	}

	public CustomArthasAgent(Map<String, String> configMap, String arthasHome, boolean slientInit,
					   Instrumentation instrumentation) {
		if (configMap != null) {
			this.configMap = configMap;
		}

		this.arthasHome = arthasHome;
		this.slientInit = slientInit;
		this.instrumentation = instrumentation;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public void init() throws IllegalStateException {
		// 尝试判断arthas是否已在运行，如果是的话，直接就退出
		try {
			Class.forName("java.arthas.SpyAPI"); // 加载不到会抛异常
			if (SpyAPI.isInited()) {
				return;
			}
		} catch (Throwable e) {
			// ignore
		}

		try {
			if (instrumentation == null) {
				instrumentation = ByteBuddyAgent.install();
			}

			// 检查 arthasHome
			if (arthasHome == null || arthasHome.trim().isEmpty()) {
				// 解压出 arthasHome
				URL coreJarUrl = this.getClass().getClassLoader().getResource("arthas-bin.zip");
				if (coreJarUrl != null) {
					File tempArthasDir = createTempDir();
					ZipUtil.unpack(coreJarUrl.openStream(), tempArthasDir);
					arthasHome = tempArthasDir.getAbsolutePath();
				} else {
					throw new IllegalArgumentException("can not getResources arthas-bin.zip from classloader: "
						+ this.getClass().getClassLoader());
				}
			}

			// find arthas-core.jar
			File arthasCoreJarFile = new File(arthasHome, ARTHAS_CORE_JAR);
			if (!arthasCoreJarFile.exists()) {
				throw new IllegalStateException("can not find arthas-core.jar under arthasHome: " + arthasHome);
			}
			AttachArthasClassloader arthasClassLoader = new AttachArthasClassloader(
				new URL[] { arthasCoreJarFile.toURI().toURL() });

			bootstrapClass = arthasClassLoader.loadClass(ARTHAS_BOOTSTRAP);
			bootstrap = bootstrapClass.getMethod(GET_INSTANCE, Instrumentation.class, Map.class).invoke(null,
				instrumentation, configMap);
			boolean isBind = (Boolean) bootstrapClass.getMethod(IS_BIND).invoke(bootstrap);
			if (!isBind) {
				String errorMsg = "Arthas server port binding failed! Please check $HOME/logs/arthas/arthas.log for more details.";
				throw new RuntimeException(errorMsg);
			}
		} catch (Throwable e) {
			errorMessage = e.getMessage();
			if (!slientInit) {
				throw new IllegalStateException(e);
			}
		}
	}

	public void destroy() {
		if (bootstrapClass == null) {
			log.warn("Arthas is not already");
			return;
		}

		try {
			bootstrapClass.getMethod(DESTROY).invoke(bootstrap);
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	private static File createTempDir() {
		File baseDir = new File(System.getProperty("java.io.tmpdir"));
		String baseName = "arthas-" + System.currentTimeMillis() + "-";

		for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
			File tempDir = new File(baseDir, baseName + counter);
			if (tempDir.mkdir()) {
				return tempDir;
			}
		}
		throw new IllegalStateException("Failed to create directory within " + TEMP_DIR_ATTEMPTS + " attempts (tried "
			+ baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
	}
}
