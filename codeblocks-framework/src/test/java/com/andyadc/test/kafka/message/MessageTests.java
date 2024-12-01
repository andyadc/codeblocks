package com.andyadc.test.kafka.message;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

public abstract class MessageTests {

	protected static void printToClose(ClassPathXmlApplicationContext ctx) {
		System.out.println("Enter strings (type 'close' to exit):");
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String input = scanner.nextLine();
			if (input.equals("close")) {
				System.out.println("Program terminated.");
				break;
			}
			System.out.println("You entered: " + input);
		}
		scanner.close();
		ctx.close();
	}

}
