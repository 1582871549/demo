/**
 * FileName: PhoneRealm
 * Author:   大橙子
 * Date:     2019/4/10 15:59
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.shiro.realm;

import com.dudu.entity.dto.UserDTO;
import com.dudu.service.PhoneService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 3、自定义realm，继承于AuthorizingRealm类，
 * 重写doGetAuthorizationInfo和doGetAuthenticationInfo
 * 这两个方法，前者是用来做授权处理的，后者用来身份认证
 */
public class PhoneRealm extends AuthorizingRealm {

    @Autowired
    private PhoneService phoneService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String phone = (String) authenticationToken.getPrincipal();
        UserDTO userDTO = phoneService.getUserByPhone(phone);

        if (userDTO != null) {
            return new SimpleAuthenticationInfo(userDTO.getPhone(), userDTO.getPassword(), "xx");

            /**
             * 这里要注意new SimpleAuthenticationInfo(userDTO.getPhone(), "ok", "xx");
             * 这个语句中SimpleAuthenticationInfo的第二个参数设为和自定义token中返回的一样。
             */
        } else {
            return null;
        }
    }
}
