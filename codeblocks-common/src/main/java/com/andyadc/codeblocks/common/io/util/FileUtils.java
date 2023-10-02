package com.andyadc.codeblocks.common.io.util;

import com.andyadc.codeblocks.common.lang.StringUtils;

import java.io.File;

import static com.andyadc.codeblocks.common.constants.SystemConstant.FILE_SEPARATOR;
import static com.andyadc.codeblocks.common.util.URLUtils.resolvePath;

public abstract class FileUtils {

	/**
	 * Resolve Relative Path
	 *
	 * @param parentDirectory Parent Directory
	 * @param targetFile      Target File
	 * @return If <code>targetFile</code> is a sub-file of <code>parentDirectory</code> , resolve relative path, or
	 * <code>null</code>
	 * @since 1.0.0
	 */
	public static String resolveRelativePath(File parentDirectory, File targetFile) {
		String parentDirectoryPath = parentDirectory.getAbsolutePath();
		String targetFilePath = targetFile.getAbsolutePath();
		if (!targetFilePath.contains(parentDirectoryPath)) {
			return null;
		}
		return resolvePath(StringUtils.replace(targetFilePath, parentDirectoryPath, FILE_SEPARATOR));
	}
}
