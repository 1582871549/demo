package com.dudu.entity.dto;

import com.dudu.entity.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseDTO {

    private static final long serialVersionUID = 3315228988741343381L;
    private Long userId;
    private String username;
    private String password;
    private String salt;
    private String name;
    private String phone;
    private String photo;
    private String sex;
    private Date lastTime;
}