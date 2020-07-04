/**
 * FileName: CustomizePublisher
 * Author:   大橙子
 * Date:     2019/3/7 18:31
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.boot.event.publish;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/3/7
 * @since 1.0.0
 */

import com.dudu.common.boot.event.message.CustomizeEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * 自定义的广播发送器
 */
@Service
public class CustomizePublisher implements ApplicationEventPublisherAware, ApplicationContextAware {

    private ApplicationEventPublisher applicationEventPublisher;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;

    }

    /**
     * 发送一条广播
     */
    public void publishEvent() {
        System.out.println("自定义事件发布                : " + this.getClass().getName() + "."
                + Thread.currentThread().getStackTrace()[1].getMethodName());
        this.applicationEventPublisher.publishEvent(new CustomizeEvent(applicationContext));
    }
}
