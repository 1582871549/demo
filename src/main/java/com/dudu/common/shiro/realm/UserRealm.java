/**
 * FileName: UserRealm
 * Author:   大橙子
 * Date:     2019/3/25 22:18
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.shiro.realm;

import com.dudu.service.system.entity.UserDTO;
import com.dudu.service.system.ResourceService;
import com.dudu.service.system.RoleService;
import com.dudu.service.system.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br> 
 * @author 大橙子
 * @create 2019/3/25
 * @since 1.0.0
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ResourceService resourceService;

    /**
     * 授权
     * @param principalCollection 1
     * @return 1
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        String username = String.valueOf(SecurityUtils.getSubject().getPrincipal());

        // 查询用户的角色信息
        Set<String> roles = getRolesByUsername(username);
        // 查询角色的权限信息
        Set<String> permissions = getPermissionsByUserName(username);

        if (CollectionUtils.isEmpty(permissions)) {
            return authorizationInfo;
        }
        // 设置用户的角色和权限
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

    // 根据用户名字从数据库中获取当前用户的权限数据
    private Set<String> getPermissionsByUserName(String username) {
        // List<String> resourceList = resourceService.listResourceByUsername(username);
        List<String> resourceList = null;
        if( resourceList != null ){
            return new HashSet<>(resourceList);
        }else{
            return null;
        }
    }

    // 根据用户名字从数据库中获取当前用户的角色数据
    private Set<String> getRolesByUsername(String username) {
        List<String> roleList = roleService.listRoleByUsername(username);
        if( roleList != null ){
            return new HashSet<>(roleList);
        }else{
            return null;
        }
    }

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();
        if (StringUtils.isBlank(username)) {
            log.debug("current token's username is null.");
            throw new AuthenticationException();
        } else {
            log.debug("current token's : {}", username);
        }

        System.out.println("当前登陆密码" + token.getCredentials());

        // 通过username从数据库中查找 User对象，如果找到，没找到.
        // 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        UserDTO userDTO = userService.getUserByUsername(username);

        if(userDTO == null){
            throw new UnknownAccountException();
        }
        if (userDTO.getLocked()) {
            throw new LockedAccountException();
        }

        SecurityUtils.getSubject().getSession(true).setAttribute("user", userDTO);

        return new SimpleAuthenticationInfo(username, userDTO.getPassword(), ByteSource.Util.bytes(username), getName());
    }
}
