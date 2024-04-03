package me.huanmeng.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("HH:mm");
    private static final Date DATE = new Date(System.currentTimeMillis());
    public static final String TIME = DATEFORMAT.format(DATE);
}
