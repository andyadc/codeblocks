package com.andyadc.codeblocks.test.uuid;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

public class UUIDv7Generator {
	private static final SecureRandom RANDOM = new SecureRandom();

	public static UUID generate() {
		// Get current timestamp in milliseconds since epoch
		long timestamp = Instant.now().toEpochMilli();

		// Get random bytes
		byte[] randomBytes = new byte[10];
		RANDOM.nextBytes(randomBytes);

		// Build UUID v7
		// 48 bits timestamp (milliseconds)
		long mostSigBits = timestamp << 16;

		// 12 bits random data for sub-millisecond precision
		mostSigBits |= ((randomBytes[0] & 0x0F) << 8) | (randomBytes[1] & 0xFF);

		// Set version (4 bits = 0111 for v7)
		mostSigBits &= ~(0xF000L);
		mostSigBits |= 0x7000L;

		// Build least significant bits
		long leastSigBits = 0;
		for (int i = 2; i < 10; i++) {
			leastSigBits = (leastSigBits << 8) | (randomBytes[i] & 0xFF);
		}

		// Set variant (2 bits = 10)
		leastSigBits &= ~(0xC000000000000000L);
		leastSigBits |= 0x8000000000000000L;

		return new UUID(mostSigBits, leastSigBits);
	}

	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			UUID uuid = generate();
			System.out.println(uuid + " - Version: " + uuid.version());
		}
	}

}
