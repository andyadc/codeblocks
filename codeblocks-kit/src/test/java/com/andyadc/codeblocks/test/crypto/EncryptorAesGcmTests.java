package com.andyadc.codeblocks.test.crypto;

import com.andyadc.codeblocks.kit.crypto.aes.CryptoUtils;
import com.andyadc.codeblocks.kit.crypto.aes.EncryptorAesGcm;

import javax.crypto.SecretKey;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EncryptorAesGcmTests {

	private static final int TAG_LENGTH_BIT = 128;
	private static final int IV_LENGTH_BYTE = 12;
	private static final int AES_KEY_BIT = 256;

	private static final Charset UTF_8 = StandardCharsets.UTF_8;

	public static void main(String[] args) throws Exception {
		String OUTPUT_FORMAT = "%-30s:%s";

		String pText = "Hello World AES-GCM, Welcome to Cryptography!";

		// encrypt and decrypt need the same key.
		// get AES 256 bits (32 bytes) key
		SecretKey secretKey = CryptoUtils.getAESKey(AES_KEY_BIT);

		// encrypt and decrypt need the same IV.
		// AES-GCM needs IV 96-bit (12 bytes)
		byte[] iv = CryptoUtils.getRandomNonce(IV_LENGTH_BYTE);

		byte[] encryptedText = EncryptorAesGcm.encryptWithPrefixIV(pText.getBytes(UTF_8), secretKey, iv);

		System.out.println("\n------ AES GCM Encryption ------");
		System.out.println(String.format(OUTPUT_FORMAT, "Input (plain text)", pText));
		System.out.println(String.format(OUTPUT_FORMAT, "Key (hex)", CryptoUtils.hex(secretKey.getEncoded())));
		System.out.println(String.format(OUTPUT_FORMAT, "IV  (hex)", CryptoUtils.hex(iv)));
		System.out.println(String.format(OUTPUT_FORMAT, "Encrypted (hex) ", CryptoUtils.hex(encryptedText)));
		System.out.println(String.format(OUTPUT_FORMAT, "Encrypted (hex) (block = 16)", CryptoUtils.hexWithBlockSize(encryptedText, 16)));

		System.out.println("\n------ AES GCM Decryption ------");
		System.out.println(String.format(OUTPUT_FORMAT, "Input (hex)", CryptoUtils.hex(encryptedText)));
		System.out.println(String.format(OUTPUT_FORMAT, "Input (hex) (block = 16)", CryptoUtils.hexWithBlockSize(encryptedText, 16)));
		System.out.println(String.format(OUTPUT_FORMAT, "Key (hex)", CryptoUtils.hex(secretKey.getEncoded())));

		String decryptedText = EncryptorAesGcm.decryptWithPrefixIV(encryptedText, secretKey);

		System.out.println(String.format(OUTPUT_FORMAT, "Decrypted (plain text)", decryptedText));
	}
}
