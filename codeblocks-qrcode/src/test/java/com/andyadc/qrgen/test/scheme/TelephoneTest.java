package com.andyadc.qrgen.test.scheme;

import com.andyadc.codeblocks.qrgen.core.scheme.Telephone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TelephoneTest {

	private static final String TEL = "+1-212-555-1212";

	@Test
	public void testParse() {
		assertTrue(Telephone.parse("tel:" + TEL).getTelephone().equals(TEL));
	}

	@Test
	public void testToString() {
		assertTrue(Telephone.parse("tel:" + TEL).toString().equals("tel:" + TEL));
	}
}
