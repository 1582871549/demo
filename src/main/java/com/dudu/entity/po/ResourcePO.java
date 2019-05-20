package com.dudu.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 大橙子
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "sys_resource")
public class ResourcePO extends Model<ResourcePO> {

    private static final long serialVersionUID = -3734186975559806202L;
    /**
     * 主键
     */
    @TableId(value = "resource_id")
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
    @TableField("is_locked")
    private Boolean locked;

    @Override
    protected Serializable pkVal() {
        return this.resourceId;
    }
}