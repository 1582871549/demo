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

import com.dudu.common.configuration.config.SchoolConfig;
import com.dudu.common.configuration.example.SchoolService;
import com.dudu.common.configuration.example.impl.PrimarySchoolServiceImpl;
import org.springframework.context.annotation.Bean;
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
@Import(SchoolConfig.class)
public class CustomAutoConfiguration {

    @Bean
    public PrimarySchoolServiceImpl primarySchoolServiceImpl(SchoolService schoolService) {

        /**
         * 这里会注入SchoolService类型的bean
         * 这里注入的这个bean是SchoolConfig.class中的SchoolService类型的bean
         */
        PrimarySchoolServiceImpl primarySchoolService = new PrimarySchoolServiceImpl(schoolService);

        primarySchoolService.paly();

        return primarySchoolService;
    }
}
