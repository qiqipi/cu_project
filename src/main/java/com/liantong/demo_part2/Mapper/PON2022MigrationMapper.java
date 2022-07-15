package com.liantong.demo_part2.Mapper;

import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Repository;

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
}
