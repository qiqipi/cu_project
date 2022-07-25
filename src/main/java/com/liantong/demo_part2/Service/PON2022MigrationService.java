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

    boolean createPlanTable() throws Exception;

    List<Map<String,Object>> getPlanTable(String[] OLT_name);
}
