package com.dudu.common.configuration;

import com.dudu.common.configuration.bean.ExecProperties;
import com.dudu.common.configuration.bean.GitProperties;
import com.dudu.common.configuration.bean.MavenProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/3/15
 * @since 1.0.0
 */

@Configuration
@EnableConfigurationProperties({GitProperties.class, MavenProperties.class, ExecProperties.class})
public class CustomAutoConfiguration {


}
