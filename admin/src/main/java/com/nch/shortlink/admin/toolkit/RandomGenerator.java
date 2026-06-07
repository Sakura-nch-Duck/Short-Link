package com.nch.shortlink.admin.toolkit;


import java.security.SecureRandom;
import java.util.Random;

/**
 * 分组ID随机生成器
 */
public class RandomGenerator {

    private static final String CHAR_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final Random RANDOM = new SecureRandom();

    /**
     * 生成指定长度的随机字符串（数字 + 大小写字母）
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String generateRandomString(int length) {
        if (length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHAR_SET.length());
            sb.append(CHAR_SET.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 生成6位随机字符串（数字 + 大小写字母）
     * @return 6位随机字符串
     */
    public static String generate6DigitRandomString() {
        return generateRandomString(6);
    }
}
