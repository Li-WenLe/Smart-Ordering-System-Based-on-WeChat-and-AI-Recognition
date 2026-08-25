package com.wxprogrem.utils;

import java.text.SimpleDateFormat;

//根据日期+随机数生成唯一订单ID
public class SimpleDateFormatUtils {
    public static String getOrderId(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String uniqueID=sdf.format(System.currentTimeMillis());
        int random=Math.abs((int)(Math.random()*10000));
        return uniqueID+random;
    }
}
