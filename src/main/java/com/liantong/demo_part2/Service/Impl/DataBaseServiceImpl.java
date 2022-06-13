package com.liantong.demo_part2.Service.Impl;

import com.liantong.demo_part2.Entity.TimeStamp;
import com.liantong.demo_part2.Mapper.DataBaseMapper;
import com.liantong.demo_part2.Service.DataBaseService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class DataBaseServiceImpl implements DataBaseService {

    @Override
    public List<Map<String, Object>> homeMassage() {
        List<Map<String, Object>> result = dataBaseMapper.homeMassage(TimeStamp.timeFlag);
        return result;

    }

    @Autowired
    private DataBaseMapper dataBaseMapper;

    @Override
    public boolean backUp() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
        String t = df.format(new Date());

        try {
            String bashCommand = "sh " + "/home/jieruwangSu/houtaiJava/backup.sh " + t;
            Runtime runtime = Runtime.getRuntime();
            Process pro = runtime.exec(bashCommand);
            int status = pro.waitFor();
            if (status != 0) {
                System.out.println("Failed to call shell's command ");
                return false;
            }
            dataBaseMapper.backUp(t);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 14);
            Date today = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
            String ddl = format.format(today);
            dataBaseMapper.delete(ddl);
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            StringBuffer str = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                str.append(line).append("\n");
            }

            String result = str.toString();
            System.out.println(result);

        } catch (Exception ec) {
            ec.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean rollback(String rollback_time) {
        try {
            String bashCommand = "sh " + "/home/jieruwangSu/houtaiJava/rollback.sh " + rollback_time;
            Runtime runtime = Runtime.getRuntime();
            Process pro = runtime.exec(bashCommand);
            int status = pro.waitFor();
            if (status != 0) {
                System.out.println("Failed to call shell's command ");
                return false;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            StringBuffer str = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                str.append(line).append("\n");
            }

            String result = str.toString();
            System.out.println(result);

        } catch (Exception ec) {
            ec.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<String[]> excelToList(MultipartFile file) throws Exception {
        checkFile(file);
        System.out.println("检查完成");
        //获得Workbook工作薄对象
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<String[]> dataList = new ArrayList<>();
        // 当前读取行数-0-based
        int currentRowNum = 0;
        Workbook workbook = null;
        try {
            workbook = getWorkBook(file);
//            workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);
//            System.out.println("中间");
//            logger.info("sheet name:" + sheet.getSheetName() + ",last rownum:" + sheet.getLastRowNum());
            for (int x = 0; x <= sheet.getLastRowNum(); x++) {
                Row row = sheet.getRow(x);
                currentRowNum = row.getRowNum();
                StringBuffer sb = new StringBuffer();
                //设置单元格类型
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    if (row.getCell(i) != null) {
                        row.getCell(i).setCellType(CellType.STRING);
//                        System.out.println(row.getCell(i).toString());
                        String s = row.getCell(i).getStringCellValue();
//                    Cell cell = row.getCell(i);
                        sb.append(s);
                        sb.append(",");

                    } else {
//                        System.out.println("空"+i);
                        sb.append(" ");
                        sb.append(",");

                        //row.getCell(i).setCellType(CellType.STRING);
                        //row.getCell(i).setCellValue("0");
//                        System.out.println(row.getCell(i));

                    }

                }
//                System.out.println(sb);
                dataList.add(sb.toString().substring(0, sb.toString().lastIndexOf(",")).split(","));


            }
            System.out.println("已转为list");
        } catch (Exception e) {
//            logger.error("数据读取到第"+currentRowNum+"行时失败");
            throw new Exception("数据读取到第" + currentRowNum + "行时失败");
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return dataList;
    }

    @Override
    public void insertData(String tableName, String[] column, String[] data) {
        dataBaseMapper.insertData(tableName, column, data);
    }

    @Override
    public void changeDate(String tableName) {
        dataBaseMapper.changeDate(tableName);
    }

    @Override
    public List<Map<String, Object>> returnMeans(String department, String olt) {
        return dataBaseMapper.returnMeans(TimeStamp.timeFlag, department, olt);
    }

    @Override
    public boolean creatMiddleTable(String creatTime) throws Exception {

        dataBaseMapper.creatMiddle1(creatTime);
        dataBaseMapper.insertMiddle1(creatTime);
        int count = (int) (0.1 * dataBaseMapper.countMiddle1(creatTime));
        System.out.println(count);
        dataBaseMapper.updateMiddle1(creatTime, count);
        System.out.println("middle1已经创建完成");

        dataBaseMapper.creatMiddle2(creatTime);
        dataBaseMapper.insertMiddle2(creatTime);
        dataBaseMapper.updateMiddle2(creatTime);
        System.out.println("middle2已经创建完成");

        dataBaseMapper.createUserMassage(creatTime);
        dataBaseMapper.insertUserMassage(creatTime);
        dataBaseMapper.updateUserMassage(creatTime);
        System.out.println("用户表已经创建完成");

        dataBaseMapper.createOLTMassage(creatTime);
        dataBaseMapper.insertOLTMassage(creatTime);
        dataBaseMapper.updateOLTMassage(creatTime);
        dataBaseMapper.updateOLTUser(creatTime);
        System.out.println("OLT表已经创建完成");

        dataBaseMapper.createPONMassage(creatTime);
        dataBaseMapper.insertPONMassage(creatTime);
        dataBaseMapper.updatePONMassage(creatTime);
        System.out.println("PON表已经创建完成");
        try {

        } catch (Exception e) {
            throw new Exception("建表或插入数据过程中出错！");
        }

        return true;
    }

    @Override
    public boolean creatPlanTable(String creatTime) throws Exception {
        try {
            dataBaseMapper.creatFactor(creatTime);
            System.out.println("factor已经创建完成");
            dataBaseMapper.insertFactor(creatTime);
            dataBaseMapper.creatParms(creatTime);
            dataBaseMapper.insertParms(creatTime);
            dataBaseMapper.updateParms(creatTime);
            System.out.println("prams已经创建完成");
            dataBaseMapper.creatResult(creatTime);
            System.out.println("result已经创建完成");
        } catch (Exception e) {
            throw new Exception("建表或插入数据过程中出错！");
        }

        return true;
    }

    @Override
    public List<Map<String, Object>> getLog() {
        List<Map<String, Object>> log = dataBaseMapper.getLog();
        return log;
    }

    @Override
    public boolean judgeExist(String name, String date) {
        int exist = dataBaseMapper.judgeExist(name, date);
        System.out.println("exist:"+exist);
        if (exist > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getMassage(String suffix) {
        List<Map<String, Object>> result = dataBaseMapper.getMassage(suffix);
        return result;
    }

    @Override
    public boolean addNotes(String date, String tableName, String time, String operator, String notes, String status, String nums) throws Exception {
        dataBaseMapper.addNotes(date, tableName, time, operator, notes, status, nums);
        return true;
    }


    @Override
    public boolean creatBaseTable(String tableName, String name) throws Exception {

        try {
            switch (name) {
                case "3A表":
                    dataBaseMapper.creatBase1(tableName);
                    break;
                case "号线表":
                    dataBaseMapper.creatBase2(tableName);
                    break;
                case "PON流量表":
                    dataBaseMapper.creatBase3(tableName);
                    break;
                case "上联口流量表":
                    dataBaseMapper.creatBase4(tableName);
                    break;
                case "用户经分表":
                    dataBaseMapper.creatBase5(tableName);
                    break;
            }


        } catch (Exception e) {
            throw new Exception("建表过程中出错！");
        }

        return true;
    }

    //校验文件是否合法
    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        //判断文件是否是excel文件
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            throw new IOException(fileName + "不是excel文件");
        }
    }

    public static Workbook getWorkBook(MultipartFile file) throws Exception {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith("xls")) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith("xlsx")) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            throw new Exception("读入文件有误！");
        }
        return workbook;
    }

    @Override
    public void compareTitleRow(String[] list, String[] titleRow) throws Exception {
        if (list.length != titleRow.length) {
            throw new Exception("文件列数目有误！");
        }
        for (int i = 0; i < list.length; i++) {
            if (!list[i].equals(titleRow[i])) {
                throw new Exception("对应字段名有误会！");
            }
        }
    }

    @Override
    public void deleteTable(String tableName) {
        dataBaseMapper.deleteTable(tableName);
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 14);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
        String result = format.format(today);
        System.out.println(result);
    }

}
