package org.ylzl.eden.spring.framework.cola.assembler;

/**
 * 数据传输对象组装器(数据传输对象 <-> 领域对象)
 *
 * @author gyl
 * @since 2.4.x
 */
public interface DTOAssembler<DTO, Entity> {

	/**
	 * DTO 转 Entity
	 *
	 * @param dto
	 * @return
	 */
	Entity toEntity(DTO dto);

	/**
	 * Entity 转 DTO
	 *
	 * @param entity
	 * @return
	 */
	DTO toDTO(Entity entity);
}
