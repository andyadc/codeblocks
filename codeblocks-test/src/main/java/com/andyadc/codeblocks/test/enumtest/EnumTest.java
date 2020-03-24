package com.andyadc.codeblocks.test.enumtest;

import java.util.EnumMap;
import java.util.EnumSet;

/**
 * andy.an
 * 2020/3/24
 */
public class EnumTest {

	public static void main(String[] args) {

		for (UserRole role : UserRole.values()) {
			System.out.println(role);
		}

		UserRole role1 = UserRole.ROLE_ROOT_ADMIN;
		UserRole role2 = UserRole.ROLE_ORDER_ADMIN;
		UserRole role3 = UserRole.ROLE_NORMAL;

		System.out.println(role3.ordinal());
		System.out.println(role1.ordinal());
		System.out.println(role2.ordinal());

		System.out.println(role1.compareTo(role2));
		System.out.println(role2.compareTo(role3));
		System.out.println(role3.compareTo(role1));

		System.out.println(role1.name());
		System.out.println(role2.name());
		System.out.println(role3.name());

		System.out.println(UserRole.valueOf("ROLE_ROOT_ADMIN"));
		System.out.println(UserRole.valueOf("ROLE_ORDER_ADMIN"));
		System.out.println(UserRole.valueOf("ROLE_NORMAL"));

		// switch
		UserRole userRole = UserRole.valueOf("ROLE_NORMAL");
		switch (userRole) {
			case ROLE_NORMAL:
				System.out.println(">>> ROLE_NORMAL");
				break;
			case ROLE_ROOT_ADMIN:
				System.out.println(">>> ROLE_ROOT_ADMIN");
				break;
			case ROLE_ORDER_ADMIN:
				System.out.println(">>> ROLE_ORDER_ADMIN");
				break;
		}

		// EnumSet
		EnumSet<UserRole> roleEnumSet = EnumSet.of(
			UserRole.ROLE_ORDER_ADMIN,
			UserRole.ROLE_ROOT_ADMIN
		);
		System.out.println(roleEnumSet.contains(role1));

		// EnumMap
		EnumMap<UserRole, Integer> userRoleEnumMap = new EnumMap<>(UserRole.class);
		userRoleEnumMap.put(role1, 12345);
		System.out.println(userRoleEnumMap);

		System.out.println(RoleEnum.ROLE_ORDER_ADMIN.op());
		System.out.println(RoleEnum.ROLE_NORMAL.op());
		System.out.println(RoleEnum.ROLE_ROOT_ADMIN.op());

		System.out.println(Calculator.ADDITION.execute(11D, 23D));
		System.out.println(Calculator.DIVISION.execute(51D, 21D));
		System.out.println(Calculator.MULTIPLICATION.execute(15D, 26D));
		System.out.println(Calculator.SUBTRACTION.execute(31D, 29D));
	}
}
