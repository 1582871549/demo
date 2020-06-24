package com.dudu.common.other.json;

import lombok.Data;

/**
 * @author dujianwei
 * @create 2020/6/1
 * @since 1.0.0
 */
@Data
public class UserJsonBO {

    private String name;
    private String username;
    private String password;
    // 固定修改数
    private String fixedUpdate;
}
