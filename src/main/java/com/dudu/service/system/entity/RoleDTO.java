package com.dudu.service.system.entity;

import lombok.Data;

/**
 * @author 大橙子
 */
@Data
public class RoleDTO {

    /**
     * 主键
     */
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色描述
     */
    private String description;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String modifiedTime;
    /**
     * 是否可用（0：false，1：true）
     */
    private Boolean available;
}