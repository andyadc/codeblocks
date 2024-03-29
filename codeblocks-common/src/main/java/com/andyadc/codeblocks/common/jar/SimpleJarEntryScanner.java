package com.andyadc.codeblocks.common.jar;

import com.andyadc.codeblocks.common.constants.PathConstant;
import com.andyadc.codeblocks.common.lang.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Simple {@link JarEntry} Scanner
 */
public class SimpleJarEntryScanner {

	/**
	 * Singleton
	 */
	public static final SimpleJarEntryScanner INSTANCE = new SimpleJarEntryScanner();

	protected SimpleJarEntryScanner() {
	}

	/**
	 * @param jarURL    {@link URL} of {@link JarFile} or {@link JarEntry}
	 * @param recursive recursive
	 * @return Read-only {@link Set}
	 * @throws NullPointerException     If argument <code>null</code>
	 * @throws IllegalArgumentException <ul> <li>{@link JarUtils#resolveRelativePath(URL)}
	 * @throws IOException              <ul> <li>{@link JarUtils#toJarFile(URL)}
	 */
	public Set<JarEntry> scan(URL jarURL, final boolean recursive) throws NullPointerException, IllegalArgumentException, IOException {
		return scan(jarURL, recursive, null);
	}

	/**
	 * @param jarURL         {@link URL} of {@link JarFile} or {@link JarEntry}
	 * @param recursive      recursive
	 * @param jarEntryFilter {@link Predicate<JarEntry>}
	 * @return Read-only {@link Set}
	 * @throws NullPointerException     If argument <code>null</code>
	 * @throws IllegalArgumentException {@link JarUtils#resolveJarAbsolutePath(URL)}
	 * @throws IOException              {@link JarUtils#toJarFile(URL)}
	 * @see JarEntry
	 */
	public Set<JarEntry> scan(URL jarURL, final boolean recursive, Predicate<JarEntry> jarEntryFilter) throws NullPointerException, IllegalArgumentException, IOException {
		String relativePath = JarUtils.resolveRelativePath(jarURL);
		JarFile jarFile = JarUtils.toJarFile(jarURL);
		return scan(jarFile, relativePath, recursive, jarEntryFilter);
	}

	/**
	 * @param jarFile
	 * @param recursive
	 */
	public Set<JarEntry> scan(JarFile jarFile, final boolean recursive) throws NullPointerException, IllegalArgumentException, IOException {
		return scan(jarFile, recursive, null);
	}

	/**
	 * @param jarFile
	 * @param recursive
	 * @param jarEntryFilter
	 */
	public Set<JarEntry> scan(JarFile jarFile, final boolean recursive, Predicate<JarEntry> jarEntryFilter)
		throws NullPointerException, IllegalArgumentException, IOException {
		return scan(jarFile, StringUtils.EMPTY, recursive, jarEntryFilter);
	}

	protected Set<JarEntry> scan(JarFile jarFile, String relativePath, final boolean recursive,
								 Predicate<JarEntry> jarEntryFilter)
		throws NullPointerException, IllegalArgumentException, IOException {
		Set<JarEntry> jarEntriesSet = new LinkedHashSet<>();
		List<JarEntry> jarEntriesList = JarUtils.filter(jarFile, jarEntryFilter);

		for (JarEntry jarEntry : jarEntriesList) {
			String jarEntryName = jarEntry.getName();
			boolean accept = false;
			if (recursive) {
				accept = jarEntryName.startsWith(relativePath);
			} else {
				if (jarEntry.isDirectory()) {
					accept = jarEntryName.equals(relativePath);
				} else {
					int beginIndex = jarEntryName.indexOf(relativePath);
					if (beginIndex == 0) {
						accept = jarEntryName.indexOf(PathConstant.SLASH, relativePath.length()) < 0;
					}
				}
			}
			if (accept) {
				jarEntriesSet.add(jarEntry);
			}
		}
		return Collections.unmodifiableSet(jarEntriesSet);
	}
}
