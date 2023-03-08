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

package org.ylzl.eden.commons.concurrent;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.ylzl.eden.commons.json.JacksonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Threads(Threads.MAX)
@Fork(1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
@State(value = Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class TtlForkJoinPoolTest {

	private final TestCase testCase = TestCase.builder()
		.chineseName("梦想歌")
		.idCard("440101199103020011")
		.mobilePhone("13524678900")
		.address("广州市天河区梅赛德斯奔驰911室")
		.build();

	private final List<TestCase> ten_thousand_data = new ArrayList<>(100000);
	private final List<TestCase> million_data = new ArrayList<>(1000000);
	private final List<TestCase> ten_million_data = new ArrayList<>(10000000);

	@Setup
	public void setup() {
		for (int i = 0; i < 100000; i++) {
			ten_thousand_data.add(testCase);
		}
		for (int i = 0; i < 1000000; i++) {
			million_data.add(testCase);
		}
		for (int i = 0; i < 10000000; i++) {
			ten_million_data.add(testCase);
		}
	}

	@Benchmark
	public void ten_thousand_foreach() {
		ten_thousand_data.forEach(JacksonUtils::toJSONString);
	}

	@Benchmark
	public void ten_thousand_parallelStream() {
		ten_thousand_data.parallelStream().forEach(JacksonUtils::toJSONString);
	}

	@Benchmark
	public void ten_thousand_commonPoolSubmit() {
		TtlForkJoinPool.commonPoolSubmit(ten_thousand_data, JacksonUtils::toJSONString, 500);
	}

	@Benchmark
	public void million_foreach() {
		million_data.forEach(JacksonUtils::toJSONString);
	}

	@Benchmark
	public void million_parallelStream() {
		million_data.parallelStream().forEach(JacksonUtils::toJSONString);
	}

	@Benchmark
	public void million_commonPoolSubmit() {
		TtlForkJoinPool.commonPoolSubmit(million_data, JacksonUtils::toJSONString, 500);
	}

	@Benchmark
	public void ten_million_foreach() {
		ten_million_data.forEach(JacksonUtils::toJSONString);
	}

	@Benchmark
	public void ten_million_parallelStream() {
		ten_million_data.parallelStream().forEach(JacksonUtils::toJSONString);
	}

	@Benchmark
	public void ten_million_commonPoolSubmit() {
		TtlForkJoinPool.commonPoolSubmit(ten_million_data, JacksonUtils::toJSONString, 500);
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
			.include(TtlForkJoinPoolTest.class.getSimpleName())
			.result("result.json")
			.resultFormat(ResultFormatType.JSON).build();
		new Runner(opt).run();
	}
}
