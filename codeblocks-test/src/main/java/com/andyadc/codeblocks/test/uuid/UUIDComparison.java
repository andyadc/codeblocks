package com.andyadc.codeblocks.test.uuid;

import com.github.f4b6a3.uuid.UuidCreator;
import java.util.UUID;

public class UUIDComparison {

	public static void main(String[] args) {
		// UUID v4 (Random)
		UUID v4 = UUID.randomUUID();
		System.out.println("v4: " + v4);

		// UUID v7 (Time-ordered)
		UUID v7 = UuidCreator.getTimeOrderedEpoch();
		System.out.println("v7: " + v7);

		// UUID v7 maintains chronological order
		UUID v7_1 = UuidCreator.getTimeOrderedEpoch();
		UUID v7_2 = UuidCreator.getTimeOrderedEpoch();
		System.out.println("\nChronological comparison:");
		System.out.println(v7_1.compareTo(v7_2) < 0); // true
	}

}
