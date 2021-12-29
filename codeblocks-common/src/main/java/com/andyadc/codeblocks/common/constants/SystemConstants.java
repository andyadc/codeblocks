package com.andyadc.codeblocks.common.constants;

import static java.lang.System.getProperty;

public interface SystemConstants {

	String JAVA_VERSION = getProperty("java.version");

	String JAVA_VENDOR = getProperty("java.vendor");

	String JAVA_HOME = getProperty("java.home");

	String JAVA_CLASS_VERSION = getProperty("java.class.version");

	String JAVA_CLASS_PATH = getProperty("java.class.path");

	String FILE_SEPARATOR = getProperty("file.separator");

	String PATH_SEPARATOR = getProperty("path.separator");

	String LINE_SEPARATOR = getProperty("line.separator");

	String USER_NAME = getProperty("user.name");

	String USER_HOME = getProperty("user.home");

	String USER_DIR = getProperty("user.dir");

	String JAVA_IO_TMPDIR = getProperty("java.io.tmpdir");
}
