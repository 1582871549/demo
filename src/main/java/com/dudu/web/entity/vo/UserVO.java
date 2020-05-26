package com.dudu.web.entity.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
// @ToString(callSuper = true)
public class UserVO extends BaseVO {

    private static final long serialVersionUID = -6923768443982647649L;
    /**
     * 主键
     */
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
     * 最后登陆时间
     */
    private String lastTime;
}