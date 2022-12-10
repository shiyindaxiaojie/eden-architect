/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.test.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Feature;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.distribution.Versions;
import de.flapdoodle.embed.mongo.tests.MongosSystemForTestFactory;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import de.flapdoodle.embed.process.distribution.GenericVersion;
import de.flapdoodle.embed.process.io.Processors;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.rules.ExternalResource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * 嵌入式的 MongoDB
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class EmbeddedMongoDB extends ExternalResource {

	private static final String LOCALHOST = "127.0.0.1";

	private static final String DEFAULT_REPLICA_SET_NAME = "rs0";

	private static final String DEFAULT_CONFIG_SERVER_REPLICA_SET_NAME = "rs-config";

	private static final String STORAGE_ENGINE = "wiredTiger";

	private static final IFeatureAwareVersion VERSION =
		Versions.withFeatures(
			new GenericVersion("3.7.9"),
			Feature.ONLY_WITH_SSL,
			Feature.ONLY_64BIT,
			Feature.NO_HTTP_INTERFACE_ARG,
			Feature.STORAGE_ENGINE,
			Feature.MONGOS_CONFIGDB_SET_STYLE,
			Feature.NO_CHUNKSIZE_ARG);

	private final TestResource resource;

	private EmbeddedMongoDB(TestResource resource) {
		this.resource = resource;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder replSet() {
		return replSet(DEFAULT_REPLICA_SET_NAME);
	}

	public static Builder replSet(String replicaSetName) {
		return new Builder().withReplicaSetName(replicaSetName);
	}

	private static Integer randomOrDefaultServerPort() {
		try {
			return Network.getFreeServerPort();
		} catch (IOException e) {
			return 27017;
		}
	}

	private static IMongoCmdOptions defaultCommandOptions() {
		return new MongoCmdOptionsBuilder() //
			.useNoPrealloc(false) //
			.useSmallFiles(false) //
			.useNoJournal(false) //
			.useStorageEngine(STORAGE_ENGINE) //
			.verbose(false) //
			.build();
	}

	private static IMongodConfig defaultMongodConfig(
		IFeatureAwareVersion version,
		int port,
		IMongoCmdOptions cmdOptions,
		boolean configServer,
		boolean shardServer,
		String replicaSet) {

		try {

			MongodConfigBuilder builder =
				new MongodConfigBuilder() //
					.version(version) //
					.withLaunchArgument("--quiet") //
					.net(new Net(LOCALHOST, port, Network.localhostIsIPv6())) //
					.configServer(configServer)
					.cmdOptions(cmdOptions); //

			if (StringUtils.hasText(replicaSet)) {

				builder =
					builder //
						.replication(new Storage(null, replicaSet, 0));

				if (!configServer) {
					builder = builder.shardServer(shardServer);
				} else {
					builder = builder.shardServer(false);
				}
			}

			return builder.build();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static IMongosConfig defaultMongosConfig(
		IFeatureAwareVersion version,
		int port,
		IMongoCmdOptions cmdOptions,
		String configServerReplicaSet,
		int configServerPort) {

		try {
			MongosConfigBuilder builder =
				new MongosConfigBuilder() //
					.version(version) //
					.withLaunchArgument("--quiet", null) //
					.net(new Net(LOCALHOST, port, Network.localhostIsIPv6())) //
					.cmdOptions(cmdOptions);

			if (StringUtils.hasText(configServerReplicaSet)) {
				builder =
					builder
						.replicaSet(configServerReplicaSet) //
						.configDB(LOCALHOST + ":" + configServerPort);
			}

			return builder.build();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void before() {
		resource.start();
	}

	@Override
	protected void after() {
		resource.stop();
	}

	public MongoClient getMongoClient() {
		return resource.mongoClient();
	}

	public String getConnectionString() {
		return resource.connectionString();
	}

	interface TestResource {

		void start();

		void stop();

		String connectionString();

		default MongoClient mongoClient() {
			return new MongoClient(new MongoClientURI(connectionString()));
		}
	}

	public static class Builder {

		IFeatureAwareVersion version;
		String replicaSetName;
		List<Integer> serverPorts;
		List<Integer> configServerPorts;
		boolean silent = true;

		Builder() {
			version = VERSION;
			replicaSetName = null;
			serverPorts = Collections.emptyList();
			configServerPorts = Collections.emptyList();
		}

		public Builder withVersion(IFeatureAwareVersion version) {
			this.version = version;
			return this;
		}

		public Builder withReplicaSetName(String replicaSetName) {
			this.replicaSetName = replicaSetName;
			return this;
		}

		public Builder withServerPorts(Integer... ports) {
			this.serverPorts = Arrays.asList(ports);
			return this;
		}

		public Builder withSilent(boolean silent) {
			this.silent = silent;
			return this;
		}

		public EmbeddedMongoDB configure() {
			if (serverPorts.size() > 1 || StringUtils.hasText(replicaSetName)) {
				String rsName =
					StringUtils.hasText(replicaSetName) ? replicaSetName : DEFAULT_REPLICA_SET_NAME;
				return new EmbeddedMongoDB(
					new ReplSet(
						version, rsName, silent, serverPorts.toArray(new Integer[serverPorts.size()])));
			}
			throw new UnsupportedOperationException("implement me");
		}
	}

	static class ReplSet implements TestResource {

		private static final String DEFAULT_SHARDING = "none";
		private static final String DEFAULT_SHARD_KEY = "_class";

		private final IFeatureAwareVersion serverVersion;
		private final String configServerReplicaSetName;
		private final String replicaSetName;
		private final int mongosPort;
		private final Integer[] serverPorts;
		private final Integer[] configServerPorts;
		private final Function<Command, ProcessOutput> outputFunction;

		private MongosSystemForTestFactory mongosTestFactory;

		ReplSet(
			IFeatureAwareVersion serverVersion,
			String replicaSetName,
			boolean silent,
			Integer... serverPorts) {
			this.serverVersion = serverVersion;
			this.replicaSetName = replicaSetName;
			this.serverPorts = defaultPortsIfRequired(serverPorts);
			this.configServerPorts = defaultPortsIfRequired(null);
			this.configServerReplicaSetName = DEFAULT_CONFIG_SERVER_REPLICA_SET_NAME;
			this.mongosPort = randomOrDefaultServerPort();
			if (silent) {
				outputFunction =
					it ->
						new ProcessOutput(
							Processors.silent(),
							Processors.namedConsole("[ " + it.commandName() + " error]"),
							Processors.console());
			} else {
				outputFunction = it -> ProcessOutput.getDefaultInstance(it.commandName());
			}
		}

		Integer[] defaultPortsIfRequired(Integer[] ports) {
			if (!ObjectUtils.isEmpty(ports)) {
				return ports;
			}
			try {
				return new Integer[]{
					Network.getFreeServerPort(), Network.getFreeServerPort(), Network.getFreeServerPort()
				};
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void start() {
			if (mongosTestFactory != null) {
				return;
			}
			doStart();
		}

		private void doStart() {
			Map<String, List<IMongodConfig>> replicaSets = new LinkedHashMap<>();
			replicaSets.put(configServerReplicaSetName, initConfigServers());
			replicaSets.put(replicaSetName, initReplicaSet());
			IMongosConfig mongosConfig =
				defaultMongosConfig(
					serverVersion,
					mongosPort,
					defaultCommandOptions(),
					configServerReplicaSetName,
					configServerPorts[0]);

			mongosTestFactory =
				new MongosSystemForTestFactory(
					mongosConfig,
					replicaSets,
					Collections.emptyList(),
					DEFAULT_SHARDING,
					DEFAULT_SHARDING,
					DEFAULT_SHARD_KEY);

			try {
				mongosTestFactory.start();
			} catch (Throwable e) {
				throw new RuntimeException(" Error while starting cluster. ", e);
			}
		}

		private List<IMongodConfig> initReplicaSet() {
			List<IMongodConfig> rs = new ArrayList<>();
			for (int port : serverPorts) {
				rs.add(
					defaultMongodConfig(
						serverVersion, port, defaultCommandOptions(), false, true, replicaSetName));
			}
			return rs;
		}

		private List<IMongodConfig> initConfigServers() {
			List<IMongodConfig> configServers = new ArrayList<>(configServerPorts.length);
			for (Integer port : configServerPorts) {
				configServers.add(
					defaultMongodConfig(
						serverVersion,
						port,
						defaultCommandOptions(),
						true,
						false,
						configServerReplicaSetName));
			}
			return configServers;
		}

		@Override
		public void stop() {
			if (mongosTestFactory != null) {
				mongosTestFactory.stop();
			}
		}

		@Override
		public String connectionString() {
			return "mongodb://localhost:" + serverPorts[0] + "/?replicaSet=" + replicaSetName;
		}
	}
}
