package com.pasc.lib.base.util;

import android.text.TextUtils;

import com.pasc.lib.log.PascLog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by yintangwen952 on 2018/9/2.
 */

public class DateUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_AND_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_NO_SS = "yyyy-MM-dd HH:mm";

    public static long getTime(String dateStr, String format) {
        if (!TextUtils.isEmpty(dateStr)) {
            DateFormat df = new SimpleDateFormat(format);
            try {
                Date date = df.parse(dateStr);
                return date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 格式化日期时间
     * 最后返回类似:8月21日 12:13   或  2013年8月21日 12:23
     *
     * @param dateStr 毫秒级整数
     */
    public static String timeFormat(long dateStr) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(DATE_AND_TIME_FORMAT);
        String allTime = sDateFormat.format(new Date(dateStr + 0));
        return timeFormat(allTime);
    }

    /**
     * 格式化日期
     * 最后返回类似:8月21日   或  2013年8月21日
     *
     * @param mills 毫秒级整数
     */
    public static String dateFormat(Long mills) {
        if (mills == null) {
            return "";
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat(DATE_FORMAT);
        String allTime = sDateFormat.format(new Date(mills + 0));
        return allTime;
    }

    /**
     * *
     * 格式化日期
     * 最后返回类似:8月21日   或  2013年8月21日
     *
     * @param format 时间格式
     */
    public static String dateFormat(long mills, String format) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
        String allTime = sDateFormat.format(new Date(mills + 0));
        return allTime;
    }

    public static String getYearAndMonth(String timeStr) {
        if (TextUtils.isEmpty(timeStr)) return " ";
        String time = timeStr.split(" ")[0];
        return time.substring(0, 7).replace("-", "年") + "月";
    }

    /**
     * 获取截取月份
     */
    public static String getMonth(String timeStr) {
        String year = timeStr.split(" ")[0];
        String month = year.substring(year.indexOf("-") + 1, 7) + "月";
        return month;
    }

    /**
     * 判断是否是本年
     */
    public static boolean isThisYear(String timeStr) {
        if (TextUtils.isEmpty(timeStr)) return false;
        //String dataStr = timeStr.split(" ")[0];
        Date date = strToDate(timeStr, DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        Date todayDate = calendar.getTime();
        if (todayDate.getYear() == date.getYear()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是本月
     */
    public static boolean isThisMonth(String timeStr) {
        if (TextUtils.isEmpty(timeStr)) return false;
        //String dataStr = timeStr.split(" ")[0];
        Date date = strToDate(timeStr, DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        Date todayDate = calendar.getTime();
        if (todayDate.getYear() == date.getYear()) {
            if (todayDate.getMonth() == date.getMonth()) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * 判断是否是上月
     */
    public static boolean isLastMonth(String timeStr) {
        if (TextUtils.isEmpty(timeStr)) return false;
        Date date = strToDate(timeStr, DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        Date todayDate = calendar.getTime();
        if (todayDate.getYear() == date.getYear()) {
            if (todayDate.getMonth() - 1 == date.getMonth()) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * 格式化日期时间
     */
    public static String timeFormat(String timeStr) {
        if (timeStr == null) {
            return "";
        } else {
            String dateStr = timeStr.split(" ")[0];
            String time = timeStr.split(" ")[1];
            time = time.substring(0, 5);
            String monthDay = dateStr.substring(dateStr.indexOf("-") + 1, dateStr.length());
            Calendar calendar = Calendar.getInstance();
            Date todayDate = calendar.getTime();
            Date date = strToDate(dateStr, DATE_FORMAT);
            //            calendar.setTime(date);
            if (date.getYear() == todayDate.getYear()) {
                if (date.getMonth() == todayDate.getMonth()
                        && date.getDate() == todayDate.getDate()) {
                    return time;
                } else if (date.getMonth() == todayDate.getMonth()
                        && date.getDate() + 1 == todayDate.getDate()) {
                    return "昨天" + time;
                } else {
                    return monthDay + " " + time;
                }
            } else {
                return timeStr;
            }
        }
    }

    /**
     * 格式化日期时间
     * 返回日期，需要时间
     */
    public static String DateFormat(String timeStr) {
        if (timeStr == null) {
            return "";
        } else {
            String dateStr = timeStr.split(" ")[0];
            String time = timeStr.split(" ")[1];
            time = time.substring(0, 5);
            String monthDay = dateStr.substring(dateStr.indexOf("-") + 1, dateStr.length());
            Calendar calendar = Calendar.getInstance();
            Date todayDate = calendar.getTime();
            Date date = strToDate(dateStr, DATE_FORMAT);
            //            calendar.setTime(date);
            if (date.getYear() == todayDate.getYear()) {
                if (date.getMonth() == todayDate.getMonth()
                        && date.getDate() == todayDate.getDate()) {
                    return "今天" + time;
                } else if (date.getMonth() == todayDate.getMonth()
                        && date.getDate() + 1 == todayDate.getDate()) {
                    return "昨天" + time;
                } else {
                    return monthDay;
                }
            } else {
                return timeStr;
            }
        }
    }

    /**
     * 日期字符串转换为Date
     */
    public static Date strToDate(String dateStr, String format) {
        Date date = null;

        if (!TextUtils.isEmpty(dateStr)) {
            DateFormat df = new SimpleDateFormat(format);
            try {
                date = df.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 获取上个月的当天
     */
    public static Long getTodayOfLastMonth() {
        long str;
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.DATE, Calendar.DATE); //set the date to be 1
        lastDate.add(Calendar.MONTH, -1);//reduce a month to be last month
        //		lastDate.add(Calendar.DATE,-1);//reduce one day to be the first day of last month

        str = lastDate.getTime().getTime();
        return str;
    }

    /**
     * 判断是否为今天
     */
    public static boolean isToday(Long mills) {
        if (mills == null || mills == 0 || mills == -1) {
            return false;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        PascLog.e("DateUtils", "asd " + fmt.format(new Date(mills)) + "@@@" + fmt.format(new Date()));
        return fmt.format(new Date(mills)).equals(fmt.format(new Date()));
    }

    public static String dateFormat(String timeStamp) {
        long l;
        try {
            l = Long.parseLong(timeStamp);
            return dateFormat(l);
        } catch (NumberFormatException e) {
            return timeStamp;
        }
    }

    public static String minutes2Date(long _ms) {
        Date date = new Date(_ms);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_NO_SS, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 获取区间日期
     */
    public static List<Date> getBetweenDates(String startTime, String endTime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;
        try {
            start = f.parse(startTime);
            end = f.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Date> result = new ArrayList<Date>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        //添加或减去指定的时间给定日历领域，基于日历的规则。例如，从日历当前的时间减去5天，您就可以通过
        tempStart.add(Calendar.DAY_OF_YEAR, 0);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        tempEnd.add(Calendar.DAY_OF_YEAR, 1);
        while (tempStart.before(tempEnd)) {
            result.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 日期转换为星期("yyyy-MM-dd")
     */
    public static String dateToWeek(Date datetime) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        cal.setTime(datetime);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0) w = 0;
        return weekDays[w];
    }

    /**
     * 日期转换为星期("MM/dd")
     */
    public static String dateToMonthDay(Date datetime) {
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        cal.setTime(datetime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(cal.getTime());
        return dateString.substring(dateString.indexOf("-") + 1).replace("-", "/");
    }

    /**
     * 日期转换为星期("MM月dd日")
     */
    public static String dateToEvent(Date datetime) {
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        cal.setTime(datetime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(cal.getTime());
        String month =
                dateString.substring(dateString.indexOf("-") + 1, dateString.lastIndexOf("-"));
        String day = dateString.substring(dateString.lastIndexOf("-") + 1);
        return Integer.parseInt(month) + "月" + Integer.parseInt(day) + "日";
    }

    /**
     * 今天之后天数的日期
     */
    public static String todayAddDate(int days) {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, days);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * 转换成默认的日期
     */
    public static String getDefaultFormat(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat(DATE_AND_TIME_FORMAT);
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 判断是昨天还是 今天
     * <p>
     * 2018-09-12 09：21：22
     */
    public static String parseTodayOrLastDay(String timeStr) {
        String result = "";
        String[] split = timeStr.split(" ");
        if (TextUtils.isEmpty(timeStr)) return result;

        try {
            Date resultDate = new SimpleDateFormat(DATE_FORMAT_NO_SS).parse(timeStr);
            result = timeStr.substring(0, timeStr.length() - 3);
            if (android.text.format.DateUtils.isToday(resultDate.getTime())) {//今天
                result = "今天 " + split[1].substring(0, split[1].length() - 3);
            } else if (android.text.format.DateUtils.isToday(resultDate.getTime() + 24 * 60 * 60 * 1000)) {
                result = "昨天 " + split[1].substring(0, split[1].length() - 3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public static String dateAndtimeFormat(long dateStr) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(DATE_AND_TIME_FORMAT);
        String allTime = sDateFormat.format(new Date(dateStr + 0));
        return allTime;
    }
}
