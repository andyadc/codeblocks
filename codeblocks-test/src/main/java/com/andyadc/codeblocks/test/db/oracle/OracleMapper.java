package com.andyadc.codeblocks.test.db.oracle;

import java.util.List;
import java.util.Map;

public interface OracleMapper {

	List<Map<String, Object>> selectTmp();

	List<BalanceOutput> callBalanceList(Map<String, Object> params);

}
