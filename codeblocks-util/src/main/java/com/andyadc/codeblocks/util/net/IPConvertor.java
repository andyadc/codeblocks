package com.andyadc.codeblocks.util.net;

/**
 * @author andaicheng
 * @version 2017/1/8
 */
public final class IPConvertor {

    public static void main(String[] args) {
        System.out.printf("%s %n", toNum("10.157.165.200"));
        System.out.printf("%s %n", toIP(178103752));
    }

    /**
     * IP地址转为数字
     *
     * @param ip IP 地址
     * @return 数字
     */
    public static int toNum(String ip) {
        if (ip == null || ip.length() == 0) {
            return -1;
        }
        char[] chs = ip.toCharArray();
        int t = 0, n = 0;
        for (int i = 0, k = 0; i < chs.length; i++) {
            if (chs[i] == '.') {
                if (k++ > 2) {
                    break;
                }
                t = (t << 8) | (n & 0xff);
                n = 0;
                continue;
            }
            if (chs[i] >= '0' && chs[i] <= '9') {
                n = n * 10 + (chs[i] - '0');
                continue;
            }
            break;
        }
        t = (t << 8) | (n & 0xff);
        return (t & -1);
    }

    /**
     * 数字转为IP地址
     *
     * @param num 数字
     * @return IP 地址
     */
    public static String toIP(int num) {
        StringBuilder builder = new StringBuilder(15);
        builder.append((num & 0xff000000) >>> 24).append('.')
                .append((num & 0x00ff0000) >>> 16).append('.')
                .append((num & 0x0000ff00) >>> 8).append('.')
                .append((num & 0x000000ff) >>> 0);
        return builder.toString();
    }
}
