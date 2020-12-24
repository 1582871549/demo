package com.dudu.common.boot.aop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TestController {

    @UrlAction(name = "注解拦截")
    @RequestMapping(value = "/annotation", method = RequestMethod.GET)
    public String annotation() {
        return "注解拦截";
    }

    @RequestMapping(value = "/method", method = RequestMethod.GET)
    public String method() {
        return "方法拦截";
    }
}
