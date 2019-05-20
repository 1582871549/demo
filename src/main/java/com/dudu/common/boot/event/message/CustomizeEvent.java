/**
 * FileName: CustomizeEvent
 * Author:   大橙子
 * Date:     2019/3/7 18:28
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.boot.event.message;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/3/7
 * @since 1.0.0
 */

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * 自定义的消息类型
 */
public class CustomizeEvent extends ApplicationContextEvent {

    private static final long serialVersionUID = -3316264084317999669L;


    public CustomizeEvent(ApplicationContext source) {
        super(source);
    }
}
