package com.wph.demoshiro.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description :
 * @Author :wangcheng
 * @Date :2020/4/28 13:56
 */
@Controller
public class WebController {


    @GetMapping("/loginUI")
    public String loginUI() {
        return "login.html";
    }


    @GetMapping("/index")
    @RequiresRoles("经理")
    public String index() {
        return "index.html";
    }

}
