package org.ylzl.eden.mybatis.spring.boot.idgenerator;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import lombok.RequiredArgsConstructor;
import org.ylzl.eden.distributed.uid.SegmentGenerator;

/**
 * 号段ID生成器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class SegmentIdentifierGenerator implements IdentifierGenerator {

	private final SegmentGenerator segmentGenerator;

	@Override
	public Long nextId(Object entity) {
		return segmentGenerator.nextId();
	}
}
