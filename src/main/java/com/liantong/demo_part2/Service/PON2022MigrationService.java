package com.liantong.demo_part2.Service;

import java.util.List;
import java.util.Map;

/**
 * @author qiqipi
 * @create 2022/7/10 6:10
 */
public interface PON2022MigrationService {

    List<Map<String, Object>> getAaaTable();


    boolean createMergeTable() throws Exception;

}
