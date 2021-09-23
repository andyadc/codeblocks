package com.andyadc.codeblocks.test.crypto;

import com.andyadc.codeblocks.kit.ByteUtil;
import org.jasypt.digest.PooledStringDigester;
import org.jasypt.digest.StandardStringDigester;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.util.binary.AES256BinaryEncryptor;
import org.jasypt.util.binary.BasicBinaryEncryptor;
import org.jasypt.util.binary.StrongBinaryEncryptor;
import org.jasypt.util.digest.Digester;
import org.jasypt.util.numeric.AES256IntegerNumberEncryptor;
import org.jasypt.util.numeric.BasicIntegerNumberEncryptor;
import org.jasypt.util.numeric.StrongIntegerNumberEncryptor;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * http://www.jasypt.org/easy-usage.html
 */
public class JasyptTests {

	/**
	 * General digesting
	 */
	@Test
	public void testDigester() {
		Digester digester = new Digester();
		digester.setAlgorithm("SHA-1");

		byte[] bytes = digester.digest("adc".getBytes(StandardCharsets.UTF_8));
		String hex = ByteUtil.asHex(bytes);
		System.out.println(hex); // aa3159afc1d353ecf7d91cbd242724ce1f99d443
	}

	@Test
	public void testStandardStringDigester() {
		StandardStringDigester digester = new StandardStringDigester();
		digester.setAlgorithm("SHA-1");   // optionally set the algorithm
		digester.setIterations(50000);  // increase security by performing 50000 hashing iterations
	}

	@Test
	public void testPooledStringDigester() {
		PooledStringDigester digester = new PooledStringDigester();
		digester.setPoolSize(4);          // This would be a good value for a 4-core system
		digester.setAlgorithm("SHA-1");
		digester.setIterations(50000);
	}

	/**
	 * Password-Based Encryption (PBE)
	 */
	@Test
	public void testStandardPBEStringEncryptor() {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword("jasypt");                         // we HAVE TO set a password
		encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");   // optionally set the algorithm
		encryptor.setIvGenerator(new RandomIvGenerator());       // for PBE-AES-based algorithms, the IV generator is MANDATORY
	}

	@Test
	public void testPooledPBEStringEncryptor() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setPoolSize(4);          // This would be a good value for a 4-core system
		encryptor.setPassword("jasypt");
		encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
	}

	/**
	 * Password encryption (digesting)
	 */
	@Test
	public void test02() {
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		String encryptedPassword = passwordEncryptor.encryptPassword("adc");
		System.out.println(encryptedPassword);

		boolean check = passwordEncryptor.checkPassword("adc", encryptedPassword);
		System.out.println(check);
	}

	@Test
	public void test03() {
		ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
		passwordEncryptor.setAlgorithm("SHA-1");
		passwordEncryptor.setPlainDigest(true);

		String encryptedPassword = passwordEncryptor.encryptPassword("adc");
		System.out.println(encryptedPassword);

		boolean check = passwordEncryptor.checkPassword("adc", encryptedPassword);
		System.out.println(check);
	}

	/**
	 * Text encryption
	 *
	 * @see StrongTextEncryptor
	 * @see AES256TextEncryptor
	 */
	@Test
	public void test04() {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("123456");

		String myEncryptedText = textEncryptor.encrypt("adc");
		System.out.println(myEncryptedText); // nOMF/wc56Uxd0b0FsaR4aw==

		String plainText = textEncryptor.decrypt("nOMF/wc56Uxd0b0FsaR4aw==");
		System.out.println(plainText);
	}

	/**
	 * Number encryption
	 *
	 * @see StrongIntegerNumberEncryptor
	 * @see AES256IntegerNumberEncryptor
	 */
	@Test
	public void test07() {
		BasicIntegerNumberEncryptor numberEncryptor = new BasicIntegerNumberEncryptor();
		numberEncryptor.setPassword("123");

		BigInteger myEncryptedNumber = numberEncryptor.encrypt(new BigInteger("555"));
		System.out.println(myEncryptedNumber);

		BigInteger plainNumber = numberEncryptor.decrypt(new BigInteger("191477058105907907194973443346685422139984576528"));
		System.out.println(plainNumber);
	}

	/**
	 * Binary encryption
	 *
	 * @see StrongBinaryEncryptor
	 * @see AES256BinaryEncryptor algorithm: PBEWithHMACSHA512AndAES_256
	 */
	@Test
	public void test08() {
		BasicBinaryEncryptor binaryEncryptor = new BasicBinaryEncryptor();
		binaryEncryptor.setPassword("123");

		byte[] myEncryptedBinary = binaryEncryptor.encrypt("321".getBytes(StandardCharsets.UTF_8));
		System.out.println(ByteUtil.asHex(myEncryptedBinary));

		byte[] plainBinary = binaryEncryptor.decrypt(myEncryptedBinary);
		System.out.println(ByteUtil.asHex(plainBinary));
	}

	// ---------------------- Using Jasypt with the Bouncy Castle JCE provider --------------------------

}
