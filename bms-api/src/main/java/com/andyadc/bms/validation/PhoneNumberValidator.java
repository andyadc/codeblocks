package com.andyadc.bms.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

	private static final String DEFAULT_PHONE_CHECK_REGEX = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
	private static Pattern DEFAULT_PATTERN = Pattern.compile(DEFAULT_PHONE_CHECK_REGEX);

	@Override
	public void initialize(PhoneNumber annotation) {
		String phoneRegex = annotation.regex();
		if (phoneRegex.length() > 1) {
			DEFAULT_PATTERN = Pattern.compile(phoneRegex);
		}
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value != null) {
			Matcher matcher = DEFAULT_PATTERN.matcher(value);
			return matcher.matches();
		}
		return true;
	}
}
