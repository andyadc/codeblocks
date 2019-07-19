package com.andyadc.codeblocks.test.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * andy.an
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) return true; // can be null
		return value.matches("[0-9]+")
			&& (value.length() > 8)
			&& (value.length() < 14);
	}
}
