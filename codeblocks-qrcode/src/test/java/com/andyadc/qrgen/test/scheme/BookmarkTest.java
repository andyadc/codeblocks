package com.andyadc.qrgen.test.scheme;

import com.andyadc.codeblocks.qrgen.core.scheme.Bookmark;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BookmarkTest {

	private static final String BOOKMARK = "MEBKM:URL:google.com;TITLE:Google;;";

	@Test
	public void testParse() {
		Bookmark bookmark = Bookmark.parse(BOOKMARK);
		Assertions.assertEquals("google.com", bookmark.getUrl());
		Assertions.assertEquals("Google", bookmark.getTitel());
	}

	@Test
	public void testToString() {
		Bookmark bookmark = new Bookmark();
		bookmark.setUrl("google.com");
		bookmark.setTitel("Google");
		Assertions.assertEquals(BOOKMARK, bookmark.toString());
	}
}
