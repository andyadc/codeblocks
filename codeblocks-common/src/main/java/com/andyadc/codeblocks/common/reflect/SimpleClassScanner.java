package com.andyadc.codeblocks.common.reflect;

import com.andyadc.codeblocks.common.filter.PackageNameClassNameFilter;
import com.andyadc.codeblocks.common.function.Streams;
import com.andyadc.codeblocks.common.lang.StringUtils;
import com.andyadc.codeblocks.common.util.CollectionUtils;
import com.andyadc.codeblocks.common.util.URLUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class SimpleClassScanner {

	/**
	 * Singleton
	 */
	public final static SimpleClassScanner INSTANCE = new SimpleClassScanner();

	protected SimpleClassScanner() {
	}

	/**
	 * It's equal to invoke {@link #scan(ClassLoader, String, boolean, boolean)} method with
	 * <code>requiredLoad=false</code> and <code>recursive=false</code>
	 *
	 * @param classLoader {@link ClassLoader}
	 * @param packageName the name of package
	 * @return {@link #scan(ClassLoader, String, boolean, boolean)} method with <code>requiredLoad=false</code>
	 * @throws IllegalArgumentException scanned source is not legal
	 * @throws IllegalStateException    scanned source's state is not valid
	 */
	public Set<Class<?>> scan(ClassLoader classLoader, String packageName) throws IllegalArgumentException, IllegalStateException {
		return scan(classLoader, packageName, false);
	}

	/**
	 * It's equal to invoke {@link #scan(ClassLoader, String, boolean, boolean)} method with
	 * <code>requiredLoad=false</code>
	 *
	 * @param classLoader {@link ClassLoader}
	 * @param packageName the name of package
	 * @param recursive   included sub-package
	 * @return {@link #scan(ClassLoader, String, boolean, boolean)} method with <code>requiredLoad=false</code>
	 * @throws IllegalArgumentException scanned source is not legal
	 * @throws IllegalStateException    scanned source's state is not valid
	 */
	public Set<Class<?>> scan(ClassLoader classLoader, String packageName, boolean recursive) throws IllegalArgumentException, IllegalStateException {
		return scan(classLoader, packageName, recursive, false);
	}

	/**
	 * scan {@link Class} set under specified package name or its' sub-packages in {@link ClassLoader}, if
	 * <code>requiredLoad</code> indicates <code>true</code> , try to load those classes.
	 *
	 * @param classLoader  {@link ClassLoader}
	 * @param packageName  the name of package
	 * @param recursive    included sub-package
	 * @param requiredLoad try to load those classes or not
	 */
	public Set<Class<?>> scan(ClassLoader classLoader,
							  String packageName,
							  final boolean recursive,
							  boolean requiredLoad) throws IllegalArgumentException, IllegalStateException {
		Set<Class<?>> classesSet = new LinkedHashSet<>();
		final String packageResourceName = ClassLoaderUtils.ResourceType.PACKAGE.resolve(packageName);
		try {
			Set<String> classNames = new LinkedHashSet<>();
			// Find in class loader
			Set<URL> resourceURLs = ClassLoaderUtils.getResources(classLoader, ClassLoaderUtils.ResourceType.PACKAGE, packageName);

			if (resourceURLs.isEmpty()) {
				//Find in class path
				List<String> classNamesInPackage = CollectionUtils.newArrayList(ClassUtils.getClassNamesInPackage(packageName));

				if (!classNamesInPackage.isEmpty()) {
					String classPath = ClassUtils.findClassPath(classNamesInPackage.get(0));
					URL resourceURL = new File(classPath).toURI().toURL();
					resourceURLs = CollectionUtils.ofSet(resourceURL);
				}
			}

			for (URL resourceURL : resourceURLs) {
				URL classPathURL = resolveClassPathURL(resourceURL, packageResourceName);
				String classPath = classPathURL.getFile();
				Set<String> classNamesInClassPath = ClassUtils.findClassNamesInClassPath(classPath, true);
				classNames.addAll(filterClassNames(classNamesInClassPath, packageName, recursive));
			}

			for (String className : classNames) {
				Class<?> class_ = requiredLoad ? ClassLoaderUtils.loadClass(classLoader, className) : ClassLoaderUtils.findLoadedClass(classLoader, className);
				if (class_ != null) {
					classesSet.add(class_);
				}
			}
		} catch (IOException e) {
		}
		return Collections.unmodifiableSet(classesSet);
	}

	public Set<Class<?>> scan(ClassLoader classLoader,
							  URL resourceInArchive,
							  boolean requiredLoad,
							  Predicate<Class<?>>... classFilters) {
		File archiveFile = URLUtils.resolveArchiveFile(resourceInArchive);
		Set<String> classNames = ClassUtils.findClassNamesInClassPath(archiveFile, true);
		Set<Class<?>> classesSet = new LinkedHashSet<>();
		for (String className : classNames) {
			Class<?> class_ = requiredLoad ? ClassLoaderUtils.loadClass(classLoader, className) : ClassLoaderUtils.findLoadedClass(classLoader, className);
			if (class_ != null) {
				classesSet.add(class_);
			}
		}
		return Streams.filterAll(classesSet, classFilters);
	}

	public Set<Class<?>> scan(ClassLoader classLoader,
							  File archiveFile,
							  boolean requiredLoad,
							  Predicate<Class<?>>... classFilters) {
		Set<String> classNames = ClassUtils.findClassNamesInClassPath(archiveFile, true);
		Set<Class<?>> classesSet = new LinkedHashSet<>();
		for (String className : classNames) {
			Class<?> class_ = requiredLoad ? ClassLoaderUtils.loadClass(classLoader, className) : ClassLoaderUtils.findLoadedClass(classLoader, className);
			if (class_ != null) {
				classesSet.add(class_);
			}
		}
		return Streams.filterAll(classesSet, classFilters);
	}

	private Set<String> filterClassNames(Set<String> classNames, String packageName, boolean recursive) {
		PackageNameClassNameFilter packageNameClassNameFilter = new PackageNameClassNameFilter(packageName, recursive);
		return CollectionUtils.newLinkedHashSet(Streams.filter(classNames, packageNameClassNameFilter));
	}

	private URL resolveClassPathURL(URL resourceURL, String packageResourceName) {
		String resource = resourceURL.toExternalForm();
		String classPath = StringUtils.substringBefore(resource, packageResourceName);
		URL classPathURL = null;
		try {
			classPathURL = new URL(classPath);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		return classPathURL;
	}
}
