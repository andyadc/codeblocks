package com.andyadc.codeblocks.test.crypto.hkdf;

import com.andyadc.codeblocks.kit.crypto.hkdf.HkdfMacFactory;
import org.junit.Test;

import javax.crypto.Mac;
import java.security.Security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HkdfMacFactoryTest {
	@Test
	public void hmacSha256() {
		testHmacFactory(HkdfMacFactory.Default.hmacSha256(), 32);
	}

	@Test
	public void hmacSha512() {
		testHmacFactory(HkdfMacFactory.Default.hmacSha512(), 64);
	}

	@Test
	public void hmacSha1() {
		testHmacFactory(HkdfMacFactory.Default.hmacSha1(), 20);
	}

	@Test
	public void hmacMd5() {
		testHmacFactory(new HkdfMacFactory.Default("HmacMD5"), 16);
	}

	@Test
	public void customProvider() {
		testHmacFactory(new HkdfMacFactory.Default("HmacSHA1", Security.getProvider("SunJCE")), 20);
	}

	@Test(expected = RuntimeException.class)
	public void hmacInstanceNotExisting() {
		new HkdfMacFactory.Default("HmacNotExisting", null).getMacLengthBytes();
	}

	@Test(expected = RuntimeException.class)
	public void hmacUsingEmptyKey() {
		HkdfMacFactory.Default.hmacSha256().createInstance(null);
	}

	private void testHmacFactory(HkdfMacFactory macFactory, int refLength) {
		Mac mac = macFactory.createInstance(macFactory.createSecretKey(new byte[refLength]));
		assertNotNull(mac);

		mac.update(new byte[]{0x76, (byte) 0x92, 0x0E, 0x5E, (byte) 0x85, (byte) 0xDB, (byte) 0xA7, (byte) 0x8F});
		byte[] hash = mac.doFinal();
		assertEquals(refLength, hash.length);
	}
}
