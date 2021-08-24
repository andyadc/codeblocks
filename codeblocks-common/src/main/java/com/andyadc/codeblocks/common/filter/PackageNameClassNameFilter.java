package com.andyadc.codeblocks.common.filter;

import com.andyadc.codeblocks.common.reflect.ClassUtils;

import java.util.function.Predicate;

public class PackageNameClassNameFilter implements Predicate<String> {

	private final String packageName;
	private final boolean includedSubPackages;
	private final String subPackageNamePrefix;

	/**
	 * Constructor
	 *
	 * @param packageName         the name of package
	 * @param includedSubPackages included sub-packages
	 */
	public PackageNameClassNameFilter(String packageName, boolean includedSubPackages) {
		this.packageName = packageName;
		this.includedSubPackages = includedSubPackages;
		this.subPackageNamePrefix = includedSubPackages ? packageName + "." : null;
	}

	@Override
	public boolean test(String className) {
		String packageName = ClassUtils.resolvePackageName(className);
		boolean accepted = packageName.equals(this.packageName);
		if (!accepted && includedSubPackages) {
			accepted = packageName.startsWith(subPackageNamePrefix);
		}
		return accepted;
	}
}
