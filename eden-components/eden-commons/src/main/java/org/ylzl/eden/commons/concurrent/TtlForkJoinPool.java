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
}
