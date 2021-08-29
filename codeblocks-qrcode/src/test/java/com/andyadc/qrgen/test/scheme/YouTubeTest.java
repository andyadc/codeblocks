package com.andyadc.qrgen.test.scheme;

import com.andyadc.codeblocks.qrgen.core.scheme.YouTube;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class YouTubeTest {

	private static final String VIDEO = "w3jLJU7DT5E";

	@Test
	public void testParse() {
		assertTrue(YouTube.parse(YouTube.YOUTUBE + ":" + VIDEO).getVideoId().equals(VIDEO));
	}

	@Test
	public void testToString() {
		assertTrue(YouTube.parse(YouTube.YOUTUBE + ":" + VIDEO).toString().equals(YouTube.YOUTUBE + ":" + VIDEO));
	}
}
