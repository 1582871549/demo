package com.dudu.manager.system.repository.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 大橙子
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ResourcePO {

    private static final long serialVersionUID = -3734186975559806202L;
    /**
     * 主键
     */
    private String resourceId;
    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 描述
     */
    private String comment;
    /**
     * 类型
     */
    private String type;
    /**
     * url
     */
    private String url;
    /**
     * 操作
     */
    private String operation;
    /**
     * 父元素id
     */
    private String parentId;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 头像
     */
    private String icon;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 是否锁定
     */
    private Boolean locked;
}