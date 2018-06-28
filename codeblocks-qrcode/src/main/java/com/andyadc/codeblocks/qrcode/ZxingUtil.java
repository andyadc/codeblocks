package com.andyadc.codeblocks.qrcode;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author andy.an
 * @since 2018/6/28
 */
public final class ZxingUtil {

    protected static boolean isBlank(final CharSequence str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void createFile(byte[] bytes, File file) throws IOException {
        String filePath = file.getCanonicalPath();
        filePath = filePath.replace("\\", "/");
        String directoryPath = filePath.substring(0, filePath.lastIndexOf("/"));
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

        createFile(bytes, directoryPath, fileName);
    }

    protected static void createFile(byte[] bytes, String directoryPath, String fileName) throws IOException {
        createDirectory(directoryPath);

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            File file = new File(directoryPath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } finally {
            if (bos != null) {
                bos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    protected static void createDirectory(File file) throws IOException {
        String filePath = file.getCanonicalPath();
        filePath = filePath.replace("\\", "/");
        String directoryPath = filePath.substring(0, filePath.lastIndexOf("/"));

        createDirectory(directoryPath);
    }

    protected static void createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            directory.mkdirs();
        }
    }
}
