package com.srmn.xwork.utils;

/**
 * Created by kiler on 2015/10/7.
 */
public class StringUtil {

    public static boolean isNullOrEmpty(String value) {
        if (value == null)
            return true;
        if (value.equals(""))
            return true;
        if (value.length() == 0)
            return true;
        return false;
    }
}
