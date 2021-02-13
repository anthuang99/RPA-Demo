package org.webdriver.patatiumwebui.web.util;

import org.webdriver.patatiumwebui.exception.RPAException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static String getDateShow(String yyyyMMddHHmmss, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat resultFormat = new SimpleDateFormat(format);
        try {
            Date date = simpleDateFormat.parse(yyyyMMddHHmmss);
            return resultFormat.format(date);
        } catch (ParseException e) {
            throw new RPAException(e);
        }
    }

    public static String getDateShow(String yyyyMMddHHmmss, String inputFormat, String outputFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(inputFormat);
        SimpleDateFormat resultFormat = new SimpleDateFormat(outputFormat);
        try {
            Date date = simpleDateFormat.parse(yyyyMMddHHmmss);
            return resultFormat.format(date);
        } catch (ParseException e) {
            throw new RPAException(e);
        }
    }

    public static String getDateShow(Date date, String format) {
        SimpleDateFormat resultFormat = new SimpleDateFormat(format);
        return resultFormat.format(date);
    }

    public static Date getDateFromString(String yyyyMMddHHmmss, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            Date date = simpleDateFormat.parse(yyyyMMddHHmmss);
            return date;
        } catch (ParseException e) {
            throw new RPAException(e);
        }
    }

    /**
     * 取得前一个月的第一天
     * @param format
     * @return
     */
    public static String getPreMonthFirstDate(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 取得下一个月的第一天
     * @param format
     * @return
     */
    public static String getNextMonthFirstDate(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(calendar.getTime());
    }
}
