package com.andyadc.codeblocks.kit;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author andy.an
 * @since 2019/3/18
 */
public final class RandomUtil {

    private static String LETTERS = "0123456789qazwsxedcrfvtgbyhnujmikolp";
    private static int LETTERS_LEN = LETTERS.length();

    private static SecureRandom random;

    static {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(genRandomNum(6));
        System.out.println(genRandomStr(6));
    }

    public static String genRandomStr(int size) {
        StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            builder.append(LETTERS.charAt(random.nextInt(LETTERS_LEN)));
        }
        return builder.toString();
    }

    public static String genRandomNum(int size) {
        StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
