package com.wishare.finance.infrastructure.support.shortcode;

/**
 * 短链工具类
 *
 * @author dxclay
 * @version 1.0
 * @since 2024/4/23
 */
public class ShortCodeUtils {

    public static final int SPEED_62 = 62;

    private static final char[] BASE_62_ARRAY = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };


    public static String generateShortCode(long number, int seed){
        if (number < 0) {
            number = ((long) 2 * 0x7fffffff) + number + 2;
        }
        char[] buf = new char[32];
        int charPos = 32;
        while ((number / seed) > 0) {
            buf[--charPos] = BASE_62_ARRAY[(int) (number % seed)];
            number /= seed;
        }
        buf[--charPos] = BASE_62_ARRAY[(int) (number % seed)];
        return new String(buf, charPos + 5, (32 - charPos - 5));
    }

}
