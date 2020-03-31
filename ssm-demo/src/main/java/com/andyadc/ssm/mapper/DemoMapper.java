package com.andyadc.ssm.mapper;

import com.andyadc.ssm.entity.Demo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DemoMapper {
	int deleteByPrimaryKey(Long id);

	int insertSelective(Demo record);

	Demo selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Demo record);

	List<Demo> selectByTypeList(List<Long> types);

	List<Demo> selectByTypeArray(Long[] types);

	int batchInsert(List<Demo> demos);

	List<Demo> selectByNameLike(String name);

	int updateByMap(Map<String, Object> map);

	List<Demo> selectByChoose(Demo demo);

	List<Demo> selectByWhere(Demo demo);
}
