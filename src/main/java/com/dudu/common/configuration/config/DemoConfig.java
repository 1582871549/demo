/**
 * FileName: DemoConfig
 * Author:   大橙子
 * Date:     2019/3/23 17:46
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.configuration.config;

import com.dudu.common.configuration.bean.DemoBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 〈一句话功能简述〉<br> 
 * 根据不同环境, 初始化的时候对用户注入不同的参数
 *
 * @author 大橙子
 * @create 2019/3/23
 * @since 1.0.0
 */
@Configuration
public class DemoConfig {

    @Bean
    @Profile("dev")
    public DemoBean devDemoBean(){
        System.out.println("开发环境demobean             : " + this.getClass().getName() + "."
                + Thread.currentThread() .getStackTrace()[1].getMethodName());
        return new DemoBean("from dev profile");
    }

    @Bean
    @Profile("prod")
    public DemoBean prodDemoBean(){
        System.out.println("正式环境demobean             : " + this.getClass().getName() + "."
                + Thread.currentThread() .getStackTrace()[1].getMethodName());
        return new DemoBean("from prod profile");
    }
}
