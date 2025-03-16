package com.andyadc.summer.boot;

import com.andyadc.summer.utils.ClassPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.nio.file.Paths;

public class SummerApplication {

	static final String CONFIG_APP_YAML = "/application.yml";
	static final String CONFIG_APP_PROP = "/application.properties";

	final Logger logger = LoggerFactory.getLogger(SummerApplication.class);

	public static void run(String webDir, String baseDir, Class<?> configClass, String... args) throws Exception {
		new SummerApplication().start(webDir, baseDir, configClass, args);
	}

	public void start(String webDir, String baseDir, Class<?> configClass, String... args) throws Exception {
		printBanner();

		// start info:
		final long startTime = System.currentTimeMillis();
		final int javaVersion = Runtime.version().feature();
		final long pid = ManagementFactory.getRuntimeMXBean().getPid();
		final String user = System.getProperty("user.name");
		final String pwd = Paths.get("").toAbsolutePath().toString();
		logger.info("Starting {} using Java {} with PID {} (started by {} in {})", configClass.getSimpleName(), javaVersion, pid, user, pwd);


	}

	protected void printBanner() {
		String banner = ClassPathUtils.readString("/banner.txt");
		banner.lines().forEach(System.out::println);
	}


}
