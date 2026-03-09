package com.andyadc.codeblocks.test.validation;

public class ValidatorService {

	private final Validator<String> emailValidator;
	private final Validator<String> passwordValidator;

	public ValidatorService(Validator<String> emailValidator, Validator<String> passwordValidator) {
		this.emailValidator = emailValidator;
		this.passwordValidator = passwordValidator;
	}

	public void validate(String email, String password) {
		if (!emailValidator.validate(email)) {
			throw new IllegalArgumentException("邮箱格式错误");
		}

		if (!passwordValidator.validate(password)) {
			throw new IllegalArgumentException("邮箱格式错误");
		}

	}
}
