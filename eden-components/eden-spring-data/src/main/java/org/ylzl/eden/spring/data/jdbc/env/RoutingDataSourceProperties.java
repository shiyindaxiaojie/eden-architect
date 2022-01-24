package org.ylzl.eden.spring.data.jdbc.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.spring.data.core.constant.SpringDataConstants;

import java.util.List;

/**
 * 动态数据源配置属性
 *
 * @author gyl
 * @since 2.4.x
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SpringDataConstants.PROP_PREFIX + ".routing-datasource")
public class RoutingDataSourceProperties {

  private Boolean enabled;

  private String[] nodes;

  private List<DataSourceProperties> metadata;
}
