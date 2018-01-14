package com.whut.mine.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    private static final SimpleDateFormat YYYYMMDDHHMMSS_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat YYYYMMDDHHMMSS_FORMAT_NODIV = new SimpleDateFormat(
            "yyyyMMddHHmmss", Locale.getDefault());

    //年月日时分秒
    public static String getTodaynyrsfm() {
        return YYYYMMDDHHMMSS_FORMAT.format(new Date());
    }

    public static String getTomorrownyrsfm() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天
        return YYYYMMDDHHMMSS_FORMAT.format(c.getTime());
    }

    static String getTodaynyrsfmNoDiv() {
        return YYYYMMDDHHMMSS_FORMAT_NODIV.format(new Date());
    }

}
