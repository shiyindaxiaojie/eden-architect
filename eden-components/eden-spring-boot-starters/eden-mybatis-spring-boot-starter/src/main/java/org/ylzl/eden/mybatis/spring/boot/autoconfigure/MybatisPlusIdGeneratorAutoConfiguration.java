package org.ylzl.eden.mybatis.spring.boot.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.distributed.uid.SegmentGenerator;
import org.ylzl.eden.distributed.uid.SnowflakeGenerator;
import org.ylzl.eden.mybatis.spring.boot.env.MybatisPlusIdGeneratorProperties;
import org.ylzl.eden.mybatis.spring.boot.idgenerator.SegmentIdentifierGenerator;
import org.ylzl.eden.mybatis.spring.boot.idgenerator.SnowflakeIdentifierGenerator;

/**
 *  MybatisPlus ID 生成自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
@EnableConfigurationProperties({MybatisPlusIdGeneratorProperties.class})
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class MybatisPlusIdGeneratorAutoConfiguration {

	@ConditionalOnProperty(
		prefix = MybatisPlusIdGeneratorProperties.PREFIX,
		name = MybatisPlusIdGeneratorProperties.TYPE,
		havingValue = MybatisPlusIdGeneratorProperties.SNOWFLAKE
	)
	@Bean
	public SnowflakeIdentifierGenerator snowflakeIdentifierGenerator(SnowflakeGenerator snowflakeGenerator) {
		return new SnowflakeIdentifierGenerator(snowflakeGenerator);
	}

	@ConditionalOnProperty(
		prefix = MybatisPlusIdGeneratorProperties.PREFIX,
		name = MybatisPlusIdGeneratorProperties.TYPE,
		havingValue = MybatisPlusIdGeneratorProperties.SEGMENT
	)
	@Bean
	public SegmentIdentifierGenerator segmentIdentifierGenerator(SegmentGenerator segmentGenerator) {
		return new SegmentIdentifierGenerator(segmentGenerator);
	}
}
