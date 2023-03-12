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

package org.ylzl.eden.data.auditor;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.ylzl.eden.spring.framework.json.support.JSONHelper;

import java.util.concurrent.TimeUnit;

/**
 * 数据脱敏基准测试
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Threads(1)
@Fork(3)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 3, time = 10)
@State(value = Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class DataMaskerBenchmark {

	private TestCase testCase;

	@Setup
	public void setUp() {
		testCase = TestCase.builder()
			.chineseName("梦想歌")
			.idCard("440101199103020011")
			.mobilePhone("13524678900")
			.address("广州市天河区梅赛德斯奔驰911室")
			.build();
	}

	@Benchmark
	public void jacksonToJSONString(Blackhole blackhole) {
		String json = JSONHelper.json("jackson").toJSONString(testCase);
		blackhole.consume(json);
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
			.include(DataMaskerBenchmark.class.getSimpleName())
			.result("result.json")
			.resultFormat(ResultFormatType.JSON).build();
		new Runner(opt).run();
	}
}
