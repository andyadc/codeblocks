package com.andyadc.codeblocks.kit;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author andy.an
 * @since 2019/3/18
 */
public final class RandomUtil {

    private static final String LETTERS = "0123456789qazwsxedcrfvtgbyhnujmikolp";
    private static final int LETTERS_LEN = LETTERS.length();

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static void main(String[] args) {
        System.out.println(genRandomNum(6));
        System.out.println(genRandomStr(6));
    }

    public static String genRandomStr(int size) {
        StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            builder.append(LETTERS.charAt(RANDOM.nextInt(LETTERS_LEN)));
        }
        return builder.toString();
    }

    public static String genRandomNum(int size) {
        StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            builder.append(RANDOM.nextInt(10));
        }
        return builder.toString();
    }
}
