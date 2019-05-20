/**
 * FileName: ApplicationEvent
 * Author:   大橙子
 * Date:     2019/3/7 18:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.boot.event.listen;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/3/7
 * @since 1.0.0
 */

/**
 * 自定义的系统广播监听器，接收所有类型的消息
 */
@Service
public class AllEventListener implements ApplicationListener<ApplicationEvent> {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

    }
}
