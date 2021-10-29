package com.andyadc.codeblocks.test.lib;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;

/**
 * https://mvnrepository.com/artifact/com.github.javafaker/javafaker
 * https://github.com/DiUS/java-faker
 * <p>
 * This library is a port of Ruby's stympy/faker gem (as well as Perl's Data::Faker library) that generates fake data.
 * It's useful when you're developing a new project and need some pretty data for showcase.
 * </p>
 */
public class FakerTests {

	@Test
	public void test() {
		Faker faker = new Faker();
		System.out.println(faker.name().fullName());
	}
}
