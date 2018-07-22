package com.andyadc.permission.mapper;

import com.andyadc.permission.entity.AuthDept;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthDeptMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(AuthDept record);

    AuthDept selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuthDept record);
}