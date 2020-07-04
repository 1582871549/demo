/**
 * FileName: ShiroConfig
 * Author:   大橙子
 * Date:     2019/3/25 22:03
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.configuration.config;

import com.dudu.common.exception.CustomShiroExceptionResolver;
import com.dudu.common.shiro.realm.UserRealm;
import com.dudu.web.filter.CustomFilter;
import com.dudu.web.filter.KaptchaFilter;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 大橙子
 * @TODO 多个realm配置  在securityManager中   以及多realm的认证策略
 * @date 2019/3/25
 * @since 1.0.0
 */
@Configuration
@Order(1)
public class ShiroConfig {

    /**
     * 配置kaptcha图片验证码框架提供的Servlet,,这是个坑,很多人忘记注册(注意)
     *
     * @return kaptchaServlet
     */
    @Bean
    public ServletRegistrationBean<HttpServlet> kaptchaServlet() {
        ServletRegistrationBean<HttpServlet> servlet = new ServletRegistrationBean<>(new KaptchaServlet(), "/kaptcha.jpg");
        servlet.addInitParameter(Constants.KAPTCHA_SESSION_CONFIG_KEY, Constants.KAPTCHA_SESSION_KEY);//session key
        servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "50");//字体大小
        servlet.addInitParameter(Constants.KAPTCHA_BORDER, "no");
        servlet.addInitParameter(Constants.KAPTCHA_BORDER_COLOR, "105,179,90");
        servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "45");
        servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "宋体,楷体,微软雅黑");
        servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        servlet.addInitParameter(Constants.KAPTCHA_IMAGE_WIDTH, "125");
        servlet.addInitParameter(Constants.KAPTCHA_IMAGE_HEIGHT, "60");
        //可以设置很多属性,具体看com.google.code.kaptcha.Constants
