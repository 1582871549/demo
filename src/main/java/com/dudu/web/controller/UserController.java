package com.dudu.web.controller;

import com.dudu.common.base.BaseController;
import com.dudu.common.shiro.token.UsernamePasswordPhoneToken;
import com.dudu.dao.RoleMapper;
import com.dudu.entity.bean.RolePO;
import com.dudu.service.db.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 大橙子
 */

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

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

        List<RolePO> roleList = roleMapper.selectList(null);

        StringBuffer sb = new StringBuffer();

        System.out.println("-----------------------------------");
        for (RolePO rolePO : roleList) {
            System.out.println(rolePO);
            sb.append(rolePO);
            sb.append("----");
        }
        System.out.println("-----------------------------------");

        return sb.toString();
    }
}