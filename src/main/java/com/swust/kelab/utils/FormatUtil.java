package com.swust.kelab.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * 格式化 工具类
 * 
 * @author Wu
 *
 */
public class FormatUtil {
    /**
     * 默认时间格式
     */
    private static final String DEFAULT_TIME_FORMAT="yyyy-MM-dd HH:mm:ss";
    
    /**
     * 默认时间格式化对象
     */
    private static FastDateFormat formatter= FastDateFormat
            .getInstance(DEFAULT_TIME_FORMAT, 
            TimeZone.getDefault(),Locale.getDefault());;
    
    /**
     * 格式化给定时间,采用默认的时间格式
     * 
     * @param sourceDate 需要格式化的时间
     * @return           格式化后的时间字符串
     */
    public static String formatDate(Date sourceDate){
    	if(sourceDate != null){
    		return formatter.format(sourceDate);
    	}
    	return null;
    }
    
    /**
     * 格式化给定时间,采用指定的时间格式
     * 
     * @param sourceDate 需要格式化的时间
     * @return           格式化后的时间字符串
     */
    public static String formatDate(Date sourceDate,String timeFormat){
        FastDateFormat newformatter=null;
        if(DEFAULT_TIME_FORMAT.equals(timeFormat)){
            newformatter=formatter;
        }else{
            newformatter= FastDateFormat.getInstance(timeFormat,
                TimeZone.getDefault(),Locale.getDefault());
        }
        return newformatter.format(sourceDate);
    }
    /**
     * 时间字符串转换为日期(
     * 只精确到分钟
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String dateStr) throws ParseException{
    	   SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	   return fd.parse(dateStr);
    }
    public static void main(String[] args) {
       try {
		System.out.println(stringToDate("2014-01-12 11:19"));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
}
