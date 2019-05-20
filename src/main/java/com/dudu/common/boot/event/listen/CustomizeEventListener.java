/**
 * FileName: CustomizeEventListener
 * Author:   大橙子
 * Date:     2019/3/7 18:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.boot.event.listen;

import com.dudu.common.boot.event.message.CustomizeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
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
 * 自定义的系统广播监听器，只接受CustomizeEvent类型的消息
 */
@Service
public class CustomizeEventListener implements ApplicationListener<CustomizeEvent> {

    @Async
    @Override
    public void onApplicationEvent(CustomizeEvent event) {

        System.out.println("自定义事件监听                : " + this.getClass().getName() + "."
                + Thread.currentThread() .getStackTrace()[1].getMethodName());
        // ApplicationContext context = (ApplicationContext) event.getSource();
        //
        // System.out.println(" === " + context.getApplicationName());
        // System.out.println(" === " + context.getDisplayName());
        // System.out.println(" === " + context.getId());
        // System.out.println(" === " + context.getParent());
        // System.out.println(" === " + context.getStartupDate());
    }
}
