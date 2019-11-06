/**
 * FileName: CustomAutoConfiguration
 * Author:   大橙子
 * Date:     2019/3/15 16:46
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.configuration;

import com.dudu.common.configuration.bean.ExecProperties;
import com.dudu.common.configuration.bean.GitProperties;
import com.dudu.common.configuration.bean.MavenProperties;
import com.dudu.common.configuration.config.DataSourceConfig;
import com.dudu.common.configuration.config.ShiroConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/3/15
 * @since 1.0.0
 */

@Configuration
@Import(value = {DataSourceConfig.class, ShiroConfig.class})
@EnableConfigurationProperties({GitProperties.class, MavenProperties.class, ExecProperties.class})
public class CustomAutoConfiguration {


}
