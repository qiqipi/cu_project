package com.liantong.demo_part2.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DataBaseService {

    boolean backUp();


    boolean rollback(String rollback_time);

//    List<List<String>> getExcelFile(MultipartFile file) throws Exception;


//    List<List<String>> readExcel(String path);

    void compareTitleRow(String[] list, String[] titleRow) throws Exception;

    /**
     * 删除node表中所有数据
     */
    void deleteTable(String tableName);


    List<String[]> excelToList(MultipartFile file) throws IOException, Exception;

    void insertData(String tableName, String[] column, String[] data);

    void changeDate(String tableName);


    boolean creatBaseTable(String tableName,String name) throws Exception;

    boolean creatMiddleTable(String creatTime) throws Exception;

    boolean creatPlanTable(String creatTime) throws Exception;

    boolean addNotes(String date,String tableName,String time,String operator,String notes,String status,String nums) throws Exception;

    List<Map<String, Object>> getLog();

    List<Map<String, Object>> getMassage(String suffix);

    List<Map<String, Object>> returnMeans(String department,String olt);

    List<Map<String, Object>> homeMassage();

    boolean judgeExist(String name,String date);
}
