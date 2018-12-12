package com.andyadc.codeblocks.showcase.sys.mapper;

import com.andyadc.codeblocks.framework.mybatis.MyBatisRepository;
import com.andyadc.codeblocks.showcase.sys.entity.SpringTransaction;
import org.apache.ibatis.annotations.Param;

@MyBatisRepository
public interface SpringTransactionMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(SpringTransaction record);

    SpringTransaction selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SpringTransaction record);

    int updateIfNumberIsZero(@Param("id") Long id, @Param("number") int number);
}