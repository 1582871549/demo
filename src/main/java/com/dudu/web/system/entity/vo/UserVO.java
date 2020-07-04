package com.dudu.web.system.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserVO implements Serializable {

    private static final long serialVersionUID = -6923768443982647649L;
    /**
     * 主键
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像路径
     */
    private String photo;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String modifiedTime;
    /**
     * 最后登陆时间
     */
    private String lastTime;
    /**
     * 是否锁定
     */
    private Boolean locked;
}