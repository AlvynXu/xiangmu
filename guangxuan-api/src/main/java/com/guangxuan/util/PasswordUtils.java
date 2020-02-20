package com.guangxuan.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author deofly
 * @since 2019-03-29
 */
public class PasswordUtils {

    public static String createPassword(String password, String salt) {
        return DigestUtils.sha256Hex(password + salt);
    }
}
