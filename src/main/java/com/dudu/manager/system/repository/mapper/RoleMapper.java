package com.dudu.manager.system.repository.mapper;

import com.dudu.manager.system.repository.entity.RoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    RoleDO getRole(@Param("roleId") Long roleId);

    List<RoleDO> listRole();

    int insertRole(RoleDO roleDO);
}