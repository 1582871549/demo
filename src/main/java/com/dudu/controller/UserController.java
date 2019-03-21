package com.dudu.controller;

import com.dudu.common.configuration.bean.MyProperties;
import com.dudu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/*")
public class UserController {

    @Autowired
    private MyProperties myProperties;
    @Autowired
    private UserService userService;


    @RequestMapping("/list")
    public String count(@RequestParam String name) {

        List<String> list = userService.listUser(name);

        String str = list.toString();

        return str;
    }

    @RequestMapping(value = {"/", "/index"})
    public String index() {
        System.out.println("-------toString--------"+myProperties.toString()+"---------------");
        return "this is index page";
    }

}
