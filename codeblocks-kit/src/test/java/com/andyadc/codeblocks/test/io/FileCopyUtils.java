package com.andyadc.codeblocks.test.io;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * <code>org.springframework.util.FileCopyUtils</code>
 * <p>
 * Simple utility methods for file and stream copying. All copy methods use a block size
 * of 4096 bytes, and close all affected streams when done. A variation of the copy
 * methods from this class that leave streams open can be found in {@link StreamUtils}.
 *
 * @author andaicheng
 * @version 2016/12/4
 * @see StreamUtils
 */
public abstract class FileCopyUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileCopyUtils.class);

    private static final int BUFFER_SIZE = StreamUtils.BUFFER_SIZE;

    private FileCopyUtils() {
    }

    //---------------------------------------------------------------------
    // Copy methods for java.io.File
    //---------------------------------------------------------------------

    /**
     * Copy the contents of the given input File to the given output File.
     *
     * @param in  the file to copy from
     * @param out the file to copy to
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static int copy(File in, File out) throws IOException {
        Validate.notNull(in, "No input File specified");
        Validate.notNull(out, "No output File specified");
        return copy(new BufferedInputStream(new FileInputStream(in)),
                new BufferedOutputStream(new FileOutputStream(out)));
    }

    /**
     * Copy the contents of the given byte array to the given output File.
     *
     * @param in  the byte array to copy from
     * @param out the file to copy to
     * @throws IOException in case of I/O errors
     */
    public static void copy(byte[] in, File out) throws IOException {
        Validate.notNull(in, "No input byte array specified");
        Validate.notNull(out, "No output File specified");
        ByteArrayInputStream inStream = new ByteArrayInputStream(in);
        OutputStream outStream = new BufferedOutputStream(new FileOutputStream(out));
        copy(inStream, outStream);
    }

    /**
     * Copy the contents of the given input File into a new byte array.
     *
     * @param in the file to copy from
     * @return the new byte array that has been copied to
     * @throws IOException in case of I/O errors
     */
    public static byte[] copyToByteArray(File in) throws IOException {
        Validate.notNull(in, "No input File specified");
        return copyToByteArray(new BufferedInputStream(new FileInputStream(in)));
    }


    //---------------------------------------------------------------------
    // Copy methods for java.io.InputStream / java.io.OutputStream
    //---------------------------------------------------------------------

    /**
     * Copy the contents of the given InputStream to the given OutputStream.
     * Closes both streams when done.
     *
     * @param in  the stream to copy from
     * @param out the stream to copy to
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static int copy(InputStream in, OutputStream out) throws IOException {
        Validate.notNull(in, "No InputStream specified");
        Validate.notNull(out, "No OutputStream specified");
        try {
            return StreamUtils.copy(in, out);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                logger.error("InputStream close error", ex);
            }
            try {
                out.close();
            } catch (IOException ex) {
                logger.error("OutputStream close error", ex);
            }
        }
    }

    /**
     * Copy the contents of the given byte array to the given OutputStream.
     * Closes the stream when done.
     *
     * @param in  the byte array to copy from
     * @param out the OutputStream to copy to
     * @throws IOException in case of I/O errors
     */
    public static void copy(byte[] in, OutputStream out) throws IOException {
        Validate.notNull(in, "No input byte array specified");
        Validate.notNull(out, "No OutputStream specified");
        try {
            out.write(in);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                logger.error("OutputStream close error", ex);
            }
        }
    }

    /**
     * Copy the contents of the given InputStream into a new byte array.
     * Closes the stream when done.
     *
     * @param in the stream to copy from
     * @return the new byte array that has been copied to
     * @throws IOException in case of I/O errors
     */
    public static byte[] copyToByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        copy(in, out);
        return out.toByteArray();
    }


    //---------------------------------------------------------------------
    // Copy methods for java.io.Reader / java.io.Writer
    //---------------------------------------------------------------------

    /**
     * Copy the contents of the given Reader to the given Writer.
     * Closes both when done.
     *
     * @param in  the Reader to copy from
     * @param out the Writer to copy to
     * @return the number of characters copied
     * @throws IOException in case of I/O errors
     */
    public static int copy(Reader in, Writer out) throws IOException {
        Validate.notNull(in, "No Reader specified");
        Validate.notNull(out, "No Writer specified");
        try {
            int byteCount = 0;
            char[] buffer = new char[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            return byteCount;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                logger.error("Reader close error", ex);
            }
            try {
                out.close();
            } catch (IOException ex) {
                logger.error("Writer close error", ex);
            }
        }
    }

    /**
     * Copy the contents of the given String to the given output Writer.
     * Closes the writer when done.
     *
     * @param in  the String to copy from
     * @param out the Writer to copy to
     * @throws IOException in case of I/O errors
     */
    public static void copy(String in, Writer out) throws IOException {
        Validate.notNull(in, "No input String specified");
        Validate.notNull(out, "No Writer specified");
        try {
            out.write(in);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                // NO-OP
            }
        }
    }

    /**
     * Copy the contents of the given Reader into a String.
     * Closes the reader when done.
     *
     * @param in the reader to copy from
     * @return the String that has been copied to
     * @throws IOException in case of I/O errors
     */
    public static String copyToString(Reader in) throws IOException {
        StringWriter out = new StringWriter();
        copy(in, out);
        return out.toString();
    }
}
