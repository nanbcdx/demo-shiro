package com.wph.demoshiro.config;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @Description :
 * @Author :wangcheng
 * @Date :2020/4/28 15:15
 */
@Configuration
public class ShiroConfig {

    @Bean
//    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager =
                new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
        return sessionManager;
    }

    @Bean
    public RememberMeManager rememberMeManager() {
        CookieRememberMeManager cManager = new CookieRememberMeManager();
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cManager.setCookie(cookie);
        return cManager;
    }

    @Bean//由此注解描述的方法会交给spring框架管理，默认bean的名字为方法名
    public SecurityManager securityManager(Realm realm,
                                           CacheManager cacheManager, RememberMeManager rememberMeManager,
                                           SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        securityManager.setCacheManager(cacheManager);
        securityManager.setRememberMeManager(rememberMeManager);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * 请求过滤规则和跳转规则
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactory(SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl("/loginUI");
//        bean.setSuccessUrl("/index");
        HashMap<String, String> map = new HashMap<>();
        //anon表示放行,authc表示需要认证
        //放行静态文件,static目录下的文件  anon
        //map.put("/example/**","anno");
        //登出请求logout由shiro管理
        map.put("/logout", "logout");
//        map.put("/user/addUser", "anon");
        map.put("/user/login", "anon");
        //其他请求全部拦截需要认证通过后放行
        //map.put("/**","authc");
        //rememberMe功能需要user认证
        map.put("/**", "user");
        bean.setFilterChainDefinitionMap(map);
        return bean;
    }

    @Bean
    public CacheManager shiroCacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    //==================Shiro框架授权配置=================
    //Shiro框架中的授权是基于spring中AOP规范做了一个具体实现
    //AuthorizationAttributeSourceAdvisor中定义了切入点和通知
    //这里的切入点需要实用@RequiresPermissions注解进行描述
    //代理对象检测到方法上有@RequiresPermissions注解就会调用通知对象进行功能增强。
    @Bean
    public AuthorizationAttributeSourceAdvisor
    authorizationAttributeSourceAdvisor(
            SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor =
                new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}