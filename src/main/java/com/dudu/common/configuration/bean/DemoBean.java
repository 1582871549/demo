/**
 * FileName: DemoBean
 * Author:   大橙子
 * Date:     2019/3/23 17:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.configuration.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 〈一句话功能简述〉<br> 
 * <p>
 *     根据不同的环境来注入该bean
 * </p>
 *
 * @author 大橙子
 * @create 2019/3/23
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemoBean {
    private String content;
}
