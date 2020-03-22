/**
 * FileName: LoginController
 * Author:   大橙子
 * Date:     2019/3/25 22:13
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.web.controller;

import com.dudu.common.base.BaseController;
import com.dudu.common.enums.ReturnCodeEnum;
import com.dudu.common.shiro.util.ShiroKit;
import com.dudu.entity.dto.UserDTO;
import com.dudu.entity.vo.UserVO;
import com.dudu.service.db.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 *     登陆接口
 * </p>
 *
 * @author 大橙子
 * @date 2019/3/25
 * @since 1.0.0
 */
@Controller
public class LoginController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    /**
     * 跳转登陆页
     *      已通过验证的用户跳转主页(不包含 记住我 的用户)
     *      未通过验证的用户重定向到登陆页
     *
     * @return path
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (ShiroKit.isAuthenticated()) {
            return "redirect:/";
        }
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody UserVO userVO, HttpSession session){

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userVO, userDTO);
        success(ReturnCodeEnum.BASE_SUCCESS.getCode(), "");
        userService.login(userDTO);
        return "login";
    }

    @RequestMapping(value = "/logOut", method = RequestMethod.GET)
    public String logOut(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
//        session.removeAttribute("user");
        return "login";
    }

    /**
     * 主页
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(HttpSession session, Model model) {

        // 将用户的token放入session  后期放入cookie
        if (ShiroKit.isUser() && session.getAttribute("token") == null) {
            session.setAttribute("token", "token");
        }

        return "index";
    }

    /**
     * 欢迎页面
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome(HttpServletRequest request, Model model) {
        return "welcome";
    }

    public void aaa(){
        // 获取当前用户
        Subject currentUser = SecurityUtils.getSubject();

        // 操作会话
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");
        String value = (String) session.getAttribute("someKey");
        if (value.equals("aValue")) {
            log.info("Retrieved the correct value! [" + value + "]");
        }

// 执行登录
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
            token.setRememberMe(true);
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                log.info("There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                log.info("Password for account " + token.getPrincipal() + " was incorrect!");
            } catch (LockedAccountException lae) {
                log.info("The account for username " + token.getPrincipal() + " is locked. "
                        + "Please contact your administrator to unlock it.");
            } catch (AuthenticationException ae) {
                // unexpected condition? error?
            }
        }

// 输出用户信息
        log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");

// 检查角色
        if (currentUser.hasRole("schwartz")) {
            log.info("May the Schwartz be with you!");
        } else {
            log.info("Hello, mere mortal.");
        }

// 检查权限
        if (currentUser.isPermitted("lightsaber:weild")) {
            log.info("You may use a lightsaber ring. Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }

// 结束，执行注销
        currentUser.logout();

        System.exit(0);
    }

}
