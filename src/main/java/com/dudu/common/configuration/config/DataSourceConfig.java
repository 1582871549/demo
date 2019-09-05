/**
 * FileName: PageHelperConfig
 * Author:   大橙子
 * Date:     2019/4/2 16:52
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.configuration.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.dudu.common.configuration.bean.HikariConfigBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author 大橙子
 * @date  2019/4/2
 * @since 1.0.0
 */
@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = {"com.dudu.dao"})
public class DataSourceConfig {

    @Resource
    private HikariConfigBean hikariConfigBean;

    /**
     * 分页插件
     * @return paginationInterceptor
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 配置数据源
     * @return dataSource
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/music?characterEncoding=utf8&useSSL=false&autoReconnect=true&serverTimezone=UTC");
        config.setUsername("dudu");
        config.setPassword("dudu");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setConnectionTestQuery("select 1");
        config.setMinimumIdle(10);
        config.setMaximumPoolSize(25);
        config.setPoolName("dudu-hikari-pool");
        return new HikariDataSource(config);
    }

    /**
     * 配置事务管理器
     * @return dataSourceTransactionManager
     */
    @Bean
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
