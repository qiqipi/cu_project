package com.liantong.demo_part2.Mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DataBaseMapper {

    void backUp(String time);

    void delete(String time);

    void insertData(String tableName, String[] column, String[] data);

    void changeDate(String tableName);

    void deleteTable(String tableName);

    void creatBase1(String tableName);

    void creatBase2(String tableName);

    void creatBase3(String tableName);

    void creatBase4(String tableName);

    void creatBase5(String tableName);

    void creatMiddle1(String currentDate);

    void creatMiddle2(String currentDate);

    int countMiddle1(String currentDate);

    void insertMiddle1(String currentDate);

    void insertMiddle2(String currentDate);


    void updateMiddle1(@Param("currentDate") String currentDate, @Param("count") int count);

    void updateMiddle2(String currentDate);


    void creatFactor(String currentDate);

    void insertFactor(String currentDate);

    void creatParms(String currentDate);

    void creatResult(String currentDate);

    void insertParms(String currentDate);

    void updateParms(String currentDate);

    void addNotes(String date, String tableName,String time, String operator, String notes, String status,String nums);

    List<Map<String,Object>> getLog();

    List<Map<String,Object>> getMassage(String suffix);

    List<Map<String,Object>> returnMeans(String timeFlag, String department,String olt);

    List<Map<String,Object>> homeMassage(String currentDate);

    int judgeExist(String name,String date);


    void createUserMassage(String currentDate);

    void insertUserMassage(String currentDate);

    void updateUserMassage(String currentDate);

    void createOLTMassage(String currentDate);

    void insertOLTMassage(String currentDate);

    void updateOLTMassage(String currentDate);

    void createPONMassage(String currentDate);

    void insertPONMassage(String currentDate);

    void updatePONMassage(String currentDate);

    void updateOLTUser(String currentDate);


}
