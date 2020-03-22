package com.dudu.service.db;

import java.util.List;

public interface RoleService {

    /**
     * 根据用户id获取用户对应的权限列表
     * @param username 用户名
     * @return roleList
     */
    List<String> listRoleByUsername(String username);
}
