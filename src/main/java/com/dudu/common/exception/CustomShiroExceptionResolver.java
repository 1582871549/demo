/**
 * FileName: CustomShiroExceptionResolver
 * Author:   大橙子
 * Date:     2019/4/12 15:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *     自定义shiro异常处理类
 * </p>
 * @author 大橙子
 * @date 2019/4/12
 * @since 1.0.0
 */
public class CustomShiroExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        /* 无权操作异常, 转发至403路径 */
        if (e instanceof UnauthorizedException) {
            return new ModelAndView("error/shiro_403");
        }
        e.printStackTrace();
        ModelAndView view = new ModelAndView("error/shiro_403");
        view.addObject("exception", e.toString().replaceAll("\n", "<br/>"));
        return view;
    }
}
