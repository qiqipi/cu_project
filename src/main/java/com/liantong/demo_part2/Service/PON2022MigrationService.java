package com.liantong.demo_part2.Service;

import java.util.List;
import java.util.Map;

/**
 * @author qiqipi
 * @create 2022/7/10 6:10
 */
public interface PON2022MigrationService {

    List<Map<String, Object>> getOLTChosenTable();


    boolean createMergeTable() throws Exception;

    boolean createOLTChosenTable() throws Exception;



    List<Map<String,Object>> getRegion();

    boolean createPlanTable(String[] time) throws Exception;

    List<Map<String,Object>> getPlanTable(String[] OLT_name);

    double getPredict1(String OLTName, String PONBoard, String PONPort);

    List<Map<String,Object >> getRank_table(String values[]);
}
