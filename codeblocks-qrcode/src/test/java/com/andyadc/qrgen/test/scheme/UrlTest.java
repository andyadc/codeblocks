package com.andyadc.qrgen.test.scheme;

import com.andyadc.codeblocks.qrgen.core.scheme.Url;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrlTest {

	private static final String URL = "http://github.com/kenglxn/QRGen";

	@Test
	public void testParseString() {
		assertTrue(Url.parse(URL).getUrl().equals(URL));
	}

	@Test
	public void testToString() {
		assertTrue(Url.parse(URL).toString().equals(URL));
	}
}
