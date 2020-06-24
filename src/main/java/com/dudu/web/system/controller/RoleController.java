package com.dudu.web.system.controller;

import com.dudu.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/3/23
 * @since 1.0.0
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/getName")
    public String getRoleNameByRid(@RequestParam String rid){
        return "";
    }

    @RequestMapping("/a-url")
    public String url1(){
        return null;
    };

    @RequestMapping("/b-url")
    public String url2(){
        return null;
    };

}
