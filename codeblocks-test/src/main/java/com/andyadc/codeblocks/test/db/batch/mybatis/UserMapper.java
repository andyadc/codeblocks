package com.andyadc.codeblocks.test.db.batch.mybatis;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

	void batchInsertUser(@Param("list") List<User> userList);
}
