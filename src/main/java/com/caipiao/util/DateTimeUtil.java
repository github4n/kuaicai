package com.caipiao.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间格式化工具
 *
 * @author zhangjj
 * @create 2018-08-24 11:02
 **/
public class DateTimeUtil {

    public final static String DATATIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String currentDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat(DATATIME_FORMAT);
        return sdf.format(new Date());
    }
}
