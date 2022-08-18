package org.ylzl.eden.spring.integration.businessprocess;

import lombok.*;

/**
 * 订单
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class Order {

	private Long orderNo;

	private Integer stock;
}
