package org.ylzl.eden.spring.boot.data.jdbc;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.spring.boot.data.core.DataConstants;

import java.util.List;

/**
 * 动态数据源配置属性
 *
 * @author gyl
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = DataConstants.PROP_PREFIX + ".routing-datasource")
public class RoutingDataSourceProperties {

  private Boolean enabled;

  private String[] nodes;

  private List<DataSourceProperties> metadata;
}
