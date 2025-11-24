package com.andyadc.codeblocks.test.uuid;

import java.time.Instant;
import java.util.UUID;

public class UUIDv7Utility {

	// Extract timestamp from UUID v7
	public static Instant getTimestamp(UUID uuid) {
		if (uuid.version() != 7) {
			throw new IllegalArgumentException("Not a UUID v7");
		}

		// Extract the 48-bit timestamp from most significant bits
		long mostSigBits = uuid.getMostSignificantBits();
		long timestamp = mostSigBits >>> 16;

		return Instant.ofEpochMilli(timestamp);
	}

	// Check if UUID is v7
	public static boolean isUUIDv7(UUID uuid) {
		return uuid.version() == 7;
	}

	public static void main(String[] args) {
		UUID uuid = UUIDv7Generator.generate();

		System.out.println("UUID: " + uuid);
		System.out.println("Version: " + uuid.version());
		System.out.println("Timestamp: " + getTimestamp(uuid));
	}

}
