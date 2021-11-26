package com.andyadc.codeblocks.test.crypto;

import com.andyadc.codeblocks.kit.crypto.aes.EncryptorAesGcmPassword;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EncryptorAesGcmPasswordTests {

	private static final Charset UTF_8 = StandardCharsets.UTF_8;

	public static void main(String[] args) throws Exception {
		String OUTPUT_FORMAT = "%-30s:%s";
		String PASSWORD = "this is a password";
		String pText = "AES-GSM Password-Bases encryption!";

		String encryptedTextBase64 = EncryptorAesGcmPassword.encrypt(pText.getBytes(UTF_8), PASSWORD);

		System.out.println("\n------ AES GCM Password-based Encryption ------");
		System.out.println(String.format(OUTPUT_FORMAT, "Input (plain text)", pText));
		System.out.println(String.format(OUTPUT_FORMAT, "Encrypted (base64) ", encryptedTextBase64));

		System.out.println("\n------ AES GCM Password-based Decryption ------");
		System.out.println(String.format(OUTPUT_FORMAT, "Input (base64)", encryptedTextBase64));

		String decryptedText = EncryptorAesGcmPassword.decrypt(encryptedTextBase64, PASSWORD);
		System.out.println(String.format(OUTPUT_FORMAT, "Decrypted (plain text)", decryptedText));
	}
}
