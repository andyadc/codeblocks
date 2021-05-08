package com.andyadc.codeblocks.test.crypto;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

/**
 * AES(AdvancedEncryptionStandard)
 */
public class AesGcmTest {
	private final static int GCM_IV_LENGTH = 12;
	private final SecureRandom secureRandom = new SecureRandom();

	private static SecretKey generateKey(int i) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(i);
		return keyGenerator.generateKey();
	}

	public static IvParameterSpec generateIv() {
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
	}

	@Test
	void givenString_whenEncrypt_thenSuccess() throws Exception {

		String input = "andyadc";
		SecretKey key = generateKey(128);
		IvParameterSpec spec = generateIv();
		String algorithm = "AES/CBC/PKCS5Padding";

		String cipherText = encrypt(algorithm, input, key, spec);
//		System.out.println(cipherText);
		String plainText = decrypt(algorithm, cipherText, key, spec);
//		System.out.println(plainText);

		Assertions.assertEquals(input, plainText);
	}

	@Test
	public void testEncryption() throws Exception {
		//create new random key
		byte[] key = new byte[16];
		secureRandom.nextBytes(key);

		SecretKey secretKey = new SecretKeySpec(key, "AES");
		byte[] associatedData = "andaicheng".getBytes(StandardCharsets.UTF_8); //meta data you want to verify with the secret message

		String message = "the secret message";
		byte[] cipherText = encrypt(message, secretKey, associatedData);
		System.out.println(cipherText);
		System.out.println();

		String encodeHexString = Hex.encodeHexString(cipherText);
		System.out.println(encodeHexString);
		System.out.println();

		byte[] decodeHex = Hex.decodeHex(encodeHexString);
		System.out.println(decodeHex);
		System.out.println();

		String decrypted = decrypt(cipherText, secretKey, associatedData);
		System.out.println(decrypted);
		System.out.println();

		decrypted = decrypt(decodeHex, secretKey, associatedData);
		System.out.println(decrypted);
		System.out.println();

		String encodeBase64Str = Base64.getEncoder().encodeToString(cipherText);
		System.out.println(encodeBase64Str);
		System.out.println();

		byte[] decodeBase64Bytes = Base64.getDecoder().decode(encodeBase64Str);
		decrypted = decrypt(decodeBase64Bytes, secretKey, associatedData);
		System.out.println(decrypted);
		System.out.println();
	}

	private String encrypt(String algorithm, String plaintext, SecretKey key, AlgorithmParameterSpec spec) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key, spec);

		byte[] bytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(bytes);
	}

	private String decrypt(String algorithm, String cipherText, SecretKey key, AlgorithmParameterSpec spec) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key, spec);

		byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
		return new String(bytes, StandardCharsets.UTF_8);
	}

	/**
	 * Encrypt a plaintext with given key.
	 *
	 * @param plainText      to encrypt (utf-8 encoding will be used)
	 * @param secretKey      to encrypt, must be AES type, see {@link SecretKeySpec}
	 * @param associatedData optional, additional (public) data to verify on decryption with GCM auth tag
	 * @return encrypted message
	 * @throws Exception if anything goes wrong
	 */
	private byte[] encrypt(String plainText, SecretKey secretKey, byte[] associatedData) throws Exception {
		byte[] iv = new byte[GCM_IV_LENGTH]; //NEVER REUSE THIS IV WITH SAME KEY
		secureRandom.nextBytes(iv);

		final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		AlgorithmParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv); //128 bit auth tag length
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

		if (associatedData != null) {
			cipher.updateAAD(associatedData);
		}

		byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

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
