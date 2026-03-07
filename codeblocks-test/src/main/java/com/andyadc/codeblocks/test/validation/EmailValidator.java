package com.andyadc.codeblocks.test.validation;

public class EmailValidator {

	public static final Validator<String> DEFAULT = email ->
		email != null &&
			email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");


}
