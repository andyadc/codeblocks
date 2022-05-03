package com.andyadc.bms.modules.common;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.LinkedHashMap;
import java.util.List;

@Mapper
public interface CommonMapper {

	@Select("${sql}")
	List<LinkedHashMap<String, Object>> select(@Param("sql") String sql);

	@Update("${sql}")
	Long update(@Param("sql") String sql);
}
