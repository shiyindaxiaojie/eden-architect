package org.ylzl.eden.cola.extension.register;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 扩展点坐标
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Accessors(chain = true)
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class ExtensionCoordinate {

	private final String extensionName;

	private final String uniqueIdentity;
}
