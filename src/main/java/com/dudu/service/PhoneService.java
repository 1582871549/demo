/**
 * FileName: PhoneService
 * Author:   大橙子
 * Date:     2019/4/10 16:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.service;

import com.dudu.entity.dto.UserDTO;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/4/10
 * @since 1.0.0
 */
public interface PhoneService {

    /**
     * 通过手机号获取用户
     * @param phone 手机号
     * @return 实体对象
     */
    UserDTO getUserByPhone(String phone);
}
