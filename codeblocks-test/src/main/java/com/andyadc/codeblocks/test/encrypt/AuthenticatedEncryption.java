package com.andyadc.codeblocks.test.encrypt;

import jakarta.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface AuthenticatedEncryption {

	@Retention(RetentionPolicy.SOURCE)
	@interface KeyStrength {
	}

	/**
	 * High Security which is equivalent to a AES key size of 128 bit
	 */
	int STRENGTH_HIGH = 0;

	/**
	 * Very high security which is equivalent to a AES key size of 256 bit
	 * Note: This is usually not required.
	 */
	int STRENGTH_VERY_HIGH = 1;

	/**
	 * Encrypts and adds a authentication tag the given content
	 *
	 * @param rawEncryptionKey to use as encryption key material
	 * @param rawData          to encrypt
	 * @param associatedData   additional data used to create the auth tag and will be subject to integrity/authentication check
	 * @return encrypted content
	 * @throws AuthenticatedEncryptionException if any crypto fails
	 */
	byte[] encrypt(byte[] rawEncryptionKey, byte[] rawData, @Nullable byte[] associatedData) throws AuthenticatedEncryptionException;

	/**
	 * Decrypt and verifies the authenticity of given encrypted data
	 *
	 * @param rawEncryptionKey to use as decryption key material
	 * @param encryptedData    to decrypt
	 * @param associatedData   additional data used to create the auth tag; must be same as provided
	 *                         in the encrypt step
	 * @return decrypted, original data
	 * @throws AuthenticatedEncryptionException if any crypto fails
	 */
	byte[] decrypt(byte[] rawEncryptionKey, byte[] encryptedData, byte[] associatedData) throws AuthenticatedEncryptionException;

	/**
	 * Get the required key size length in bytes for given security strength type
	 *
	 * @param keyStrengthType STRENGTH_HIGH or STRENGTH_VERY_HIGH
	 * @return required size in byte
	 */
	int byteSizeLength(@KeyStrength int keyStrengthType);
}
