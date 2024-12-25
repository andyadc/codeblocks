package com.andyadc.codeblocks.test.spring.ioc;

import org.junit.jupiter.api.Test;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.security.PermitAll;
import javax.annotation.sql.DataSourceDefinition;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceResolverTest {

	@Test
	public void scanClass() {
		String pkg = "com.andyadc.codeblocks.test";
		ResourceResolver resolver = new ResourceResolver(pkg);
		List<String> classes = resolver.scan(resource -> {
			String name = resource.name();
			if (name.endsWith(".class")) {
				return name.substring(0, name.length() - 6).replace("/", ".").replace("\\", ".");
			}
			return null;
		});

		Collections.sort(classes);
		System.out.println(classes);

		String[] listClasses = new String[]{
			// list of some scan classes:
			"com.andyadc.codeblocks.test.Tests", //
			"com.andyadc.codeblocks.test.db.mybatis.MyBatisBatchTests", //
		};
		for (String clazz : listClasses) {
			assertTrue(classes.contains(clazz));
		}
	}

	@Test
	public void scanJar() {
		String pkg = PostConstruct.class.getPackage().getName();
		System.out.println(pkg);
		ResourceResolver resolver = new ResourceResolver(pkg);
		List<String> classes = resolver.scan(resource -> {
			String name = resource.name();
			if (name.endsWith(".class")) {
				return name.substring(0, name.length() - 6).replace("/", ".").replace("\\", ".");
			}
			return null;
		});

		System.out.println(classes);

		// classes in jar:
		assertTrue(classes.contains(PostConstruct.class.getName()));
		assertTrue(classes.contains(PreDestroy.class.getName()));
		assertTrue(classes.contains(PermitAll.class.getName()));
		assertTrue(classes.contains(DataSourceDefinition.class.getName()));
	}

	@Test
	public void scanTxt() {
		String pkg = "com.andyadc.codeblocks.test.spring";
		ResourceResolver resolver = new ResourceResolver(pkg);
		List<String> classes = resolver.scan(resource -> {
			String name = resource.name();
			if (name.endsWith(".txt")) {
				return name.replace("\\", "/");
			}
			return null;
		});

		System.out.println(classes);
		Collections.sort(classes);

		assertArrayEquals(new String[]{
			// txt files:
			"com/andyadc/codeblocks/test/spring/resource.txt", //
		}, classes.toArray());
	}

}
