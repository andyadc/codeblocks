package com.andyadc.codeblocks.util.number;

import com.google.common.math.IntMath;
import com.google.common.math.LongMath;

import java.math.RoundingMode;

/**
 * 数学相关工具类.包括
 * <p>
 * 1. 2的倍数的计算
 * 2. 其他函数如最大公约数, 乘方，开方，安全的取模，可控制取整方向的相除等。
 *
 * @author andaicheng
 * @version 2017/4/23
 */
public final class MathUtils {

    private MathUtils() {
    }

    /////// 2 的倍数的计算////

    /**
     * 往上找出最接近的2的倍数，比如15返回16， 17返回32.
     *
     * @param value 必须为正数，否则抛出异常.
     */
    public static int nextPowerOfTwo(final int value) {
        return IntMath.ceilingPowerOfTwo(value);
    }

    /**
     * 往上找出最接近的2的倍数，比如15返回16， 17返回32.
     *
     * @param value 必须为正数，否则抛出异常.
     */
    public static long nextPowerOfTwo(final long value) {
        return LongMath.ceilingPowerOfTwo(value);
    }

    /**
     * 往下找出最接近2的倍数，比如15返回8， 17返回16.
     *
     * @param value 必须为正数，否则抛出异常.
     */
    public static int previousPowerOfTwo(final int value) {
        return IntMath.floorPowerOfTwo(value);
    }

    /**
     * 往下找出最接近2的倍数，比如15返回8， 17返回16.
     *
     * @param value 必须为正数，否则抛出异常.
     */
    public static long previousPowerOfTwo(final long value) {
        return LongMath.floorPowerOfTwo(value);
    }

    /**
     * 是否2的倍数
     *
     * @param value 不是正数时总是返回false
     */
    public static boolean isPowerOfTwo(int value) {
        return IntMath.isPowerOfTwo(value);
    }

    /**
     * 是否2的倍数
     *
     * @param value <=0 时总是返回false
     */
    public static boolean isPowerOfTwo(final long value) {
        return LongMath.isPowerOfTwo(value);
    }

    /**
     * 当模为2的倍数时，用比取模块更快的方式计算.
     *
     * @param value 可以为负数，比如 －1 mod 16 = 15
     */
    public static int modByPowerOfTwo(final int value, final int mod) {
        return value & mod - 1;
    }

    ////////////// 其他函数//////////

    /**
     * 两个数的最大公约数，必须均为非负数.
     * <p>
     * 是公约数，别想太多
     */
    public static int gcd(final int a, final int b) {
        return IntMath.gcd(a, b);
    }

    /**
     * 两个数的最大公约数，必须均为非负数
     */
    public static long gcd(final long a, final long b) {
        return LongMath.gcd(a, b);
    }

    /**
     * 保证结果为正数的取模.
     * <p>
     * 如果(v = x/m) <0，v+=m.
     */
    public static int mod(final int x, final int m) {
        return IntMath.mod(x, m);
    }

    /**
     * 保证结果为正数的取模.
     * <p>
     * 如果(v = x/m) <0，v+=m.
     */
    public static long mod(final long x, final long m) {
        return LongMath.mod(x, m);
    }

    /**
     * 保证结果为正数的取模
     */
    public static long mod(final long x, final int m) {
        return LongMath.mod(x, m);
    }

    /**
     * 能控制rounding方向的相除.
     * <p>
     * jdk的'/'运算符，直接向下取整
     */
    public static int divide(final int p, final int q, RoundingMode mode) {
        return IntMath.divide(p, q, mode);
    }

    /**
     * 能控制rounding方向的相除
     * <p>
     * jdk的'/'运算符，直接向下取整
     */
    public static long divide(final long p, final long q, RoundingMode mode) {
        return LongMath.divide(p, q, mode);
    }

    /**
     * 平方
     *
     * @param k 平方次数,不能为负数, k=0时返回1.
     */
    public static int pow(final int b, final int k) {
        return IntMath.pow(b, k);
    }

    /**
     * 平方
     *
     * @param k 平方次数,不能为负数, k=0时返回1.
     */
    public static long pow(final long b, final int k) {
        return LongMath.pow(b, k);
    }

    /**
     * 开方
     */
    public static int sqrt(final int x, RoundingMode mode) {
        return IntMath.sqrt(x, mode);
    }

    /**
     * 开方
     */
    public static long sqrt(final long x, RoundingMode mode) {
        return LongMath.sqrt(x, mode);
    }
}
