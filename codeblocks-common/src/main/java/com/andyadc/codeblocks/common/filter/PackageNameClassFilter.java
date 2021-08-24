package com.andyadc.codeblocks.common.filter;

import java.util.function.Predicate;

public class PackageNameClassFilter implements Predicate<Class<?>> {

	private final String packageName;
	private final boolean includedSubPackages;
	private final String subPackageNamePrefix;

	/**
	 * Constructor
	 *
	 * @param packageName         the name of package
	 * @param includedSubPackages included sub-packages
	 */
	public PackageNameClassFilter(String packageName, boolean includedSubPackages) {
		this.packageName = packageName;
		this.includedSubPackages = includedSubPackages;
		this.subPackageNamePrefix = includedSubPackages ? packageName + "." : null;
	}

	@Override
	public boolean test(Class<?> filteredObject) {
		Package package_ = filteredObject.getPackage();
		String packageName = package_.getName();
		boolean accepted = packageName.equals(this.packageName);
		if (!accepted && includedSubPackages) {
			accepted = packageName.startsWith(subPackageNamePrefix);
		}
		return accepted;
	}
}
