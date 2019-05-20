/**
 * FileName: CustomModularRealmAuthenticator
 * Author:   大橙子
 * Date:     2019/4/10 15:56
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.shiro.authenticator;

import com.dudu.common.shiro.token.UsernamePasswordPhoneToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

/**
 * 2、自定义一个ModularRealmAuthenticator的子类，
 * 重写doAuthenticate方法，这个方法的功能是用来决定单realm或者多realm时应该怎么做的
 */
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        // TODO Auto-generated method stub

        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 强制转换回自定义的CustomizedToken
        UsernamePasswordPhoneToken phoneToken = (UsernamePasswordPhoneToken) authenticationToken;
        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 判断是单Realm还是多Realm
        if (realms.size() == 1)
            return doSingleRealmAuthentication(realms.iterator().next(), phoneToken);
        else
            return doMultiRealmAuthentication(realms, phoneToken);
    }
}
