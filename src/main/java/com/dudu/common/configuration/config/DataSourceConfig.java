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

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 配置数据源
 *
 * @author 大橙子
 * @date  2019/4/2
 * @since 1.0.0
 */
@Configuration
@MapperScan(basePackages = {"com.dudu.manager.system.repository.mapper"})
@EnableTransactionManagement
public class DataSourceConfig {

    @Autowired
    private DruidProperties druidProperties;

    @Bean
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        druidProperties.config(dataSource);
        return dataSource;
    }

    /**
     * 分页插件
     */
    // @Bean
    // public PaginationInterceptor paginationInterceptor() {
    //     return new PaginationInterceptor();
    // }

    /**
     * 配置事务管理器
     */
    @Bean
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
