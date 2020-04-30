package com.wph.demoshiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wph.demoshiro.entity.Role;

import java.util.List;

/**
 * @Description :
 * @Author :wangcheng
 * @Date :2020/4/28 13:43
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> selectByUserId(Integer userId);
}
