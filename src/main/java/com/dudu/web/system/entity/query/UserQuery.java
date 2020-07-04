package com.dudu.web.system.entity.query;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserQuery implements Serializable {

    private static final long serialVersionUID = -6923768443982647649L;
    /**
     * 主键
     */
    private Long userId;
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
     * 是否锁定
     */
    private Boolean locked;
}