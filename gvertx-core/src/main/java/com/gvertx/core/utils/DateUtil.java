package com.gvertx.core.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Locale;

/**
 * Created by wangziqing on 17/2/16.
 */
public class DateUtil {
    private static final DateTimeFormatter RFC1123_DATE_FORMAT;

    public DateUtil() {
    }

    public static String formatForHttpHeader(Date date) {
        return RFC1123_DATE_FORMAT.print(new DateTime(date));
    }

    public static String formatForHttpHeader(Long unixTime) {
        return RFC1123_DATE_FORMAT.print(new DateTime(unixTime));
    }

    public static Date parseHttpDateFormat(String httpDateFormat) throws IllegalArgumentException {
        return parseHttpDateFormatToDateTime(httpDateFormat).toDate();
    }

    public static DateTime parseHttpDateFormatToDateTime(String httpDateFormat) throws IllegalArgumentException {
        return RFC1123_DATE_FORMAT.parseDateTime(httpDateFormat);
    }

    static {
        RFC1123_DATE_FORMAT = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss \'GMT\'").withLocale(Locale.US).withZone(DateTimeZone.UTC);
    }
}
