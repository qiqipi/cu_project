package com.liantong.demo_part2.Service.Impl;

import com.liantong.demo_part2.Mapper.PON2022MigrationMapper;
import com.liantong.demo_part2.Service.PON2022MigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author qiqipi
 * @create 2022/7/10 6:12
 */
@Service
public class PON2022MigrationServiceImpl implements PON2022MigrationService {

    @Autowired
    PON2022MigrationMapper pon2022MigrationMapper;

    @Override
    public List<Map<String, Object>> getOLTChosenTable() {
        List<Map<String, Object>> aaaTable = pon2022MigrationMapper.getOLTChosenTable();
        return aaaTable;
    }

    public List<Map<String,Object>> getPlanTable(String OLT_name){
        List<Map<String, Object>> planTable = pon2022MigrationMapper.getPlanTable(OLT_name);
        return planTable;
    }

    @Override
    public boolean createMergeTable() throws Exception {
        pon2022MigrationMapper.dropMergeTable();
        pon2022MigrationMapper.createMergeTable();
        pon2022MigrationMapper.initMergeTable();

        try {
        }catch (Exception e){
            throw new Exception("生成merge表出错");
        }
        System.out.println("merge表格完成");
        return true;
    }

    @Override
    public boolean createOLTChosenTable() throws Exception {

        pon2022MigrationMapper.createOLTChosenTable();
        pon2022MigrationMapper.insertOLTChosenTable();
        try {
        }catch (Exception e){
            throw new Exception("生成OLTChosen表出错");
        }
        System.out.println("OLTChosen完成");
        return true;
    }


    public List<Map<String,Object>> getRegion(){
        List<Map<String, Object>> region = pon2022MigrationMapper.getRegion();
        return region;
    }


    public boolean createPlanTable() throws Exception {
        pon2022MigrationMapper.createPlanTable();
        pon2022MigrationMapper.insertPlanTable1();
        try {
        }catch (Exception e){
            throw new Exception("生成PlanTable表出错");
        }
        System.out.println("PlanTable完成");
        return true;
    }

}
