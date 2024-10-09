package com.andyadc.codeblocks.test.guava;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

/**
 * - HashBasedTable：基于哈希表来存储数据。HashBasedTable提供了快速的插入、查找和删除操作，并且不保证键顺序。
 * - TreeBasedTable：实现基于红黑树，根据键的自然顺序或者提供的比较器对行键和列键进行排序。TreeBasedTable在按键顺序遍历数据时非常高效，但插入和查找操作可能比哈希表慢。
 * - ImmutableTable：不可变的Table实现，在创建时接收所有数据，之后不允许修改。ImmutableTable对于需要共享或发布不可变数据集的情况非常有用，同时它提供了高效的内存使用。
 */
public class TableTests {

	@Test
	public void test001() {
		Table<String, String, String> table = HashBasedTable.create();

		table.put("/account/transferAccounts", "F912_11", "payerUid");
		table.put("/account/merchantWithdraw", "F912_12", "merchantUId");

		Set<String> columnKeySet = table.columnKeySet();
		System.out.println(columnKeySet);

		Set<String> rowKeySet = table.rowKeySet();
		System.out.println(rowKeySet);

		Map<String, Map<String, String>> columnMap = table.columnMap();
		System.out.println(columnMap);

		Map<String, String> row = table.row("/account/transferAccounts");
		System.out.println(row);

	}

}
