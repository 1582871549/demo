package com.dudu.service.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author 大橙子
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResourceDTO extends BaseDTO {

    private static final long serialVersionUID = 7225989106023399374L;
    private String resourceId;
    private String resourceName;
    private String comment;
    private String type;
    private String url;
    private String operation;
    private String parentId;
    private Integer sort;
    private String icon;
}