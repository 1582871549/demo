package com.dudu.manager.system.service;

import com.dudu.manager.system.repository.entity.RoleDO;

import java.util.List;

public interface RoleManager {

    RoleDO getRole(Long roleId);

    List<RoleDO> listRole();

    boolean insertRole(RoleDO roleDO);
}
