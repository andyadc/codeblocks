package com.andyadc.qrgen.test.scheme;

import com.andyadc.codeblocks.qrgen.core.scheme.BizCard;
import com.andyadc.codeblocks.qrgen.core.scheme.EMail;
import com.andyadc.codeblocks.qrgen.core.scheme.EnterpriseWifi;
import com.andyadc.codeblocks.qrgen.core.scheme.GeoInfo;
import com.andyadc.codeblocks.qrgen.core.scheme.Girocode;
import com.andyadc.codeblocks.qrgen.core.scheme.GooglePlay;
import com.andyadc.codeblocks.qrgen.core.scheme.ICal;
import com.andyadc.codeblocks.qrgen.core.scheme.KddiAu;
import com.andyadc.codeblocks.qrgen.core.scheme.MMS;
import com.andyadc.codeblocks.qrgen.core.scheme.MeCard;
import com.andyadc.codeblocks.qrgen.core.scheme.SMS;
import com.andyadc.codeblocks.qrgen.core.scheme.Telephone;
import com.andyadc.codeblocks.qrgen.core.scheme.Url;
import com.andyadc.codeblocks.qrgen.core.scheme.VCard;
import com.andyadc.codeblocks.qrgen.core.scheme.Wifi;
import com.andyadc.codeblocks.qrgen.core.scheme.parser.ExtendableQRCodeSchemeParser;
import com.andyadc.codeblocks.qrgen.core.scheme.parser.QRCodeSchemeParser;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ExtendableQRCodeSchemeParserTest {

	protected QRCodeSchemeParser createParser() {
		return new ExtendableQRCodeSchemeParser();
	}

	@Test
	public void getSupportedSchemes() {
		Set<Class<?>> expectedTypes = new LinkedHashSet<Class<?>>();
		expectedTypes.add(Girocode.class);
		expectedTypes.add(VCard.class);
		expectedTypes.add(Wifi.class);
		expectedTypes.add(BizCard.class);
		expectedTypes.add(EMail.class);
		expectedTypes.add(EnterpriseWifi.class);
		expectedTypes.add(GeoInfo.class);
		expectedTypes.add(GooglePlay.class);
		expectedTypes.add(ICal.class);
		expectedTypes.add(KddiAu.class);
		expectedTypes.add(MeCard.class);
		expectedTypes.add(MMS.class);
		expectedTypes.add(SMS.class);
		expectedTypes.add(Telephone.class);
		expectedTypes.add(Url.class);
		expectedTypes.add(Foo.class);
		assertEquals(expectedTypes, createParser().getSupportedSchemes());
	}

	@Test
	public void parseWifi() throws Exception {
		Object scheme = createParser().parse("WIFI:S:some weird SSID;T:WPA;P:aintNoSecret;H:true;");
		assertNotNull(scheme);
		// TODO
//		assertThat(scheme, is(Wifi.class));
		Wifi wifi = (Wifi) scheme;
		assertEquals("some weird SSID", wifi.getSsid());
		assertEquals("WPA", wifi.getAuthentication());
		assertEquals("aintNoSecret", wifi.getPsk());
		assertEquals(true, wifi.isHidden());
	}

	@Test
	public void parseVCard() throws Exception {
		Object scheme = createParser().parse("BEGIN:VCARD\n" + //
			"VERSION:3.0\n" + //
			"N:Cookiemonster\n" + //
			"ORG:CTV\n" + //
			"TITLE:monster\n" + //
			"TEL:0023478324\n" + //
			"URL:www.sesamestreet.com\n" + //
			"EMAIL:cookiemonster@sesamestreet.com\n" + //
			"ADR:Sesamestreet 1\n" + //
			"NOTE:more cookies, please\n" + //
			"END:VCARD");
		assertNotNull(scheme);
		// TODO
//		assertThat(scheme, is(VCard.class));
		VCard vcard = (VCard) scheme;
		assertEquals("Cookiemonster", vcard.getName());
		assertEquals("Sesamestreet 1", vcard.getAddress());
		assertEquals("CTV", vcard.getCompany());
		assertEquals("cookiemonster@sesamestreet.com", vcard.getEmail());
		assertEquals("monster", vcard.getTitle());
		assertEquals("www.sesamestreet.com", vcard.getWebsite());
		assertEquals("0023478324", vcard.getPhoneNumber());
		assertEquals("more cookies, please", vcard.getNote());
	}

	@Test
	public void parseGirocode() throws Exception {
		Object scheme = createParser().parse("BCD\n" + //
			"001\n" + //
			"1\n" + //
			"SCT\n" + //
			"DAAABCDGGD\n" + //
			"Miss Marple\n" + //
			"DE91300776014444814989\n" + //
			"EUR27.06\n" + //
			"xyz\n" + //
			"reference\n" + //
			"for a good prupose\n" + //
			"Watch this Girocode :-)"

		);
		assertNotNull(scheme);
		// TODO
//		assertThat(scheme, is(Girocode.class));
		Girocode girocode = (Girocode) scheme;
		assertEquals(Girocode.Encoding.UTF_8, girocode.getEncoding());
		assertEquals("DAAABCDGGD", girocode.getBic());
		assertEquals("Miss Marple", girocode.getName());
		assertEquals("DE91300776014444814989", girocode.getIban());
		assertEquals("EUR27.06", girocode.getAmount());
		assertEquals("xyz", girocode.getPurposeCode());
		assertEquals("reference", girocode.getReference());
		assertEquals("for a good prupose", girocode.getText());
		assertEquals("Watch this Girocode :-)", girocode.getHint());
	}

	@Test
	public void parseUrlCode() throws Exception {
		Object scheme = createParser().parse("http://www.github.org/QRCode");
		assertNotNull(scheme);
		// TODO
//		assertThat(scheme, is(Url.class));
		Url urlCode = (Url) scheme;
		assertEquals("http://www.github.org/QRCode", urlCode.getUrl());
	}

	@Test
	public void parseUnknownScheme() throws Exception {
		assertThrows(UnsupportedEncodingException.class, () -> {
			Object o = createParser().parse("xx");
			System.out.println(o);
		});
	}

	@Test
	public void useParserExtension() throws Exception {
		Object scheme = createParser().parse("foo:bar");
		assertNotNull(scheme);
		// TODO
//		assertThat(scheme, is(Foo.class));
	}
}
