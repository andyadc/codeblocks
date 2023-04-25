package com.andyadc.bms.validation;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.UsernameRule;
import org.passay.WhitespaceRule;

import java.util.Arrays;

/**
 * http://www.passay.org/
 */
public class PasswordConstraintValidator {

	// Password Strength and Rules
	private static final PasswordValidator validator = new PasswordValidator(
		Arrays.asList(
			// length between 6 and 18 characters
			new LengthRule(6, 18),
			// at least one lower-case character
			new CharacterRule(EnglishCharacterData.LowerCase, 1),
			// at least one upper-case character
			new CharacterRule(EnglishCharacterData.UpperCase, 1),
			// at least one digit character
			new CharacterRule(EnglishCharacterData.Digit, 1),
			// at least one symbol (special character)
			new CharacterRule(EnglishCharacterData.Special, 1),

			// define some illegal sequences that will fail when >= 5 chars long
			// alphabetical is of the form 'abcde', numerical is '34567', qwery is 'asdfg'
			// the false parameter indicates that wrapped sequences are allowed; e.g. 'xyzabc'
//				new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
//				new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false),
			new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),

			new UsernameRule(true, true),

			// no whitespace
			new WhitespaceRule()
		)
	);

	public static boolean isValid(final String password, String username) {
		PasswordData passwordData = new PasswordData(password);
		passwordData.setUsername(username);

		RuleResult ruleResult = validator.validate(passwordData);
		return ruleResult.isValid();
	}
}
