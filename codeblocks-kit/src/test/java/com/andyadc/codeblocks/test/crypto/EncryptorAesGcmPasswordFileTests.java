package com.andyadc.codeblocks.test.crypto;

import com.andyadc.codeblocks.kit.crypto.aes.EncryptorAesGcmPasswordFile;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EncryptorAesGcmPasswordFileTests {

	private static final Charset UTF_8 = StandardCharsets.UTF_8;

	public static void main(String[] args) throws Exception {
		String password = "password123";
		String fromFile = "readme.txt"; // from resources folder
		String toFile = "c:\\test\\readme.encrypted.txt";

		// encrypt file
		//EncryptorAesGcmPasswordFile.encryptFile(fromFile, toFile, password);

		// decrypt file
		byte[] decryptedText = EncryptorAesGcmPasswordFile.decryptFile(toFile, password);
		String pText = new String(decryptedText, UTF_8);
		System.out.println(pText);
	}
}
