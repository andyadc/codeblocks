package com.andyadc.qrgen.test.scheme;

import com.andyadc.codeblocks.qrgen.core.scheme.Wifi;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WifiTest {

	@Test
	public void parse() {
		Wifi wifi = Wifi.parse(
			"WIFI:S:some weird SSID;T:WPA;P:aintNoSecret;H:true;");
		assertEquals("some weird SSID", wifi.getSsid());
		assertEquals("WPA", wifi.getAuthentication());
		assertEquals("aintNoSecret", wifi.getPsk());
		assertEquals(true, wifi.isHidden());
	}

	/**
	 * The following characters need to be escaped with a backslash (\) in the
	 * SSID and PSK strings: backslash (\), single-quote ('), double-quote ("),
	 * dot (.), colon (:), comma (,), and semicolon (;)
	 */
	@Test
	public void parseEscapeSsidAndPassword() {
		Wifi wifi = Wifi.parse(
			"WIFI:S:s\\;o\\,\\\"me \\'wei\\\\rd\\. SSID\\;;T:WPA;P:\\;a\\,\\\"intNo\\,Sec\\\\ret;false;");

		assertEquals("s;o,\"me 'wei\\rd. SSID;", wifi.getSsid());
		assertEquals("WPA", wifi.getAuthentication());
		assertEquals(";a,\"intNo,Sec\\ret", wifi.getPsk());
		assertEquals(false, wifi.isHidden());
	}

	@Test
	public void parseNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			Wifi.parse(null);
		});
	}

	@Test
	public void parseEmptyString() {
		assertThrows(IllegalArgumentException.class, () -> {
			Wifi.parse("");
		});
	}

	@Test
	public void parseHeaderOnly() {
		Wifi wifi = Wifi.parse("WIFI:");
		assertNull(null, wifi.getSsid());
		assertNull(null, wifi.getAuthentication());
		assertNull(null, wifi.getPsk());
		assertEquals(false, wifi.isHidden());
	}

	@Test
	public void testToString() {
		Wifi wifi = new Wifi();
		wifi.setSsid("some weird SSID");
		wifi.setAuthentication(Wifi.Authentication.WPA);
		wifi.setPsk("aintNoSecret");
		wifi.setHidden(true);

		assertEquals("WIFI:S:some weird SSID;T:WPA;P:aintNoSecret;H:true;",
			wifi.toString());
	}

	@Test
	public void testToStringEscapeSsidAndPassword() {
		Wifi wifi = new Wifi();
		wifi.setSsid("s;o,\"me 'wei\\rd. SSID;");
		wifi.setAuthentication(Wifi.Authentication.WPA);
		wifi.setPsk(";a,\"intNo,Sec\\ret");
		wifi.setHidden(false);

		assertEquals("WIFI:S:s\\;o\\,\\\"me \\'wei\\\\rd\\. SSID\\;;T:WPA;P:\\;a\\,\\\"intNo\\,Sec\\\\ret;H:false;",
			wifi.toString());
	}
}
