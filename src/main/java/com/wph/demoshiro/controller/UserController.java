package com.wph.demoshiro.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wph.demoshiro.entity.User;
import com.wph.demoshiro.mapper.UserMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Description :
 * @Author :wangcheng
 * @Date :2020/4/28 13:45
 */
@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    private ObjectMapper objectMapper=new ObjectMapper();


    @PostMapping("/login")
    public String login(User user){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken up=new UsernamePasswordToken(user.getUsername(),user.getPassword());
        subject.login(up);
        return "登录成功";
    }

    @GetMapping("selectUser")
    public String selectUser(Integer id) throws JsonProcessingException {
        User user=userMapper.selectById(id);
        if(user!=null){
            return objectMapper.writeValueAsString(user);
        }
        return "没有该记录";
    }

    @PostMapping("updateUser")
    @RequiresPermissions("user:update")
    public String updateUser(User user){
      try {
          userMapper.updateById(user);
          return  "更新成功";
      }catch (Exception e){
          return "更新失败";
      }
    }

    @PostMapping("addUser")
    @RequiresPermissions("user:add")
    public String addUser(User user){
        String salt= UUID.randomUUID().toString();
        SimpleHash sh=new SimpleHash("MD5",user.getPassword(),salt,1);
        user.setPassword(sh.toHex());
        user.setSalt(salt);
        user.setValid(1);
        int result = userMapper.insert(user);
        if(result!=1) {
            return "添加失败";
        }
        return "添加成功";
    }


    @GetMapping("deleteUser")
    @RequiresPermissions("user:delete")
    public String deleteUser(Integer id){
        int result = userMapper.deleteById(id);
        if(result!=1) {
            return "删除失败";
        }
        return "删除成功";
    }

}
