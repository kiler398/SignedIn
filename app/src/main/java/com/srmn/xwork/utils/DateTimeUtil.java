package com.srmn.xwork.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kiler on 2016/1/15.
 */
public class DateTimeUtil {

    public static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat MonthFormat = new SimpleDateFormat("yyyy-MM");
    public static final SimpleDateFormat MiniteTimeFormat = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat WWeekFormat = new SimpleDateFormat("EEEE");

    public static String FormatDate(Date date) {
        return DateFormat.format(date);
    }

    public static String FormatDateTime(Date date) {
        return DateTimeFormat.format(date);
    }

    public static String FormatMonth(Date date) {
        return MonthFormat.format(date);
    }


    public static String FormatMiniteTime(Date date) {
        return MiniteTimeFormat.format(date);
    }

    public static String FormatWeek(Date dateTime) {
        return WWeekFormat.format(dateTime);
    }


    public static Date BuildDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        return cal.getTime();
    }
}
