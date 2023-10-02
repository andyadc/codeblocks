package com.andyadc.codeblocks.common.jar;

import com.andyadc.codeblocks.common.constants.FileSuffixConstant;
import com.andyadc.codeblocks.common.constants.ProtocolConstant;
import com.andyadc.codeblocks.common.constants.SeparatorConstants;
import com.andyadc.codeblocks.common.function.Streams;
import com.andyadc.codeblocks.common.lang.StringUtils;
import com.andyadc.codeblocks.common.util.URLUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Jar Utility class
 */
public class JarUtils {

	/**
	 * Create a {@link JarFile} from specified {@link URL} of {@link JarFile}
	 *
	 * @param jarURL {@link URL} of {@link JarFile} or {@link JarEntry}
	 * @return JarFile
	 * @throws IOException If {@link JarFile jar file} is invalid, see {@link JarFile#JarFile(String)}
	 */
	public static JarFile toJarFile(URL jarURL) throws IOException {
		JarFile jarFile = null;
		final String jarAbsolutePath = resolveJarAbsolutePath(jarURL);
		if (jarAbsolutePath == null)
			return null;
		jarFile = new JarFile(jarAbsolutePath);
		return jarFile;
	}

	/**
	 * Assert <code>jarURL</code> argument is valid , only supported protocols : {@link ProtocolConstant#JAR jar} and
	 * {@link ProtocolConstant#FILE file}
	 *
	 * @param jarURL {@link URL} of {@link JarFile} or {@link JarEntry}
	 * @throws NullPointerException     If <code>jarURL</code> is <code>null</code>
	 * @throws IllegalArgumentException If {@link URL#getProtocol()} is not {@link ProtocolConstant#JAR jar} or {@link ProtocolConstant#FILE
	 *                                  file}
	 */
	protected static void assertJarURLProtocol(URL jarURL) throws NullPointerException, IllegalArgumentException {
		final String protocol = jarURL.getProtocol(); //NPE check
		if (!ProtocolConstant.JAR.equals(protocol)
			&& !ProtocolConstant.FILE.equals(protocol)) {
			String message = String.format("jarURL Protocol[%s] is unsupported ,except %s and %s ", protocol, ProtocolConstant.JAR, ProtocolConstant.FILE);
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Resolve Relative path from Jar URL
	 *
	 * @param jarURL {@link URL} of {@link JarFile} or {@link JarEntry}
	 * @return Non-null
	 * @throws NullPointerException     see {@link #assertJarURLProtocol(URL)}
	 * @throws IllegalArgumentException see {@link #assertJarURLProtocol(URL)}
	 */
	public static String resolveRelativePath(URL jarURL) throws NullPointerException, IllegalArgumentException {
		assertJarURLProtocol(jarURL);
		String form = jarURL.toExternalForm();
		String relativePath = StringUtils.substringAfter(form, SeparatorConstants.ARCHIVE_ENTITY);
		relativePath = URLUtils.resolvePath(relativePath);
		return URLUtils.decode(relativePath);
	}

	/**
	 * Resolve absolute path from the {@link URL} of {@link JarEntry}
	 *
	 * @param jarURL {@link URL} of {@link JarFile} or {@link JarEntry}
	 * @return If {@link URL#getProtocol()} equals <code>jar</code> or <code>file</code> , resolves absolute path, or
	 * return <code>null</code>
	 * @throws NullPointerException     see {@link #assertJarURLProtocol(URL)}
	 * @throws IllegalArgumentException see {@link #assertJarURLProtocol(URL)}
	 */
	public static String resolveJarAbsolutePath(URL jarURL) throws NullPointerException, IllegalArgumentException {
		assertJarURLProtocol(jarURL);
		File archiveFile = URLUtils.resolveArchiveFile(jarURL, FileSuffixConstant.JAR);
		return archiveFile == null ? null : archiveFile.getAbsolutePath();
	}

	/**
	 * Filter {@link JarEntry} list from {@link JarFile}
	 *
	 * @param jarFile        {@link JarFile}
	 * @param jarEntryFilter {@link Predicate}
	 * @return Read-only List
	 */
	public static List<JarEntry> filter(JarFile jarFile, Predicate<JarEntry> jarEntryFilter) {
		if (jarFile == null) {
			return Collections.emptyList();
		}
		Enumeration<JarEntry> jarEntries = jarFile.entries();
		List<JarEntry> jarEntriesList = Collections.list(jarEntries);
		return Streams.filter(jarEntriesList, jarEntryFilter);
	}

	/**
	 * Find {@link JarEntry} from specified <code>url</code>
	 *
	 * @param jarURL jar resource url
	 * @return If found , return {@link JarEntry}
	 */
	public static JarEntry findJarEntry(URL jarURL) throws IOException {
		JarFile jarFile = JarUtils.toJarFile(jarURL);
		final String relativePath = JarUtils.resolveRelativePath(jarURL);
		return jarFile.getJarEntry(relativePath);
	}

	/**
	 * Extract the source {@link JarFile} to target directory
	 *
	 * @param jarSourceFile   the source {@link JarFile}
	 * @param targetDirectory target directory
	 * @throws IOException When the source jar file is an invalid {@link JarFile}
	 */
	public static void extract(File jarSourceFile, File targetDirectory) throws IOException {
		extract(jarSourceFile, targetDirectory, null);
	}

	/**
	 * Extract the source {@link JarFile} to target directory with specified {@link JarEntry}
	 *
	 * @param jarSourceFile   the source {@link JarFile}
	 * @param targetDirectory target directory
	 * @param jarEntryFilter  {@link JarEntry}
	 * @throws IOException When the source jar file is an invalid {@link JarFile}
	 */
	public static void extract(File jarSourceFile, File targetDirectory, Predicate<JarEntry> jarEntryFilter) throws IOException {
		final JarFile jarFile = new JarFile(jarSourceFile);
		extract(jarFile, targetDirectory, jarEntryFilter);
	}

	/**
	 * Extract the source {@link JarFile} to target directory with specified {@link Predicate<JarEntry>}
	 *
	 * @param jarFile         the source {@link JarFile}
	 * @param targetDirectory target directory
	 * @param jarEntryFilter  {@link JarEntry}
	 * @throws IOException When the source jar file is an invalid {@link JarFile}
	 */
	public static void extract(JarFile jarFile, File targetDirectory, Predicate<JarEntry> jarEntryFilter) throws IOException {
		List<JarEntry> jarEntriesList = filter(jarFile, jarEntryFilter);
		doExtract(jarFile, jarEntriesList, targetDirectory);
	}

	/**
	 * Extract the source {@link JarFile} to target directory with specified {@link Predicate<JarEntry>}
	 *
	 * @param jarResourceURL  The resource URL of {@link JarFile} or {@link JarEntry}
	 * @param targetDirectory target directory
	 * @param jarEntryFilter  {@link JarEntry}
	 * @throws IOException When the source jar file is an invalid {@link JarFile}
	 */
	public static void extract(URL jarResourceURL, File targetDirectory, Predicate<JarEntry> jarEntryFilter) throws IOException {
		final JarFile jarFile = JarUtils.toJarFile(jarResourceURL);
		final String relativePath = JarUtils.resolveRelativePath(jarResourceURL);
		final JarEntry jarEntry = jarFile.getJarEntry(relativePath);
		final boolean isDirectory = jarEntry.isDirectory();
		List<JarEntry> jarEntriesList = filter(jarFile, filteredObject -> {
			String name = filteredObject.getName();
			if (isDirectory && name.equals(relativePath)) {
				return true;
			} else return name.startsWith(relativePath);
		});

		jarEntriesList = Streams.filter(jarEntriesList, jarEntryFilter);
		doExtract(jarFile, jarEntriesList, targetDirectory);
	}

	protected static void doExtract(JarFile jarFile, Iterable<JarEntry> jarEntries, File targetDirectory) throws IOException {
		if (jarEntries != null) {
			for (JarEntry jarEntry : jarEntries) {
				String jarEntryName = jarEntry.getName();
				File targetFile = new File(targetDirectory, jarEntryName);
				if (jarEntry.isDirectory()) {
					targetFile.mkdirs();
				} else {
					InputStream inputStream = null;
					OutputStream outputStream = null;
					try {
						inputStream = jarFile.getInputStream(jarEntry);
						if (inputStream != null) {
							File parentFile = targetFile.getParentFile();
							if (!parentFile.exists()) {
								parentFile.mkdirs();
							}
							outputStream = new FileOutputStream(targetFile);
							IOUtils.copy(inputStream, outputStream);
						}
					} finally {
						IOUtils.closeQuietly(outputStream);
						IOUtils.closeQuietly(inputStream);
					}
				}
			}
		}
	}
}
