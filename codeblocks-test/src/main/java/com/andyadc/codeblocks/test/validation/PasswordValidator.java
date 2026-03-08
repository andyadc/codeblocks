package com.andyadc.codeblocks.test.validation;

public class PasswordValidator {

	public static final Validator<String> MIN_LENGTH_8 = password ->
		password != null &&
			password.length() > 8;

}
