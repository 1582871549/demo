package com.dudu.entity.dto;

import com.dudu.entity.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author 大橙子
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends BaseDTO {

    private static final long serialVersionUID = -8532970873838122778L;
    private String roleId;
    private String roleName;
    private String comment;
}