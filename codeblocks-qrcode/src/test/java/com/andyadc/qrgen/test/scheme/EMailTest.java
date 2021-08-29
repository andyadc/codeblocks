package com.andyadc.qrgen.test.scheme;

import com.andyadc.codeblocks.qrgen.core.scheme.EMail;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EMailTest {

	private static final String MAIL = "email@address.com";

	@Test
	public void testParse() {
		assertTrue(EMail.parse("mailto:" + MAIL).getEmail().equals(MAIL));
	}

	@Test
	public void testToString() {
		assertTrue(EMail.parse("mailto:" + MAIL).toString().equals("mailto:" + MAIL));
	}
}
