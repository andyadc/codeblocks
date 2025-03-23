package com.andyadc.summer.boot;

import com.andyadc.summer.io.PropertyResolver;
import com.andyadc.summer.utils.ClassPathUtils;
import com.andyadc.summer.utils.PidUtils;
import com.andyadc.summer.utils.StringUtils;
import com.andyadc.summer.web.ContextLoaderInitializer;
import org.apache.catalina.Context;
import org.apache.catalina.Server;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashSet;

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
		final String javaVersion = System.getProperty("java.version");
		final long pid = PidUtils.getPid();
		final String user = System.getProperty("user.name");
		final String pwd = Paths.get("").toAbsolutePath().toString();
		logger.info("Starting {} using Java {} with PID {} (started by {} in {})", configClass.getSimpleName(), javaVersion, pid, user, pwd);


	}

	protected Server startTomcat(String webDir, String baseDir, Class<?> configClass, PropertyResolver propertyResolver) throws Exception {
		int port = propertyResolver.getProperty("${server.port:8080}", int.class);
		logger.info("starting Tomcat at port {}...", port);
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(port);
		tomcat.getConnector().setThrowOnFailure(true);
		Context ctx = tomcat.addWebapp("", new File(webDir).getAbsolutePath());
		WebResourceRoot resources = new StandardRoot(ctx);
		resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", new File(baseDir).getAbsolutePath(), "/"));
		ctx.setResources(resources);
		ctx.addServletContainerInitializer(
			new ContextLoaderInitializer(configClass, propertyResolver), new HashSet<>()
		);
		tomcat.start();
		logger.info("Tomcat started at port {}...", port);

		return tomcat.getServer();
	}

	protected void printBanner() {
		String banner = ClassPathUtils.readString("/banner.txt");
		StringUtils.lines(banner).forEach(System.out::println);
		// java 9+
		// banner.lines().forEach(System.out::println);
	}

}
