package com.dudu.common.configuration.bean;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>数据库数据源配置</p>
 * <p>说明:这个类中包含了许多默认配置,若这些配置符合您的情况,您可以不用管,若不符合,建议不要修改本类,建议直接在"application.yml"中配置即可</p>
 *
 * @author dujianwei
 */
@Data
@Component
@ConfigurationProperties(prefix = HikariConfigBean.PREFIX)
public class HikariConfigBean {

    public static final String PREFIX = "spring.datasource";

    private String url;
    private String username;
    private String password;
    private String driverClassName = "com.mysql.cj.jdbc.Driver";
    private Integer connectionTimeout = 30000;
    private Integer idleTimeout = 600000;
    private Integer maxLifetime = 1800000;
    private String connectionTestQuery = "select 1";
    private Integer minimumIdle = 10;
    private Integer maximumPoolSize = 20;
    private String poolName = "dudu-hikari-pool";

    public void config(HikariConfig config) {
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setConnectionTestQuery(connectionTestQuery);
        config.setMinimumIdle(minimumIdle);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setPoolName(poolName);
    }
}
