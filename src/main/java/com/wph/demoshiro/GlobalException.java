package com.wph.demoshiro;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description :
 * @Author :wangcheng
 * @Date :2020/4/28 17:49
 */
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(ShiroException.class)
    public String doShiroException(ShiroException e){
        String result=null;
        if(e instanceof UnknownAccountException){
            result="账号不存在";
        }else if(e instanceof LockedAccountException){
            result="账号被冻结";
        }else if(e instanceof IncorrectCredentialsException){
            result="密码错误";
        }else if(e instanceof UnauthorizedException){
            result="没有操作权限";
        }else {
            result="系统维护中...";
        }
        return result;

    }
}
