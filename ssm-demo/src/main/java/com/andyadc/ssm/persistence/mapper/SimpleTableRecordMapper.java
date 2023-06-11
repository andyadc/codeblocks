package com.andyadc.ssm.persistence.mapper;

import com.andyadc.ssm.persistence.entity.SimpleTableRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SimpleTableRecordMapper {

	@Insert(value = "insert into user (username) values (#{username})")
	int insert(SimpleTableRecord record);

	@Insert("INSERT INTO user(username)VALUES(#{username}) ON DUPLICATE KEY UPDATE param_value=VALUES(param_value);")
	int insertOrUpdate(SimpleTableRecord record);
}
