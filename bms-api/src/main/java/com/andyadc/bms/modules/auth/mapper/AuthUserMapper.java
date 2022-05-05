package com.andyadc.bms.modules.auth.mapper;

import com.andyadc.bms.modules.auth.entity.AuthUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthUserMapper {

	String table_name = "auth_user";

	@Results(value = {
		@Result(property = "username", column = "username")
	})
	@Select(value = "select * from " + table_name + " where username = #{username} and deleted = 0 limit 1")
	AuthUser findByUsername(String username);

	@Results(value = {
		@Result(property = "phoneNo", column = "phone_no")
	})
	@Select(value = "select * from " + table_name + " where phone_no = #{phoneNo} and deleted = 0 limit 1")
	AuthUser findByPhoneNo(String phoneNo);
}
