package com.andyadc.qrgen.test.scheme;

import com.andyadc.codeblocks.qrgen.core.scheme.GooglePlay;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GooglePlayTest {

	private static final String APP = "de.pawlidi.android";
	private static final String CODE = "{{{market://details?id=" + APP + "}}}";

	@Test
	public void testParse() {
		assertTrue(GooglePlay.parse(CODE).getAppPackage().equals(APP));
	}

	@Test
	public void testToString() {
		assertTrue(GooglePlay.parse(CODE).toString().equals(CODE));
	}
}
