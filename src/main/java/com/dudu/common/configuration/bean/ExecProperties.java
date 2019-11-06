package com.dudu.common.configuration.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 通过各属性组装出每个项目的存储库路径
 *
 * @author 大橙子
 * @create 2019/10/31
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = ExecProperties.PREFIX)
public class ExecProperties {

    public static final String PREFIX = "coverage-resource.exec";

    private String directory;
    private String defaultName;
}
