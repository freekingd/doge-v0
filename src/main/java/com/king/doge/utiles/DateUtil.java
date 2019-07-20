package com.king.doge.utiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhuru on 2018/12/17.
 */
public class DateUtil {

    /**
     * 格式化date
     * @param date
     * @return
     */
    public static String getDateString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    /**
     * @param dateString 字符串格式的时间
     * @param hour 需要加减的时区
     * @return 格式为“yyyy-MM-dd HH:mm:ss”的Date
     * @throws ParseException
     */
    public static Date toDate(String dateString, int hour) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(sdf.parse(dateString).getTime() + hour*60*60*1000);
        return date;
    }

}
