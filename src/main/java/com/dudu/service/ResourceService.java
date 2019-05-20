package com.dudu.service;

import java.util.List;

public interface ResourceService {

    /**
     * 根据用户id获取用户对应的权限
     * @param username 用户名
     * @return resourceList
     */
    List<String> listResourceByUsername(String username);
}
