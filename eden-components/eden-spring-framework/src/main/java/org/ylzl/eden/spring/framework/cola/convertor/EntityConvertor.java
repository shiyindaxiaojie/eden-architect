package org.ylzl.eden.spring.framework.cola.convertor;

import org.mapstruct.MappingTarget;

/**
 * 实体转换器(领域对象 <-> 数据库对象)
 *
 * @author gyl
 * @since 2.4.x
 */
public interface EntityConvertor<Entity, DO> {

	Entity toEntity(DO dataObject);

	DO toDataObject(Entity entity);

	void updateDOFromEntity(Entity entity, @MappingTarget DO dataObject);
}
