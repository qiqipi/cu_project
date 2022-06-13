package com.liantong.demo_part2.Entity;

public class TimeStamp {

    public static String timeFlag = "";

    public static void setTimeFlag(String modifyTime) {
        if (modifyTime != null) {
            TimeStamp.timeFlag = modifyTime;
        }
    }
}
