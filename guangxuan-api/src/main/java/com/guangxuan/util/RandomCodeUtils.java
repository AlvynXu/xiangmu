package com.guangxuan.util;

import java.util.Random;

/**
 * @author zhuolin
 */
public class RandomCodeUtils {

    public static String generateRandomCode(Integer length) {
        String string = new String("0123456789");
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(random.nextInt(string.length()));
        }
        return stringBuffer.toString();
    }
}
