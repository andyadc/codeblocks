package framework.test.idgen;

import com.alibaba.druid.pool.DruidDataSource;
import com.andyadc.codeblocks.framework.idgen.IDGen;
import com.andyadc.codeblocks.framework.idgen.Result;
import com.andyadc.codeblocks.framework.idgen.segment.SegmentIDGenImpl;
import com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocDao;
import com.andyadc.codeblocks.framework.idgen.segment.dao.impl.IDAllocDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * andy.an
 */
public class SegmentService {

	private static final Logger logger = LoggerFactory.getLogger(SegmentService.class);

	private static final String url = "jdbc:mysql://www.qq-server.com:3307/idea?useSSL=false&serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8";
	private static final String username = "root";
	private static final String password = "andyadC7,./!";

	DruidDataSource dataSource;
	IDGen idGen;

	public SegmentService() throws SQLException {

		// Config dataSource
		dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.init();

		// Config Dao
		IDAllocDao dao = new IDAllocDaoImpl(dataSource);
		idGen = new SegmentIDGenImpl();
		((SegmentIDGenImpl) idGen).setDao(dao);
		if (idGen.init()) {
			logger.info("Segment Service Init Successfully");
		} else {
			throw new RuntimeException("Segment Service Init Fail");
		}
	}

	public Result getId(String key) {
		return idGen.gen(key);
	}

	public SegmentIDGenImpl getIdGen() {
		if (idGen instanceof SegmentIDGenImpl) {
			return (SegmentIDGenImpl) idGen;
		}/**/
		return null;
	}
}
