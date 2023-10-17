package com.andyadc.codeblocks.kit;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

// TODO
public final class IDCardUtil {

	private static final Map<Integer, String> lastNoMapping = new HashMap<>(16, 1);
	private static final Map<Integer, Integer> factorMapping = new HashMap<>(32, 1);

	static {
		lastNoMapping.put(0, "1");
		lastNoMapping.put(1, "0");
		lastNoMapping.put(2, "X");
		lastNoMapping.put(3, "9");
		lastNoMapping.put(4, "8");
		lastNoMapping.put(5, "7");
		lastNoMapping.put(6, "6");
		lastNoMapping.put(7, "5");
		lastNoMapping.put(8, "4");
		lastNoMapping.put(9, "3");
		lastNoMapping.put(10, "2");

		factorMapping.put(1, 7);
		factorMapping.put(2, 9);
		factorMapping.put(3, 10);
		factorMapping.put(4, 5);
		factorMapping.put(5, 8);
		factorMapping.put(6, 4);
		factorMapping.put(7, 2);
		factorMapping.put(8, 1);
		factorMapping.put(9, 6);
		factorMapping.put(10, 3);
		factorMapping.put(11, 7);
		factorMapping.put(12, 9);
		factorMapping.put(13, 10);
		factorMapping.put(14, 5);
		factorMapping.put(15, 8);
		factorMapping.put(16, 4);
		factorMapping.put(17, 2);
	}

	public static void main(String[] args) {
		System.out.println(isLegal("110101198006010255"));
	}

	public static boolean isLegal(String idCardNo) {
		if (idCardNo == null || idCardNo.length() != 18) {
			return false;
		}
		String lastNo = null;
		int sum = 0;
		for (int i = 0; i < 18; i++) {
			char c = idCardNo.charAt(i);
			int no = c - '0';
			if (i == 17) {
				lastNo = String.valueOf(c);
			} else {
				Integer factor = factorMapping.get(i + 1);
				sum += (no * factor);
			}
		}
		int divide = sum % 11;
		String result = lastNoMapping.get(divide);
		return lastNo != null && lastNo.equalsIgnoreCase(result);
	}

	public static float getAgeDetail(String idCardNo) {
		if (idCardNo == null || idCardNo.length() != 18) {
			return -1F;
		}
		String date = idCardNo.substring(6, 10) + "-" + idCardNo.substring(10, 12) + "-" + idCardNo.substring(12, 14);
		// 如果有空格
		int index = date.indexOf(" ");
		if (index != -1) {
			date = date.substring(0, index);
		}

		String[] data = date.split("-");
		Calendar birthday = new GregorianCalendar(
			Integer.parseInt(data[0]),
			Integer.parseInt(data[1]),
			Integer.parseInt(data[2])
		);
		Calendar now = Calendar.getInstance();
		int day = now.get(Calendar.DAY_OF_MONTH) - birthday.get(Calendar.DAY_OF_MONTH);
		// 月份从0开始计算，所以需要+1
		int month = now.get(Calendar.MONTH) + 1 - birthday.get(Calendar.MONTH);
		int year = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
		// 按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。
		if (day < 0) {
			month -= 1;
			now.add(Calendar.MONTH, -1);// 得到上一个月，用来得到上个月的天数。
			day = day + now.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		if (month < 0) {
			month = (month + 12) % 12;
			year--;
		}

		StringBuilder tag = new StringBuilder();
		if (year > 0) {
			tag.append(year).append("岁");
		}
		if (month > 0) {
			tag.append(month).append("个月");
		}
		if (day > 0) {
			tag.append(day).append("天");
		}
		if (year == 0 && month == 0 && day == 0) {
			tag.append("今日出生");
		}
		float age = year + month / 12F + day / 365F;
//		String.valueOf(tag);
		return age;
	}
}
