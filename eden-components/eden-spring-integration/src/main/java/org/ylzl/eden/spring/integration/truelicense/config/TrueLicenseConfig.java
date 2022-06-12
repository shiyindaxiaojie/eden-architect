package org.ylzl.eden.spring.integration.truelicense.config;

import lombok.*;

/**
 * TrueLicense 配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class TrueLicenseConfig {

	private String subject;

	private String licensePath;

	private String storePass;

	private String keyPass;

	private String privateKeysStorePath;

	private String privateAlias;

	private String publicKeysStorePath;

	private String publicAlias;
}
