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
    public List<Map<String, Object>> getAaaTable() {
        List<Map<String, Object>> aaaTable = pon2022MigrationMapper.getAaaTable();
        return aaaTable;
    }


    @Override
    public boolean createMergeTable() throws Exception {
        pon2022MigrationMapper.dropMergeTable();
        pon2022MigrationMapper.createMergeTable();
        pon2022MigrationMapper.initMergeTable();
        System.out.println("merge表格完成");
        try {
        }catch (Exception e){
            throw new Exception("生成merge表出错");
        }
        return true;
    }


}
