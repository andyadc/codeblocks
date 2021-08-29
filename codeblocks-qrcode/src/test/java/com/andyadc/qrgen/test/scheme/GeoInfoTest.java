package com.andyadc.qrgen.test.scheme;

import com.andyadc.codeblocks.qrgen.core.scheme.GeoInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeoInfoTest {

	private static final String GEO_INFO = "geo:40.71872,-73.98905,100";

	@Test
	public void testParseString() {
		assertTrue(GeoInfo.parse(GEO_INFO).getPoints().size() == 3);
	}

	@Test
	public void testToString() {
		assertTrue(GeoInfo.parse(GEO_INFO).toString().equals(GEO_INFO));
	}

}
