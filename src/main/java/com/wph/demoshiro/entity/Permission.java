package com.wph.demoshiro.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("permission")
@Data
public class Permission {

  @TableId(type = IdType.AUTO)
  private Integer id;
  private String permission;




}
