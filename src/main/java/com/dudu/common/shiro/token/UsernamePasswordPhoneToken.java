/**
 * FileName: UsernamePasswordPhoneToken
 * Author:   大橙子
 * Date:     2019/4/10 15:40
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.shiro.token;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 〈一句话功能简述〉<br> 
 * shiro框架提供了一个UsernamePasswordToken令牌，
 * 用来验证用户名和密码类的登录。那如果想要通过替他方式登录认证，
 * 例如通过手机验证码接口，就需要通过自定义token、自定义realm等来实现。
 *
 * 1、首先   自定义一个token继承UsernamePasswordToken，
 *          为什么要继承这个类而不是AuthenticationToken？，
 *          是因为这样做保证了用户名密码认证方式任然能正常使用
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UsernamePasswordPhoneToken extends UsernamePasswordToken {

    private static final long serialVersionUID = -7724077746088962317L;

    // 手机号码
    private String phone;

    /**
     * 重写getPrincipal方法
     */
    @Override
    public Object getPrincipal() {
        // TODO Auto-generated method stub
        // 如果获取到用户名，则返回用户名，否则返回电话号码
        if (phone == null) {
            return getUsername();
        } else {
            return getPhone();
        }
    }

    /**
     * 重写getCredentials方法
     */
    @Override
    public Object getCredentials() {
        // TODO Auto-generated method stub
        // 如果获取到密码，则返回密码，否则返回null
        if (phone == null) {
            return getPassword();
        } else {
            return "ok";
        }
    }

    public UsernamePasswordPhoneToken() {
    }

    public UsernamePasswordPhoneToken(final String phone) {
        this.phone = phone;
    }

    public UsernamePasswordPhoneToken(final String username, final String password) {
        super(username, password);
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
