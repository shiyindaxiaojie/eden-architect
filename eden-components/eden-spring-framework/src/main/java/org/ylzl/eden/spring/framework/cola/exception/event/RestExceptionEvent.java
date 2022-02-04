package org.ylzl.eden.spring.framework.cola.exception.event;

import org.ylzl.eden.spring.framework.cola.dto.Response;
import lombok.*;
import org.springframework.context.ApplicationEvent;

/**
 * Rest 异常事件
 *
 * @author gyl
 * @since 2.4.x
 */
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class RestExceptionEvent extends ApplicationEvent {

	private Response response;

	public RestExceptionEvent(Object source) {
		super(source);
	}
}
