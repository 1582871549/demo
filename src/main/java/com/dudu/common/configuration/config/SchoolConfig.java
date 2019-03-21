/**
 * FileName: SchoolConfig
 * Author:   大橙子
 * Date:     2019/3/7 10:58
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.configuration.config;

import com.dudu.common.configuration.bean.MyProperties;
import com.dudu.common.configuration.example.SchoolService;
import com.dudu.common.configuration.example.impl.SecondarySchoolServiceImpl;
import com.dudu.service.UserService;
import com.dudu.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/3/7
 * @since 1.0.0
 */
@Configuration
public class SchoolConfig {


    @Bean
    public SchoolService schoolService() {

        SecondarySchoolServiceImpl schoolService = new SecondarySchoolServiceImpl();

        System.out.println("SchoolConfig   配置类中注入时的  schoolService  实例地址  ：" + schoolService);

        return schoolService;
    }

    @Bean
    public UserService userService(){
        return new UserServiceImpl();
    }

    @Bean
    public MyProperties myProperties() {
        return new MyProperties("杜建伟", "男");
    }
}
