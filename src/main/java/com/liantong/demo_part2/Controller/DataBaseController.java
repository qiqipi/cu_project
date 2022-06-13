package com.liantong.demo_part2.Controller;


import com.liantong.demo_part2.Entity.TimeStamp;
import com.liantong.demo_part2.Service.DataBaseService;
import com.liantong.demo_part2.Entity.TimeStamp.*;
import com.liantong.demo_part2.Utils.JsonResult;
import com.liantong.demo_part2.Utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Api(tags = "操作全部系统的数据库表的接口文档")
@Controller
@RequestMapping("/Access/DataBase")
public class DataBaseController {

    @Autowired
    private DataBaseService dataBaseService;

    private Result result = new Result();


    /**
     * 对全量数据进行备份
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/backup", method = RequestMethod.GET)
    @ApiOperation(value = "备份数据库", response = Result.class)
    public void backUp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag;
        try {
            flag = dataBaseService.backUp();
            if (flag) {
                result.packetResult(true, "数据备份成功", flag);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "数据备份失败", flag);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            System.out.println(e);
            result.packetResult(false, null, null);
            JsonResult.toJson(result, response);
        }
    }

    /**
     * 对全量数据进行回滚
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/rollback", method = RequestMethod.GET)
    @ApiOperation(value = "回滚数据库", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rollback_time", value = "(必填)回滚时间", paramType = "query", required = true, dataType = "String")
    })
    public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rollback_time = request.getParameter("rollback_time");
        boolean flag;
        try {
            flag = dataBaseService.rollback(rollback_time);
            if (flag) {
                result.packetResult(true, "数据回滚成功", flag);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "数据回滚失败", flag);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            System.out.println(e);
            result.packetResult(false, null, null);
            JsonResult.toJson(result, response);
        }
    }


    /**
     * 对全量数据进行回滚
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/importData", method = {RequestMethod.POST})
    @ApiOperation(value = "导入输入表格", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "(必填)表格数据", required = true, dataType = "List")
    })
    public void importNode(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "name") String name, @RequestParam(value = "time") String time,@RequestParam(value = "flag") int flag, @RequestParam(value = "date") String date, @RequestParam(value = "notes") String notes) throws Exception {
//        try {
            //1.读取excel文件，转化为list
            List<String[]> lists = dataBaseService.excelToList(file);
            System.out.println(lists);
            System.out.println("读取文件,转为list");
            String tableName;
            String[] column;

//            String currentDate="_"+date.substring(0,4)+"_"+date.substring(5,7)+"_"+date.substring(8,10);
            switch (name) {
                case "号线表":
                    tableName = "base_line_" + date;
                    column = new String[]{"MAC", "LOID", "OLT", "OLT端口", "分局", "分公司", "OLT管理IP", "CODE_NAME", "七级标准地址ID", "七级标准地址", "归属六级标准地址ID", "楼宇编码", "楼宇名称", "经度", "纬度", "归属五级标准地址ID", "小区", "光分纤盒", "光交接箱", "一级光分路器", "二级光分路器"};
                    break;
                case "3A表":
                    tableName = "base_aaa_" + date;
                    column = new String[]{"日期", "用户ID", "上行流量", "下行流量", "在线时长",
                            "城市", "类型"};
                    break;
                case "用户经分表":
                    tableName = "base_user_divide_" + date;
                    column = new String[]{"部门名称", "设备号码", "大公商类型", "上网账号", "对应的固话号码",
                            "是否IPTV用户", "IPTV及沃TV账号", "ONU设备型号", "速率"};
                    break;
                case "PON流量表":
                    tableName = "base_pon_traffic_" + date;
                    column = new String[]{"分公司", "olt名称", "ip", "厂家", "设备类型",
                            "带宽", "端口", "流入峰值", "流入峰值利用率", "流入均值", "流入均值利用率", "流出峰值", "流出峰值利用率", "流出均值",
                            "流出均值利用率", "PON板号", "PON口号", "IP_and_PON_port", "采集时间"};
                    break;
                case "上联口流量表":
                    tableName = "base_uplink_traffic_" + date;
                    column = new String[]{"分公司", "olt名称", "ip", "厂家", "设备类型",
                            "带宽", "端口", "流入峰值", "流入峰值利用率", "流入均值", "流入均值利用率",
                            "流出峰值", "流出峰值利用率", "流出均值", "流出均值利用率", "上联板号", "上联口号"};
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + name);
            }

            //2.判断文件内容是否为规定的模板
            dataBaseService.compareTitleRow(lists.get(0), column);
            System.out.println("判断文件内容是否为规定的模板");
            //判断表名是否存在
            boolean exist=dataBaseService.judgeExist(name,date);
            System.out.println("判断表名是否存在");
//            System.out.println(exist);
            if(exist){
                //3.删除node表中的数据
                if (flag == 1) {
                    dataBaseService.deleteTable(tableName);
                    System.out.println("删除node表中的数据");
                }
            }
            else{
                //创建数据表
                dataBaseService.creatBaseTable(tableName,name);
                System.out.println("创建数据表");
            }
            //4.把文件内容写入数据库
            for (int i = 1; i < lists.size(); i++) {
                dataBaseService.insertData(tableName, column, lists.get(i));
            }
            if(name.equals("PON流量表")){
                dataBaseService.changeDate(tableName);
            }
            try{System.out.println("把文件内容写入数据库");


            dataBaseService.addNotes(date, name, time,"admin", notes, "操作成功",lists.size()-1+"");
            System.out.println("添加记录");

            result.packetResult(true,  "导入成功", null);
            JsonResult.toJson(result, response);
        } catch (Exception e) {
            try {
                dataBaseService.addNotes(date, name, time,"admin", notes, "操作失败",0+"");
                result.packetResult(false,  "导入失败" , null,"10200");
                JsonResult.toJson(result, response);
            } catch (Exception e1) {
                result.packetResult(false,  "导入失败" , null,"10200");
                JsonResult.toJson(result, response);
            }
        }
    }


    /**
     * 前置操作，建立所有带有时间戳的中间表
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/createMiddleTable", method = RequestMethod.GET)
    @ApiOperation(value = "创建中间操作表和规划表", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "createTime", value = "建表时间", required = true, dataType = "String")
    })
    public void creatMiddleTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag1;
        boolean flag2;
        String createTime = request.getParameter("createTime");
        String currentDate="_"+createTime;
        try {
            flag1 = dataBaseService.creatMiddleTable(currentDate);

            flag2 = dataBaseService.creatPlanTable(currentDate);

            if (flag1&&flag2) {
                result.packetResult(true, "中间表和规划表建立成功", flag1);
                JsonResult.toJson(result, response);
//            } else {
//                result.packetResult(false, "中间表建立过程错误", flag1);
//                JsonResult.toJson(result, response);
//            }
//            if (flag2) {
//                result.packetResult(true, "规划表建立成功", flag2);
//                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "中间表或规划表建立过程错误", flag2,"20000");
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            System.out.println(e);
            result.packetResult(false, "中间表或规划表建立过程异常", null,"20000");
            JsonResult.toJson(result, response);
        }
    }



    /**
     * 获取全量日志操作数据
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/getLog", method = RequestMethod.GET)
    @ApiOperation(value = "获取日志操作记录", response = Result.class)
    public void getLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> massage;
        try {
            massage = dataBaseService.getLog();
            if (massage == null) {
                result.packetResult(false, "获取日志记录为空", null);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(true, "获取日志记录成功", massage);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取日志记录失败", null);
            JsonResult.toJson(result, response);
        }
    }

    /**
     * 首页数据
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/getMessage", method = RequestMethod.GET)
    @ApiOperation(value = "获取首页信息", response = Result.class)
    public void getMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String suffix= TimeStamp.timeFlag;

        List<Map<String, Object>> message=null;
        try {
            if(suffix.equals("")){
                List<Map<String, Object>>  latestList=dataBaseService.getLog();
                if(latestList==null) {
                    result.packetResult(true, "数据表为空", null);
                    JsonResult.toJson(result, response);
                }else{
                    String latest= (String) latestList.get(latestList.size() - 1).get("表名后缀");
                    TimeStamp.setTimeFlag("_"+latest);
                    message=dataBaseService.getMassage(latest);
                }

            }else {
                message = dataBaseService.getMassage(suffix.substring(1));

            }
            if (result == null) {
                result.packetResult(false, "数据表为空", null);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(true, "获取数据表成功", message);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取数据表失败", null);
            JsonResult.toJson(result, response);
        }
    }

    /**
     * 首页数据
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/homeMessage", method = RequestMethod.GET)
    @ApiOperation(value = "获取首页信息", response = Result.class)
    public void homeMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> message;
        try {
            message = dataBaseService.homeMassage();
            if (result == null) {
                result.packetResult(false, "获取首页信息为空", null);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(true, "获取首页信息成功", message);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取首页信息失败", null);
            JsonResult.toJson(result, response);
        }
    }


    /**
     * 首页数据
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/returnMeans", method = RequestMethod.GET)
    @ApiOperation(value = "获取10G套餐分布", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "department", value = "(选填)分公司信息", required = false, dataType = "String"),
            @ApiImplicitParam(name = "olt", value = "(选填)olt信息", required = false, dataType = "String")
    })
    public void returnMeans(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> message;
        String department = request.getParameter("department");
        String olt = request.getParameter("olt");
        message = dataBaseService.returnMeans(department, olt);
        try {

            if (message == null) {
                result.packetResult(false, "获取10GPON套餐信息为空", null);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(true, "获取10GPON套餐信息成功", message);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取10GPON套餐信息失败", null);
            JsonResult.toJson(result, response);
        }
    }


}













