package com.dudu.manager.system.service.impl;

import com.dudu.manager.system.repository.entity.RoleDO;
import com.dudu.manager.system.repository.mapper.RoleMapper;
import com.dudu.manager.system.service.RoleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dujianwei
 * @create 2020/6/2
 * @since 1.0.0
 */
@Service
public class RoleManagerImpl implements RoleManager {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public RoleDO getRole(Long roleId) {
        return roleMapper.getRole(roleId);
    }

    @Override
    public List<RoleDO> listRole() {
        return roleMapper.listRole();
    }

    @Override
    public boolean insertRole(RoleDO roleDO) {


        // 如果新增失败则捕获异常
        try {
            roleMapper.insertRole(roleDO);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("SQLIntegrityConstraintViolationException 插入错误");
        }

        return true;
    }


}
