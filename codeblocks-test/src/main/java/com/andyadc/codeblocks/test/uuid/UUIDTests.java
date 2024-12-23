package com.andyadc.codeblocks.test.uuid;

import com.fasterxml.uuid.Generators;

import java.util.UUID;

/**
 * https://github.com/cowtowncoder/java-uuid-generator
 */
public class UUIDTests {

	public static void main(String[] args) {
		UUID uuid = Generators.timeBasedGenerator().generate(); // Version 1
		System.out.println(uuid.toString());

		uuid = Generators.randomBasedGenerator().generate(); // Version 4
		System.out.println(uuid.toString());

		// With JUG 4.1+: support for https://github.com/uuid6/uuid6-ietf-draft versions 6 and 7:
		uuid = Generators.timeBasedReorderedGenerator().generate(); // Version 6
		System.out.println(uuid.toString());

		uuid = Generators.timeBasedEpochGenerator().generate(); // Version 7
		System.out.println(uuid.toString());

		// With JUG 5.0 added variation:
		uuid = Generators.timeBasedEpochRandomGenerator().generate(); // Version 7 with per-call random values
		System.out.println(uuid.toString());
	}
}
