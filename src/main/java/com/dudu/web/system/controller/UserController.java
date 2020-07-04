package com.dudu.web.system.controller;

import com.dudu.common.shiro.token.UsernamePasswordPhoneToken;
import com.dudu.manager.system.repository.entity.RoleDO;
import com.dudu.manager.system.repository.mapper.RoleMapper;
import com.dudu.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 大橙子
 */

@RestController
@RequestMapping("/user")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 短信验证码登录
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "codeLogin", method = RequestMethod.GET)
    @ResponseBody
    public int codeLogin(String phone) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordPhoneToken token = new UsernamePasswordPhoneToken(phone);
        try {
            subject.login(token);
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

    @RequestMapping("/list")
    public String count(@RequestParam String name) {

        List<RoleDO> roleList = roleMapper.listRole();

        StringBuffer sb = new StringBuffer();

        System.out.println("-----------------------------------");
        for (RoleDO roleDO : roleList) {
            System.out.println(roleDO);
            sb.append(roleDO);
            sb.append("----");
        }
        System.out.println("-----------------------------------");

        return sb.toString();
    }
}