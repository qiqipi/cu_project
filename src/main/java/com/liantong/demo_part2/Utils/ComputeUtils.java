package com.liantong.demo_part2.Utils;

import org.apache.ibatis.reflection.wrapper.BaseWrapper;

import java.text.DecimalFormat;
import java.util.List;

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

    public static String  getPredict(List<Double> list) {
        double [] data = new double[list.size()];
        for(int i = 0;i < list.size();i++){
            data[i] = list.get(i);
        }
        double[] newData = data.clone();
        int count = newData.length;
        double s = 0.0;
        int i = 0;
        while (i<=30){
            s = 0.0;
            for (int j = 0; j < count; j++)
            {
                s += (j + 1) * newData[j];
            }
            s /= (count * (count + 1) / 2);
            for (int j = 0; j < count - 1; j++)
            {
                newData[j] = newData[j + 1];
            }
            newData[count - 1] = s;

            double nd =newData[newData.length-1];
            double cs = 0;
            int js = 0;
            for(int h=0; h<newData.length-1; h++){
                cs = newData[h];
                if(nd == cs){
                    js++;
                }
            }
            if(js == newData.length-1){
                break;
            }
            i++;
        }
        String  str = String.format("%.2f",s);
        return str;
    }
}
