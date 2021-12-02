package com.andyadc.codeblocks.test.jsqlparser;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * https://github.com/JSQLParser/JSqlParser/wiki/Examples-of-SQL-parsing
 */
public class JsqlparserTests {

	@Test
	public void testSimpleParse() throws Exception {
		Statement statement = CCJSqlParserUtil.parse("select *  from  demo");
		Select select = (Select) statement;

		TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
		List<String> tableList = tablesNamesFinder.getTableList(select);
		System.out.println(tableList);
	}
}
