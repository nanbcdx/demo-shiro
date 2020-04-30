package com.wph.demoshiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wph.demoshiro.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description :
 * @Author :wangcheng
 * @Date :2020/4/28 13:43
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    List<String> selectByRoleIds(@Param("roleIds") Integer[] roleIds);
}
