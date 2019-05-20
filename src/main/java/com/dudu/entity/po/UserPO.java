package com.dudu.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "sys_user")
public class UserPO extends Model<UserPO> {

    private static final long serialVersionUID = 7838569805393187805L;
    /**
     * 主键
     */
    @TableId(value = "user_id")
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像路径
     */
    private String photo;
    /**
     * 性别(0:女 1:男 2:不详)
     */
    private String sex;
    /**
     * 是否锁定(0:false 1:true)
     */
    @TableField("is_locked")
    private Boolean locked;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 最后登陆时间
     */
    private String lastTime;

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }
}