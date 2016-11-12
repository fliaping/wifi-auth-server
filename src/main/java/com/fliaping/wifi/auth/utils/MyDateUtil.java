package com.fliaping.wifi.auth.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Payne on 8/11/16.
 */
public class MyDateUtil {

    public static final String datePattern = "yyyy-MM-dd HH:mm:ss" ;

    private static SimpleDateFormat sdfChina= new SimpleDateFormat(datePattern);

    public static long date2Long(Date date){
        return date.getTime();
    }

    public static String date2String(Date date){

        return sdfChina.format(date);
    }

    public static Date long2Date(long date){
        return new Date(date);
    }

    public static String long2String(long date){
        return sdfChina.format(long2Date(date));
    }

    public static Date string2Data(String chinaFormatDate){
        try {
            return sdfChina.parse(chinaFormatDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static long string2Long(String chinaFormatDate){
        return string2Data(chinaFormatDate).getTime();
    }

    public static Date[] getSpanTime(String type){
        Calendar calendar = Calendar.getInstance();
        Date endtime = calendar.getTime();

        calendar.set(Calendar.SECOND,0);
        switch (type){
            case "year": calendar.set(Calendar.MONTH,0);
            case "month" : calendar.set(Calendar.DAY_OF_MONTH,0);
            case "day" : calendar.set(Calendar.HOUR_OF_DAY,0);
            case "hour": calendar.set(Calendar.MINUTE,0);
        }

        Date starttime = calendar.getTime();
        return new Date[]{starttime,endtime};
    }
}
