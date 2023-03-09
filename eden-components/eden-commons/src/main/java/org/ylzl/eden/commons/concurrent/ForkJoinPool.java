package org.ylzl.eden.commons.concurrent;

import com.alibaba.ttl.TtlRecursiveAction;
import com.alibaba.ttl.TtlRecursiveTask;
import lombok.*;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * ForkJoinPool
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class ForkJoinPool {

	private static final int DEFAULT_THRESHOLD = 100;

	/**
	 * 并行处理
	 *
	 * @param data      数据
	 * @param worker    执行函数
	 * @param threshold 阈值
	 * @param <T>       泛型
	 */
	public static <T> void commonPoolSubmit(List<T> data, Consumer<List<T>> worker, long threshold) {
		java.util.concurrent.ForkJoinPool.commonPool().submit(CollectionTtlRecursiveAction.<T>builder()
			.data(data)
			.worker(worker)
			.threshold(threshold)
			.build());
	}

	/**
	 * 并行处理
	 *
	 * @param data   数据
	 * @param worker 执行函数
	 * @param <T>    泛型
	 */
	public static <T> void commonPoolSubmit(List<T> data, Consumer<List<T>> worker) {
		java.util.concurrent.ForkJoinPool.commonPool()
			.submit(CollectionTtlRecursiveAction.<T>builder()
			.data(data)
			.worker(worker)
			.threshold(DEFAULT_THRESHOLD)
			.build());
	}

	/**
	 * 并行处理
	 *
	 * @param data      数据
	 * @param worker    执行函数
	 * @param merge     合并结果
	 * @param threshold 阈值
	 * @param <T>       泛型
	 */
	public static <T, R> R commonPoolInvoke(List<T> data, Function<List<T>, R> worker,
											   Function<List<R>, R> merge, long threshold) {
		java.util.concurrent.ForkJoinPool forkJoinPool = new java.util.concurrent.ForkJoinPool();
		return forkJoinPool
			.invoke(CollectionTtlRecursiveTask.<T, R>builder()
			.data(data)
			.worker(worker)
			.merge(merge)
			.threshold(threshold)
			.build());
	}

	/**
	 * 并行处理
	 *
	 * @param data      数据
	 * @param worker    执行函数
	 * @param merge     合并结果
	 * @param <T>       泛型
	 */
	public static <T, R> R commonPoolInvoke(List<T> data, Function<List<T>, R> worker,
											Function<List<R>, R> merge) {
		return java.util.concurrent.ForkJoinPool.commonPool()
			.invoke(CollectionTtlRecursiveTask.<T, R>builder()
				.data(data)
				.worker(worker)
				.merge(merge)
				.threshold(DEFAULT_THRESHOLD)
				.build());
	}

	@RequiredArgsConstructor
	@Builder
	@ToString
	@EqualsAndHashCode(callSuper = false)
	@Data
	private static class CollectionTtlRecursiveAction<T> extends TtlRecursiveAction {

		private final List<T> data;

		private final Consumer<List<T>> worker;

		private final long threshold;

		@Override
		protected void compute() {
			int size = data.size();
			if (size <= threshold) {
				worker.accept(data);
			} else {
				int mid = size / 2;
				new CollectionTtlRecursiveAction<>(new ArrayList<>(data.subList(0, mid)), worker, threshold).fork();
				new CollectionTtlRecursiveAction<>(new ArrayList<>(data.subList(mid, size)), worker, threshold).fork();
			}
		}
	}

	@RequiredArgsConstructor
	@Builder
	@ToString
	@EqualsAndHashCode(callSuper = false)
	@Data
	private static class CollectionTtlRecursiveTask<T, R> extends TtlRecursiveTask<R> {

		private final List<T> data;

		private final Function<List<T>, R> worker;

		private final Function<List<R>, R> merge;

		private final long threshold;

		@Override
		protected R compute() {
			int size = data.size();
			if (size <= threshold) {
				return worker.apply(data);
			} else {
				int mid = size / 2;
				CollectionTtlRecursiveTask<T, R> left = new CollectionTtlRecursiveTask<>(
					new ArrayList<>(data.subList(0, mid)), worker, merge, threshold);
				CollectionTtlRecursiveTask<T, R> right = new CollectionTtlRecursiveTask<>(
					new ArrayList<>(data.subList(mid, size)), worker, merge, threshold);
				left.fork();
				right.fork();
				List<R> results = new ArrayList<>();
				results.add(left.join());
				results.add(right.join());
				return merge.apply(results);
			}
		}
	}
}
