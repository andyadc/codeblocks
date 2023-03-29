package com.andyadc.codeblocks.test.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EasyExcelTests {

	/**
	 * 交集(基于java8新特性)优化解法2 适用于大数据量
	 * 求List1和List2中都有的元素
	 */
	public static List<String> intersectList2(List<String> list1, List<String> list2) {
		Map<String, String> tempMap = list2.parallelStream().collect(
			Collectors.toMap(Function.identity(), Function.identity(), (oldData, newData) -> newData)
		);
		return list1.parallelStream().filter(tempMap::containsKey).collect(Collectors.toList());
	}

	/**
	 * 差集(基于java8新特性)优化解法2 适用于大数据量
	 * 求List1中有的但是List2中没有的元素
	 */
	public static List<String> subList2(List<String> list1, List<String> list2) {
		Map<String, String> tempMap = list2.parallelStream().collect(
			Collectors.toMap(Function.identity(), Function.identity(), (oldData, newData) -> newData)
		);
		return list1.parallelStream().filter(str -> !tempMap.containsKey(str)).collect(Collectors.toList());
	}

	@Test
	public void testList() {
		List<String> list1 = Arrays.asList("1", "2", "3", "4", "5");
		List<String> list2 = Arrays.asList("1", "3", "5");

		System.out.println(subList2(list1, list2));
		System.out.println(intersectList2(list1, list2));
	}

	@Test
	public void testRead() {
		String path = "D:\\temp\\excel";
//		EasyExcel.read(path + "\\2018-05-23~.xlsx", ServiceCall.class, new DataListener()).sheet().doRead();

		List<String> total = new ArrayList<>();
		EasyExcel.read(path + "\\2018-05-23~.xlsx", ServiceCall.class, new ReadListener<ServiceCall>() {
			@Override
			public void invoke(ServiceCall serviceCall, AnalysisContext analysisContext) {
//				System.out.println(serviceCall);
				total.add(serviceCall.getService_id());
			}

			@Override
			public void doAfterAllAnalysed(AnalysisContext analysisContext) {
			}
		}).sheet().doRead();

		System.out.println(total.size());

		List<String> list_2022_01 = new ArrayList<>();
		EasyExcel.read(path + "\\2022-01-01~.xlsx", ServiceCall.class, new ReadListener<ServiceCall>() {
			@Override
			public void invoke(ServiceCall serviceCall, AnalysisContext analysisContext) {
//				System.out.println(serviceCall);
				list_2022_01.add(serviceCall.getService_id());
			}

			@Override
			public void doAfterAllAnalysed(AnalysisContext analysisContext) {
			}
		}).sheet().doRead();

		System.out.println(list_2022_01.size());

		List<String> subList = subList2(total, list_2022_01);
		System.out.println(subList);
		System.out.println(subList.size());
	}

	public static class DataListener implements ReadListener<ServiceCall> {

		@Override
		public void invoke(ServiceCall serviceCall, AnalysisContext analysisContext) {
			System.out.println(serviceCall);
		}

		@Override
		public void doAfterAllAnalysed(AnalysisContext analysisContext) {

		}
	}

	public static class ServiceCall {
		private String service_id;
		private String num;

		public String getService_id() {
			return service_id;
		}

		public void setService_id(String service_id) {
			this.service_id = service_id;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		@Override
		public String toString() {
			return new StringJoiner(", ", ServiceCall.class.getSimpleName() + "[", "]")
				.add("service_id=" + service_id)
				.add("num=" + num)
				.toString();
		}
	}
}
