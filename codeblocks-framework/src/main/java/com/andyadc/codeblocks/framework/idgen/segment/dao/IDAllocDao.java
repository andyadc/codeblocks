package com.andyadc.codeblocks.framework.idgen.segment.dao;

import com.andyadc.codeblocks.framework.idgen.segment.model.IdAlloc;

import java.util.List;

public interface IDAllocDao {
	List<IdAlloc> getAllIdAllocs();

	IdAlloc updateMaxIdAndGetLeafAlloc(String tag);

	IdAlloc updateMaxIdByCustomStepAndGetIdAlloc(IdAlloc leafAlloc);

	List<String> getAllTags();
}
