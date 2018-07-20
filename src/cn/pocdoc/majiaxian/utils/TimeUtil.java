package cn.pocdoc.majiaxian.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pengwei on 14/12/17.
 */
public class TimeUtil {

    public static long getCurrentTimestamp(){
        return System.currentTimeMillis();
    }

    /**
     * 2012-10-10
     * @return
     */
    public static String getDay(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 2012-10-10 12:12:12
     * @return
     */
    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(System.currentTimeMillis()));
    }




    public static int BetweenDays(long beginDate, long endDate){
        long timeGap = (endDate - beginDate)/1000;
        int day=24*60*60;
        return (int)(timeGap/day);
    }

    /**
     * 将时间字符串转化为时间戳
     * @param time
     * @return
     */
    public static long getTimeStamp(String time){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d.getTime();
    }

    /**
     * 两个时间字符串之间相隔的秒数
     * @param start
     * @param end
     * @return
     */
    public static long BetweenTimeString(String start, String end){
        long startTimeStamp = getTimeStamp(start);
        long endTimeStamp = getTimeStamp(end);
        return endTimeStamp - startTimeStamp;
    }

}
