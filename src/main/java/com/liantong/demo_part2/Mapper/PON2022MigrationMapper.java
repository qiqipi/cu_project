package com.liantong.demo_part2.Mapper;

import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author qiqipi
 * @create 2022/7/10 6:07
 */
@Repository
public interface PON2022MigrationMapper {

    @MapKey("id")
    List<Map<String,Object>> getOLTChosenTable();

    void dropMergeTable();

    void createMergeTable();

    int initMergeTable();

    void createOLTChosenTable();

    int insertOLTChosenTable();


    @MapKey("id")
    List<Map<String,Object>> getRegion();

    void createPlanTable();

    void insertPlanTable1(String time1 ,String  time2);

    @MapKey("id")
    List<Map<String,Object>> getPlanTable(String OLT_name);

    @MapKey("id")
    List<Double> getChannel1InPeek(String OLTName,String PONBoard,String PONPort);

    List<Double> getChannel2InPeek(String OLTName,String PONBoard,String PONPort);

    void updatePlanTable(String OLTName, String  value, String fieldName, String PONBoard, String PONPort);

    void createStandardOLTChosenTable();

    void insertStandardOLTChosenTable(String oltConcat, String oltName, String ponBoardNumber, String ponPortNumber,String channel1InPeekMax, String channel1InAvgMax, String channel2InPeekMax, String channel2InAvgMax, String  channel1InPeekMaxTime, String channel2InPeekMaxTime);

    void updateStandardOLTChosenTable(String value1,String value2,String value3,String value4);

    @MapKey("id")
    List<Map<String ,Object >> getStandardOLTChosenTable1();
    @MapKey("id")
    List<Map<String ,Object >> getStandardOLTChosenTable2();

    Map<String ,Object> getMigrationTable(String id);

    void createCoList();
}
