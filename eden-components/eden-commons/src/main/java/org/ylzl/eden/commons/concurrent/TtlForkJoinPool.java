package org.ylzl.eden.commons.concurrent;

import com.alibaba.ttl.TtlRecursiveAction;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class TtlForkJoinPool {

	public static <T> void commonPoolSubmit(List<T> data, Consumer<List<T>> consumer, long thurshold) {
		ForkJoinPool.commonPool().submit(CollectionTtlRecursiveAction.<T>builder()
			.data(data)
			.consumer(consumer)
			.thurshold(thurshold)
			.build());
	}

	@RequiredArgsConstructor
	@Builder
	@ToString
	@EqualsAndHashCode(callSuper = false)
	@Data
	private static class CollectionTtlRecursiveAction<T> extends TtlRecursiveAction {

		private final List<T> data;

		private final Consumer<List<T>> consumer;

		private final long thurshold;

		@Override
		protected void compute() {
			int size = data.size();
			if (size <= thurshold) {
				consumer.accept(data);
			} else {
				int mid = size / 2;
				new CollectionTtlRecursiveAction<>(new ArrayList<>(data.subList(0, mid)), consumer, thurshold).fork();
				new CollectionTtlRecursiveAction<>(new ArrayList<>(data.subList(mid, size)), consumer, thurshold).fork();
			}
		}
	}

	/*public static void main(String[] args) throws InterruptedException {
		test(10000, 10);
		test(100000, 100);
		test(1000000, 500);
		test(5000000, 500);
		test(10000000, 500);
	}

	public static void test(int size, long thurshold) throws InterruptedException {
		System.out.println("\n数据量为：" + size);
		List<String> data = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			data.add("{\"code\":\"ETS-5BP0000\",\"message\":\"操作成功\",\"result\":\"" + i + "\"}");
		}

		long start2 = System.currentTimeMillis();
		data.forEach(TtlForkJoinPool::test);
		long end2 = System.currentTimeMillis();
		System.out.println("Foreach cost:" + (end2 - start2));
		Thread.sleep(3000);

		long start3 = System.currentTimeMillis();
		data.parallelStream().forEach(TtlForkJoinPool::test);
		long end3 = System.currentTimeMillis();
		System.out.println("ParallelStream cost:" + (end3 - start3));

		Thread.sleep(3000);

		long start1 = System.currentTimeMillis();
		TtlForkJoinPool.commonPoolSubmit(data, TtlForkJoinPool::test, thurshold);
		long end1 = System.currentTimeMillis();
		System.out.println("TtlForkJoinPool cost:" + (end1 - start1));

		Thread.sleep(3000);
	}

	public static void test(String e) {
		JacksonUtils.parseObject(e, JsonResult.class);
	}

	public static void test(List<String> list) {
		list.forEach(e -> JacksonUtils.parseObject(e, JsonResult.class));
	}

	@RequiredArgsConstructor
	@ToString
	@EqualsAndHashCode(callSuper = false)
	@Data
	public static class JsonResult {

		private String code;
		private String message;
		private String result;
	}*/
}
