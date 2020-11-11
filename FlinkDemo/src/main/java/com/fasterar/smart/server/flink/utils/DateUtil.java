package com.fasterar.smart.server.flink.utils;
import lombok.extern.slf4j.Slf4j;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mango
 */
@Slf4j
public class DateUtil {

    /**
     * 转换年月日时 格式
     *
     * @param date
     * @return string
     */
    public static String ofDateHhString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
        return format.format(date);
    }

    /**
     * 转换年月日时 格式
     *
     * @param date
     * @return string
     */
    public static Date ofHhDate(Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
        Date date1 = format.parse(format.format(date));
        return date1;
    }

    /**
     * 转换年月日时 格式
     *
     * @param date
     * @return string
     */
    public static String ofDateDdString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }

    /**
     * 转换年月日时 格式
     *
     * @param date
     * @return string
     */
    public static Date ofDdDate(Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date1 = format.parse(format.format(date));
        return date1;
    }

    public static String ofDateString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 获取两个时间相差秒数
     */
    public static Long getDateT(String startTime, String endTime) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long days = null;
        try {
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);
            long diff = endDate.getTime() - startDate.getTime();
            days = diff / (1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }
}
