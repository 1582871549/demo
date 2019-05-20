package com.dudu.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;  
import java.util.Date;

public class DateUtil {

    /**
     * 功能描述：获取当前日期字符串
     *
     * @return Date 日期
     */
    public static String getDateStr() {
        return formatDate(new Date());
    }

    /**
     * 功能描述：获取当前日期时间字符串
     *
     * @return Date 日期
     */
    public static String getDateTimeStr() {
        return formatDateTime(new Date());
    }

    /**
     * 功能描述：格式化日期 
     *
     * @param dateStr
     *            String 字符型日期 
     * @param format
     *            String 格式 
     * @return Date 日期 
     */
    public static Date parseDate(String dateStr, String format) {
        Date parse = null;
        try {
            parse = new SimpleDateFormat(format).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    /** 
     * 功能描述：格式化字符串日期
     *  
     * @param dateStr 
     *            String 字符型日期：YYYY-MM-DD 格式 
     * @return Date 
     */  
    public static Date parseDate(String dateStr) {  
        return parseDate(dateStr, "yyyy-MM-dd");
    }

    /**
     * 功能描述：格式化字符串日期
     *
     * @param dateStr
     *            String 字符型日期：yyyy-MM-dd hh:mm:ss 格式
     * @return Date
     */
    public static Date parseDateTime(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd hh:mm:ss");
    }

    /** 
     * 功能描述：格式化输出日期 
     *  
     * @param date 
     *            Date 日期 
     * @param format 
     *            String 格式 
     * @return 返回字符型日期 
     */
    public static String format(Date date, String format) {
        String result = "";
        if (date != null) {
            try {
                result = new SimpleDateFormat(format).format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /** 
     * 功能描述： 
     *  
     * @param date 
     *            Date 日期 
     * @return 
     */  
    public static String formatDate(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    /**
     * 功能描述：返回字符型日期时间
     *
     * @param date
     *            Date 日期
     * @return 返回字符型日期时间 yyyy/MM/dd HH:mm:ss 格式
     */
    public static String formatDateTime(Date date) {
        return format(date, "yyyy-MM-dd hh:mm:ss");
    }

    /** 
     * 功能描述：返回年份 
     *  
     * @param date 
     *            Date 日期 
     * @return 返回年份 
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }  
  
    /** 
     * 功能描述：返回月份 
     *  
     * @param date 
     *            Date 日期 
     * @return 返回月份 
     */  
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }  
  
    /** 
     * 功能描述：返回日份 
     *  
     * @param date 
     *            Date 日期 
     * @return 返回日份 
     */  
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }  
  
    /** 
     * 功能描述：返回小时 
     *  
     * @param date 
     *            日期 
     * @return 返回小时 
     */  
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }  
  
    /** 
     * 功能描述：返回分钟 
     *  
     * @param date 
     *            日期 
     * @return 返回分钟 
     */  
    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }  
  
    /** 
     * 返回秒钟 
     *  
     * @param date 
     *            Date 日期 
     * @return 返回秒钟 
     */  
    public static int getSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }  
  
    /** 
     * 功能描述：返回毫秒 
     *  
     * @param date 
     *            日期 
     * @return 返回毫秒 
     */  
    public static long getMillis(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }  
  
    /**
     * 功能描述：返回字符型时间 
     *  
     * @param date 
     *            Date 日期 
     * @return 返回字符型时间 HH:mm:ss 格式 
     */  
    public static String getTime(Date date) {  
        return format(date, "hh:mm:ss");
    }
  
    /** 
     * 功能描述：日期相加 
     *  
     * @param date 
     *            Date 日期 
     * @param day 
     *            int 天数 
     * @return 返回相加后的日期 
     */  
    public static Date addDate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }  
  
    /** 
     * 功能描述：日期相减 
     *  
     * @param date 
     *            Date 日期 
     * @param date1 
     *            Date 日期 
     * @return 返回相减后的日期 
     */  
    public static int diffDate(Date date, Date date1) {
        return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
    }  
  
    /** 
     * 功能描述：取得指定月份的第一天 
     *  
     * @param strdate 
     *            String 字符型日期 
     * @return String yyyy-MM-dd 格式 
     */  
    public static String getMonthBegin(String strdate) {
        Date date = parseDate(strdate);
        return format(date, "yyyy-MM") + "-01";
    }  
  
    /** 
     * 功能描述：取得指定月份的最后一天 
     *  
     * @param strdate 
     *            String 字符型日期 
     * @return String 日期字符串 yyyy-MM-dd格式
     */  
    public static String getMonthEnd(String strdate) {
        Date date = parseDate(getMonthBegin(strdate));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return formatDate(calendar.getTime());
    }  
  
    public static void main(String[] args) {

        // 日期 字符串
        String dateStr = "2019-04-01";
        // 日期时间 字符串
        String dateTimeStr = "2019-04-01 10:49:33";


        // 格式化 日期 字符串
        Date date = DateUtil.parseDate(dateStr);
        // 格式化 日期时间 字符串
        Date dateTime = DateUtil.parseDateTime(dateTimeStr);

        // 格式化 日期
        String resultDateStr = DateUtil.formatDate(date);
        // 格式化 日期时间
        String resultDateTimeStr = DateUtil.formatDateTime(dateTime);


        System.out.println("date : " + date.toString() + "      dateTime : " + dateTime.toString());
        System.out.println("resultDateStr : " + resultDateStr + "      resultDateTimeStr : " + resultDateTimeStr);

        System.out.println("当前日期 : " + DateUtil.getDateStr() + "      当前日期时间 : " + DateUtil.getDateTimeStr());
    }
}
