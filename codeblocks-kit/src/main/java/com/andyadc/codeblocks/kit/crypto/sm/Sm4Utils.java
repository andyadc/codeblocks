package com.andyadc.codeblocks.kit.crypto.sm;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

// TODO - test
public class Sm4Utils {

	public static final String ALGORIGTHM_NAME = "SM4";
	public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS7Padding";
	public static final int DEFAULT_KEY_SIZE = 128;
	private static final String ENCODING = "UTF-8";

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public Sm4Utils() {
	}

	/**
	 * 生成ecb暗号
	 */
	private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
		Key sm4Key = new SecretKeySpec(key, ALGORIGTHM_NAME);
		cipher.init(mode, sm4Key);
		return cipher;
	}

	/**
	 * 自动生成密钥
	 */
	public static byte[] generateKey() throws Exception {
		return generateKey(DEFAULT_KEY_SIZE);
	}

	public static byte[] generateKey(int keySize) throws Exception {
		KeyGenerator kg = KeyGenerator.getInstance(ALGORIGTHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
		kg.init(keySize, new SecureRandom());
		return kg.generateKey().getEncoded();
	}

	/**
	 * 加密
	 */
	public static String encryptEcb(String hexKey, String paramStr, String charset) throws Exception {
		String cipherText = "";
		if (null != paramStr && !"".equals(paramStr)) {
			byte[] keyData = Hex.decode(hexKey);
			charset = charset.trim();
			if (charset.length() <= 0) {
				charset = ENCODING;
			}
			byte[] srcData = paramStr.getBytes(charset);
			byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);
			cipherText = Hex.toHexString(cipherArray);
		}
		return cipherText;
	}

	/**
	 * 加密模式之ecb
	 */
	public static byte[] encrypt_Ecb_Padding(byte[] key, byte[] data) throws Exception {
		Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	/**
	 * sm4解密
	 */
	public static String decryptEcb(String hexKey, String cipherText, String charset) throws Exception {
		String decryptStr;
		byte[] keyData = Hex.decode(hexKey);
		byte[] cipherData = Hex.decode(cipherText);
		byte[] srcData = decryptEcbPadding(keyData, cipherData);
		charset = charset.trim();
		if (charset.length() <= 0) {
			charset = ENCODING;
		}
		decryptStr = new String(srcData, charset);
		return decryptStr;
	}

	/**
	 * 解密
	 */
	public static byte[] decryptEcbPadding(byte[] key, byte[] cipherText) throws Exception {
		Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(cipherText);
	}

	/**
	 * 密码校验
	 */
	public static boolean verifyEcb(String hexKey, String cipherText, String paramStr) throws Exception {
		boolean flag;
		byte[] keyData = Hex.decode(hexKey);
		byte[] cipherData = Hex.decode(cipherText);
		byte[] decryptData = decryptEcbPadding(keyData, cipherData);
		byte[] srcData = paramStr.getBytes(ENCODING);
		flag = Arrays.equals(decryptData, srcData);
		return flag;
	}

	public static void main(String[] args) {
		try {
			String value = "andyadc";
			// 自定义的32位16进制密钥
			String secret = "cc9368581322479ebf3e79348a2757d9";
			String cipher = Sm4Utils.encryptEcb(secret, value, ENCODING);
			System.out.println(cipher);
			System.out.println(Sm4Utils.verifyEcb(secret, cipher, value));
			value = Sm4Utils.decryptEcb(secret, cipher, ENCODING);
			System.out.println(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
