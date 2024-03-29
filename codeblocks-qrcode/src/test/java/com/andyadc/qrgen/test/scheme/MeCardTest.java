package com.andyadc.qrgen.test.scheme;

import com.andyadc.codeblocks.qrgen.core.scheme.MeCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MeCardTest {

	private static final String MECARD = "MECARD:N:Owen,Sean;ADR:76 9th Avenue, 4th Floor, New York, NY 10011;TEL:12125551212;EMAIL:srowen@example.com;;";

	@Test
	public void testParse() {
		MeCard meCard = MeCard.parse(MECARD);
		assertEquals("Owen,Sean", meCard.getName());
		assertEquals("76 9th Avenue, 4th Floor, New York, NY 10011", meCard.getAddress());
		assertEquals("12125551212", meCard.getTelephone());
		assertEquals("srowen@example.com", meCard.getEmail());
	}

	@Test
	public void testToString() {
		MeCard meCard = new MeCard();
		meCard.setName("Owen,Sean");
		meCard.setAddress("76 9th Avenue, 4th Floor, New York, NY 10011");
		meCard.setTelephone("12125551212");
		meCard.setEmail("srowen@example.com");
		assertEquals(MECARD, meCard.toString());
	}

}
