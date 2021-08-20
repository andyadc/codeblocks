package com.andyadc.codeblocks.common.filter;

import com.andyadc.codeblocks.common.constants.FileSuffixConstants;

import java.util.jar.JarEntry;

public class ClassFileJarEntryFilter implements JarEntryFilter {

	/**
	 * {@link ClassFileJarEntryFilter} Singleton instance
	 */
	public static final ClassFileJarEntryFilter INSTANCE = new ClassFileJarEntryFilter();

	protected ClassFileJarEntryFilter() {
	}

	@Override
	public boolean accept(JarEntry jarEntry) {
		return !jarEntry.isDirectory() && jarEntry.getName().endsWith(FileSuffixConstants.CLASS);
	}
}
