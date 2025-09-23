package com.andyadc.codeblocks.test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimestampChecksum {

	// 计算校验位的方法（可以自定义）
	public static int calculateCheckDigit(String number) {
		int sum = 0;
		for (int i = 0; i < number.length(); i++) {
			char ch = number.charAt(i);
			int digit = Character.getNumericValue(ch);
			sum += digit;
		}
		return sum % 10; // 返回 0~9 之间的值
	}

	public static String generateCheckedTimestamp(long timestamp) {
		String tsStr = String.valueOf(timestamp);

		if (tsStr.length() != 13) {
			throw new IllegalArgumentException("必须是13位的时间戳");
		}

		String prefix = tsStr.substring(0, 12); // 前12位
		int checkDigit = calculateCheckDigit(prefix); // 计算校验位
		System.out.println("calculateCheckDigit: " + checkDigit);

		// 拼接新的时间戳
		return prefix + checkDigit;
	}

	public static boolean checkRequestTimestamp(String timestamp) {
		if (timestamp == null || timestamp.length() != 13) return false;

		String prefix = timestamp.substring(0, 12); // 前12位
		int checkDigit = calculateCheckDigit(prefix); // 计算校验位

		return timestamp.equals(prefix + checkDigit);
	}

	public static void main(String[] args) {
		long timestamp = Instant.now().toEpochMilli();

		String checkedTimestamp = generateCheckedTimestamp(timestamp);

		System.out.println("原始时间戳: " + timestamp);
		System.out.println("带校验位的新时间戳: " + checkedTimestamp);

		LocalDateTime localDateTime = LocalDateTime.of(2025, 5, 9, 13, 49, 0);
		long epochMilli = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		System.out.println(epochMilli);

		System.out.println((timestamp - epochMilli) / 1000L);
		System.out.println((Long.parseLong(checkedTimestamp) - epochMilli) / 1000L);

		System.out.println(checkRequestTimestamp("1746781412461"));
	}

}
