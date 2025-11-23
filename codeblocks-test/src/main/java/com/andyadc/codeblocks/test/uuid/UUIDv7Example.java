package com.andyadc.codeblocks.test.uuid;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public class UUIDv7Example {

	public static void main(String[] args) {
		// Generate UUID v7
		UUID uuid = UuidCreator.getTimeOrderedEpoch();
		System.out.println(uuid);
		System.out.println("Version: " + uuid.version());
	}

}
