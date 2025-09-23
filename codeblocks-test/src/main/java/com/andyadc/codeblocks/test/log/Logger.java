package com.andyadc.codeblocks.test.log;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Logger {

	private BufferedWriter writer;

	@PostConstruct
	void init() throws IOException {
		Path path = Files.createTempFile("codeblocks-log-", ".log");
		writer = new BufferedWriter(new FileWriter(path.toFile()));
	}

	@PreDestroy
	void close() throws IOException {
		writer.close();
	}

	public void log(String message) {
		try {
			writer.append(message);
			writer.newLine();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public void log(String... parts) {
		try {
			for (String part : parts) {
				writer.append(part);
			}
			writer.newLine();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
