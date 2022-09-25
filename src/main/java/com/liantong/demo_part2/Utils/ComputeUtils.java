package com.liantong.demo_part2.Utils;

import org.apache.ibatis.reflection.wrapper.BaseWrapper;

import java.text.DecimalFormat;

/**
 * @author qiqipi
 * @create 2022/9/25 14:03
 */
public class ComputeUtils {

    public static String computeTrafficUsage1(String traffic,String band){
        int bandwidth = 1;
        if(band.equals("EPON") || band.equals("10G EPON")){
            bandwidth = 1000;
        }else if(band.equals("GPON") || band.equals("Combo PON(XG PON)") || band.equals("Combo PON(XGS PON)")){
            bandwidth = 2500;
        }else if(band.equals("XG PON") || band.equals("XGS PON")){
            bandwidth = 10000;
        }
        String format = new DecimalFormat("######0.00").format(Double.valueOf(traffic) / bandwidth * 100);
        return format + "%";
    }

    public static String computeTrafficUsage2(String traffic,String band){
        int bandwidth = 1;
        if(band.equals("10G EPON") || band.equals("Combo PON(XG PON)") || band.equals("Combo PON(XGS PON)")){
            bandwidth = 10000;
        }
        String format = new DecimalFormat("######0.00").format(Double.valueOf(traffic) / bandwidth * 100);
        return format + "%";
    }
}
