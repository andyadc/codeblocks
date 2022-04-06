package com.andyadc.codeblocks.kit;

import java.util.Calendar;
import java.util.GregorianCalendar;

// TODO
public final class IDCardUtil {

	public static float getAgeDetail(String certId) {
		String date = "";
		if (certId.length() == 18) {
			date = certId.substring(6, 10) + "-" + certId.substring(10, 12) + "-" + certId.substring(12, 14);
		}
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
