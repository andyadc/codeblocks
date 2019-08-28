package com.andyadc.codeblocks.framework.idgen.segment.dao;

import com.andyadc.codeblocks.framework.idgen.segment.model.IdAlloc;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface IDAllocMapper {

	@Select("SELECT biz_tag, max_id, step, update_time FROM id_alloc")
	@Results(value = {
		@Result(column = "biz_tag", property = "key"),
		@Result(column = "max_id", property = "maxId"),
		@Result(column = "step", property = "step"),
		@Result(column = "update_time", property = "updateTime")
	})
	List<IdAlloc> getAllIdAllocs();

	@Select("SELECT biz_tag, max_id, step FROM id_alloc WHERE biz_tag = #{tag}")
	@Results(value = {
		@Result(column = "biz_tag", property = "key"),
		@Result(column = "max_id", property = "maxId"),
		@Result(column = "step", property = "step")
	})
	IdAlloc getIdAlloc(@Param("tag") String tag);

	@Update("UPDATE id_alloc SET max_id = max_id + step WHERE biz_tag = #{tag}")
	void updateMaxId(@Param("tag") String tag);

	@Update("UPDATE id_alloc SET max_id = max_id + #{step} WHERE biz_tag = #{key}")
	void updateMaxIdByCustomStep(@Param("idAlloc") IdAlloc idAlloc);

	@Select("SELECT biz_tag FROM id_alloc")
	List<String> getAllTags();
}
