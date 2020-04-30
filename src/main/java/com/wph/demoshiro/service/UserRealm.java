package com.wph.demoshiro.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wph.demoshiro.entity.Role;
import com.wph.demoshiro.entity.User;
import com.wph.demoshiro.mapper.PermissionMapper;
import com.wph.demoshiro.mapper.RoleMapper;
import com.wph.demoshiro.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description :
 * @Author :wangcheng
 * @Date :2020/4/28 14:21
 */
@Service
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("开始授权=============");
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        User user=(User)principals.getPrimaryPrincipal();
        List<Role> roles=roleMapper.selectByUserId(user.getId());
        if(roles==null||roles.size()==0){
            throw new AuthorizationException();
        }
        List<Integer> roleIds=new ArrayList<>();
        for(Role role:roles){
            log.info(role.toString());
            roleIds.add(role.getId());
            info.addRole(role.getName());
        }
        List<String> permissions=permissionMapper.selectByRoleIds(roleIds.toArray(new Integer[]{}));
        log.info(permissions.toString());
        info.addStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("开始认证==============");
        UsernamePasswordToken up=(UsernamePasswordToken)token;
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("username",up.getUsername());
        User user = userMapper.selectOne(wrapper);
        if(user==null){
            throw new UnknownAccountException();
        }
        if(user.getValid()==0){
            throw new LockedAccountException();
        }
        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(user,user.getPassword(),
                ByteSource.Util.bytes(user.getSalt().getBytes()),getName());

        return info;
    }

    @Override
    public CredentialsMatcher getCredentialsMatcher() {
        HashedCredentialsMatcher matcher=new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("MD5");
        matcher.setHashIterations(1);
        return matcher;
    }
}
