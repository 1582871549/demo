/**
 * FileName: MyProperties
 * Author:   大橙子
 * Date:     2019/3/7 9:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.configuration.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/3/7
 * @since 1.0.0
 */
@Data
public class MyProperties {

    private String name;
    private String sex;

    public MyProperties() {
    }

    public MyProperties(String name, String sex) {
        System.out.println("----------myProperties初始化------------");
        this.name = name;
        this.sex = sex;
    }

}