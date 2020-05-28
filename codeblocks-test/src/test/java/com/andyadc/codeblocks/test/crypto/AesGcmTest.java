package com.andyadc.codeblocks.test.crypto;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public class AesGcmTest {
	private final SecureRandom secureRandom = new SecureRandom();
	private final static int GCM_IV_LENGTH = 12;

	@Test
	public void testEncryption() throws Exception {
		//create new random key
		byte[] key = new byte[16];
		secureRandom.nextBytes(key);

		SecretKey secretKey = new SecretKeySpec(key, "AES");
		byte[] associatedData = "andaicheng".getBytes(StandardCharsets.UTF_8); //meta data you want to verify with the secret message

		String message = "the secret message";
		byte[] cipherText = encrypt(message, secretKey, associatedData);
		String decrypted = decrypt(cipherText, secretKey, associatedData);

		System.out.println(new String(cipherText, StandardCharsets.ISO_8859_1));
		System.out.println(new String(cipherText, StandardCharsets.US_ASCII));
		System.out.println(cipherText);
		String encodeHexString = Hex.encodeHexString(cipherText);
		System.out.println(encodeHexString);
		byte[] decodeHex = Hex.decodeHex(encodeHexString);
		System.out.println(decodeHex);
		System.out.println(decrypted);

		decrypted = decrypt(decodeHex, secretKey, associatedData);
		System.out.println(decrypted);
	}

	/**
	 * Encrypt a plaintext with given key.
	 *
	 * @param plaintext      to encrypt (utf-8 encoding will be used)
	 * @param secretKey      to encrypt, must be AES type, see {@link SecretKeySpec}
	 * @param associatedData optional, additional (public) data to verify on decryption with GCM auth tag
	 * @return encrypted message
	 * @throws Exception if anything goes wrong
	 */
	private byte[] encrypt(String plaintext, SecretKey secretKey, byte[] associatedData) throws Exception {
		byte[] iv = new byte[GCM_IV_LENGTH]; //NEVER REUSE THIS IV WITH SAME KEY
		secureRandom.nextBytes(iv);

		final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv); //128 bit auth tag length
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

		if (associatedData != null) {
			cipher.updateAAD(associatedData);
		}

		byte[] cipherText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

		ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
		byteBuffer.put(iv);
		byteBuffer.put(cipherText);
		return byteBuffer.array();
	}

	/**
	 * Decrypts encrypted message (see {@link #encrypt(String, SecretKey, byte[])}).
	 *
	 * @param cipherMessage  iv with ciphertext
	 * @param secretKey      used to decrypt
	 * @param associatedData optional, additional (public) data to verify on decryption with GCM auth tag
	 * @return original plaintext
	 * @throws Exception if anything goes wrong
	 */
	private String decrypt(byte[] cipherMessage, SecretKey secretKey, byte[] associatedData) throws Exception {

		final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		//use first 12 bytes for iv
		AlgorithmParameterSpec gcmIv = new GCMParameterSpec(128, cipherMessage, 0, GCM_IV_LENGTH);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmIv);

		if (associatedData != null) {
			cipher.updateAAD(associatedData);
		}
		//use everything from 12 bytes on as ciphertext
		byte[] plainText = cipher.doFinal(cipherMessage, GCM_IV_LENGTH, cipherMessage.length - GCM_IV_LENGTH);

		return new String(plainText, StandardCharsets.UTF_8);
	}
}
