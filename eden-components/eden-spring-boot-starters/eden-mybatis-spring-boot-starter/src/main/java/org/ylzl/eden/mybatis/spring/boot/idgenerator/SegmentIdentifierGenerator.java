package org.ylzl.eden.mybatis.spring.boot.idgenerator;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.ylzl.eden.distributed.uid.SegmentGenerator;

/**
 * 号段ID生成器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SegmentIdentifierGenerator implements IdentifierGenerator {

	private SegmentGenerator segmentGenerator;

	@Override
	public Long nextId(Object entity) {
		return segmentGenerator.nextId();
	}
}
