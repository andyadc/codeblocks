package com.andyadc.codeblocks.test.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherTest {

	// 密钥长度
	public static final int AES_KEY_SIZE = 256;
	// 初始化向量长度
	public static final int GCM_IV_LENGTH = 12;
	// GCM身份认证Tag长度
	public static final int GCM_TAG_LENGTH = 16;

	public static byte[] doEncrypt(byte[] plainBytes, SecretKey key, byte[] iv, byte[] aad) throws Exception {
		// 加密算法
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		// Key规范
		SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
		// GCM参数规范
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
		// 加密模式
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
		// 设置aad
		if (aad != null) {
			cipher.updateAAD(aad);
		}
		// 加密
		byte[] cipherBytes = cipher.doFinal(plainBytes);
		return cipherBytes;
	}

	public static String doDecrypt(byte[] cipherBytes, SecretKey key, byte[] iv, byte[] aad) throws Exception {
		// 加密算法
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		// Key规范
		SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
		// GCM参数规范
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
		// 解密模式
		cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
		// 设置aad
		if (aad != null) {
			cipher.updateAAD(aad);
		}
		// 解密
		byte[] decryptedBytes = cipher.doFinal(cipherBytes);
		return new String(decryptedBytes);
	}
}