//		kaptcha.border  是否有边框  默认为true  我们可以自己设置yes，no
//		kaptcha.border.color   边框颜色   默认为Color.BLACK
//		kaptcha.border.thickness  边框粗细度  默认为1
//		kaptcha.producer.impl   验证码生成器  默认为DefaultKaptcha
//		kaptcha.textproducer.impl   验证码文本生成器  默认为DefaultTextCreator
//		kaptcha.textproducer.char.string   验证码文本字符内容范围  默认为abcde2345678gfynmnpwx
//		kaptcha.textproducer.char.length   验证码文本字符长度  默认为5
//		kaptcha.textproducer.font.names    验证码文本字体样式  默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
//		kaptcha.textproducer.font.size   验证码文本字符大小  默认为40
//		kaptcha.textproducer.font.color  验证码文本字符颜色  默认为Color.BLACK
//		kaptcha.textproducer.char.space  验证码文本字符间距  默认为2
//		kaptcha.noise.impl    验证码噪点生成对象  默认为DefaultNoise
//		kaptcha.noise.color   验证码噪点颜色   默认为Color.BLACK
//		kaptcha.obscurificator.impl   验证码样式引擎  默认为WaterRipple
//		kaptcha.word.impl   验证码文本字符渲染   默认为DefaultWordRenderer
//		kaptcha.background.impl   验证码背景生成器   默认为DefaultBackground
//		kaptcha.background.clear.from   验证码背景颜色渐进   默认为Color.LIGHT_GRAY
//		kaptcha.background.clear.to   验证码背景颜色渐进   默认为Color.WHITE
//		kaptcha.image.width   验证码图片宽度  默认为200
//		kaptcha.image.height  验证码图片高度  默认为50
        return servlet;
    }


    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是会报错的，
     * 因为在初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * <p>
     * Filter Chain定义说明
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     * <p>
     * hiroFilterFactoryBean，是个factorybean，为了生成ShiroFilter。
     * 它主要保持了三项数据，securityManager，filters，filterChainDefinitionManager。
     */
    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            SecurityManager securityManager, CustomFilter customFilter, KaptchaFilter kaptchaFilter) {

        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);

        /*
         * 配置自定义过滤器
         *
         * 如果自定义filter导入异常, 直接new放入即可
         */
        Map<String, Filter> filterMap = bean.getFilters();
        filterMap.put("custom", customFilter);
        filterMap.put("kaptcha", kaptchaFilter);
        bean.setFilters(filterMap);

        /*
         * 配置自定义拦截器
         *
         * authc:所有url都必须认证通过才可以访问
         * anon:所有url都都可以匿名访问
         */
        Map<String, String> map = new LinkedHashMap<>();
        map.put("/", "authc");
        map.put("/index", "user");
        map.put("/logout", "logout");
        // map.put("/**", "anon");
        map.put("/user/**", "authc");
        map.put("/static/**", "anon");
        map.put("/css/**", "anon");
        map.put("/js/**", "anon");
        map.put("/img/**", "anon");

        /*
         * 配置用户登陆页面
         * 配置成功跳转路径
         * 配置未经授权页面
         */
        bean.setLoginUrl("/login");
        bean.setSuccessUrl("/index");
        bean.setUnauthorizedUrl("/error/403_error.html");
        bean.setFilterChainDefinitionMap(map);
        return bean;
    }

    /**
     * 权限管理，这个类组合了登陆，登出，权限，session的处理，是个比较重要的类。
     *
     * @param userRealm      自定义用户域
     * @param ehCacheManager 缓存管理器
     * @return securityManager
     */
    @Bean(name = "securityManager")
    public SecurityManager securityManager(
            UserRealm userRealm, EhCacheManager ehCacheManager, RememberMeManager rememberMeManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setCacheManager(ehCacheManager);
        securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }

    /**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 用户认证类
     *
     * @return userRealm
     */
    @Bean(name = "userRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public UserRealm userRealm(HashedCredentialsMatcher hashedCredentialsMatcher) {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return userRealm;
    }

    /**
     * HashedCredentialsMatcher，这个类是为了对密码进行编码的，防止密码在数据库里明码保存，
     * 这个类也负责在登陆时form中输入的密码进行编码。
     *
     * @return hashedCredentialsMatcher
     */
    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(2);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
     * EhCacheManager，缓存管理，用户登陆成功后，把用户信息和权限信息缓存起来，
     * 然后每次用户请求时，放入用户的session中，如果不设置这个bean，每个请求都会查询一次数据库。
     *
     * @return ehCacheManager
     */
    @Bean(name = "ehCacheManager")
    @DependsOn("lifecycleBeanPostProcessor")
    public EhCacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
        return cacheManager;
    }

    /**
     * cookie管理对象;
     *
     * @return rememberMeManager
     */
    @Bean(name = "rememberMeManager")
    public CookieRememberMeManager rememberMeManager(SimpleCookie rememberMeCookie) {
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCookie(rememberMeCookie);
        return manager;
    }

    /**
     * rememberMe 是cookie的名称，对应前端的checkbox的name = rememberMe
     * 记住我cookie生效时间30天 ,单位秒
     *
     * @return rememberMeCookie
     */
    @Bean(name = "rememberMeCookie")
    public SimpleCookie rememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie();
        cookie.setName("rememberMe");
        cookie.setMaxAge(259200);
        return cookie;
    }

    /**
     * session管理
     *
     * @param scheduler session校验程序
     * @return sessionManager
     */
    @Bean(name = "defaultWebSessionManager")
    public DefaultWebSessionManager defaultWebSessionManager(ExecutorServiceSessionValidationScheduler scheduler) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(18000000);
        // url中是否显示session Id
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        // 删除失效的session
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationInterval(18000000);
        sessionManager.setSessionValidationScheduler(scheduler);
        //设置SessionIdCookie 导致认证不成功，不从新设置新的cookie,从sessionManager获取sessionIdCookie
        //sessionManager.setSessionIdCookie(simpleIdCookie());
        sessionManager.getSessionIdCookie().setName("session-dudu-id");
        sessionManager.getSessionIdCookie().setPath("/");
        sessionManager.getSessionIdCookie().setMaxAge(60 * 60 * 24 * 7);
        return sessionManager;
    }

    /**
     * session校验程序
     *
     * @return executorServiceSessionValidationScheduler
     */
    @Bean(name = "executorServiceSessionValidationScheduler")
    public ExecutorServiceSessionValidationScheduler executorServiceSessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
        scheduler.setInterval(900000);
        return scheduler;
    }

    /**
     * 开启aop自动代理 与authorizationAttributeSourceAdvisor搭配使用
     *
     * @return defaultAdvisorAutoProxyCreator
     */
    @Bean(name = "defaultAdvisorAutoProxyCreator")
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    /**
     * 开启shiro aop注解支持. 使用代理方式
     *
     * @return authorizationAttributeSourceAdvisor
     */
    @Bean(name = "authorizationAttributeSourceAdvisor")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }

    /**
     * shiro无权异常处理类
     *
     * @return customShiroExceptionResolver
     */
    @Bean(name = "customShiroExceptionResolver")
    public CustomShiroExceptionResolver customShiroExceptionResolver() {
        return new CustomShiroExceptionResolver();
    }

    /**
     * 过滤器注册链
     * false 取消Filter自动注册, 使其不会添加到FilterChain中
     *
     * @param customFilter 自定义表单验证过滤器
     * @return filterRegistrationBean
     */
    @Bean(name = "filterRegistrationBean")
    public FilterRegistrationBean<Filter> filterRegistrationBean(CustomFilter customFilter) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(customFilter);
        registration.setEnabled(false);
        return registration;
    }

    /**
     * 自定义过滤器
     *
     * @return customFilter
     */
    @Bean(name = "customFilter")
    public CustomFilter customFilter() {
        return new CustomFilter();
    }

    /**
     * 自定义验证码过滤器
     *
     * @return kaptchaFilter
     */
    @Bean(name = "kaptchaFilter")
    public KaptchaFilter kaptchaFilter() {
        return new KaptchaFilter();
    }

    /**
     * 配置shiro在thymeleaf中使用的自定义tag
     *
     * @return shiroDialect
     */
    // @Bean(name = "shiroDialect")
    // public ShiroDialect shiroDialect() {
    //     return new ShiroDialect();
    // }

}
