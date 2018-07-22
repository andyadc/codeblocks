package com.andyadc.permission.mapper;

import com.andyadc.permission.entity.AuthRoleAcl;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthRoleAclMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AuthRoleAcl record);

    int insertSelective(AuthRoleAcl record);

    AuthRoleAcl selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuthRoleAcl record);

    int updateByPrimaryKey(AuthRoleAcl record);
}