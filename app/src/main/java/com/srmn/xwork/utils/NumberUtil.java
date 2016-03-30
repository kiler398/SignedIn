package com.srmn.xwork.utils;

import java.math.BigDecimal;

/**
 * Created by kiler on 2015/8/16.
 */
public class NumberUtil {

    public static double roundNumber(double roundNumber,int keepdigtal)
    {
        BigDecimal b   =   new BigDecimal(roundNumber);
        return  b.setScale(keepdigtal,   BigDecimal.ROUND_HALF_UP).doubleValue();
    }


}
