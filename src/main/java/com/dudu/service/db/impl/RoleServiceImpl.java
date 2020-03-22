/**
 * FileName: RoleServiceImpl
 * Author:   大橙子
 * Date:     2019/4/3 9:42
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.service.db.impl;

import com.dudu.common.boot.annotation.UrlAction;
import com.dudu.service.db.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/4/3
 * @since 1.0.0
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class RoleServiceImpl implements RoleService {

    public String getRoleNameByRid(String rid) {
        return "您查询的角色rid为" + rid;
    }

    @UrlAction(name = "注解拦截")
    public String addUrl1() {
        return "注解拦截";
    }

    public String addUrl2() {
        return "方法拦截";
    }

    @Override
    public List<String> listRoleByUsername(String username) {

        return null;
    }
}