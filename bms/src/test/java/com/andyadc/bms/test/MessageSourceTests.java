package com.andyadc.bms.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import javax.inject.Inject;
import java.util.Locale;

/**
 * https://www.baeldung.com/spring-custom-validation-message-source
 */
@SpringBootTest(
//	webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class MessageSourceTests {

	@Inject
	MessageSource messageSource;

	@Test
	public void testMessage() {
		Locale locale = Locale.getDefault();
		String message = messageSource.getMessage("message.username", null, locale);
		System.out.println(message);

		message = messageSource.getMessage("message.username", null, Locale.ENGLISH);
		System.out.println(message);

		message = messageSource.getMessage("message.username", null, Locale.CHINESE);
		System.out.println(message);
	}
}
