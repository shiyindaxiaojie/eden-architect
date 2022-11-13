package org.ylzl.eden.idempotent.spring.boot.env;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.ylzl.eden.idempotent.config.IdempotentTokenConfig;

/**
 * 幂等配置转换器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IdempotentTokenConvertor {

	IdempotentTokenConvertor INSTANCE = Mappers.getMapper(IdempotentTokenConvertor.class);

	IdempotentTokenConfig toConfig(IdempotentTokenProperties properties);
}
