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
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

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
public class TtlForkJoinPoolInvokeTest {

	private final List<String> ten_thousand_data = new ArrayList<>(100000);
	private final List<String> million_data = new ArrayList<>(1000000);

	@Setup
	public void setup() {
		for (int i = 0; i < 100000; i++) {
			ten_thousand_data.add("1");
		}
		for (int i = 0; i < 1000000; i++) {
			million_data.add("2");
		}
	}

	@Benchmark
	public void ten_thousand_foreach(Blackhole blackhole) {
		blackhole.consume(ten_thousand_data.stream().reduce(String::concat).orElse(""));
	}

	@Benchmark
	public void ten_thousand_parallelStream(Blackhole blackhole) {
		blackhole.consume(ten_thousand_data.parallelStream().reduce(String::concat).orElse(""));
	}

	@Benchmark
	public void ten_thousand_commonPoolSubmit(Blackhole blackhole) {
		blackhole.consume(ForkJoinPool.commonPoolInvoke(ten_thousand_data,
			e -> e.stream().reduce(String::concat).orElse(""),
			r -> r.stream().reduce(String::concat).orElse(""), 500));
	}

	@Benchmark
	public void million_foreach(Blackhole blackhole) {
		blackhole.consume(million_data.stream().reduce(String::concat).orElse(""));
	}

	@Benchmark
	public void million_parallelStream(Blackhole blackhole) {
		blackhole.consume(million_data.parallelStream().reduce(String::concat).orElse(""));
	}

	@Benchmark
	public void million_commonPoolSubmit(Blackhole blackhole) {
		blackhole.consume(ForkJoinPool.commonPoolInvoke(million_data,
			e -> e.stream().reduce(String::concat).orElse(""),
			r -> r.stream().reduce(String::concat).orElse(""), 500));
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
			.include(TtlForkJoinPoolSubmitTest.class.getSimpleName())
			.result("result.json")
			.resultFormat(ResultFormatType.JSON).build();
		new Runner(opt).run();
	}
}
