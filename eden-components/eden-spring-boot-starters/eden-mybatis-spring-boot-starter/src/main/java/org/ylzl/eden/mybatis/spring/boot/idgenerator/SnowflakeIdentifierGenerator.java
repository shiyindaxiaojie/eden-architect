package org.ylzl.eden.mybatis.spring.boot.idgenerator;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import lombok.RequiredArgsConstructor;
import org.ylzl.eden.distributed.uid.SnowflakeGenerator;

/**
 * 雪花ID生成器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class SnowflakeIdentifierGenerator implements IdentifierGenerator {

	private final SnowflakeGenerator snowflakeGenerator;

	@Override
	public Long nextId(Object entity) {
		return snowflakeGenerator.nextId();
	}
}
