package com.fliaping.wifi.auth.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

}
