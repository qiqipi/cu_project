package com.liantong.demo_part2.Controller;

import com.liantong.demo_part2.Entity.TimeStamp;
import com.liantong.demo_part2.Entity.User;
import com.liantong.demo_part2.Service.PlanService;
import org.springframework.stereotype.Controller;
import com.liantong.demo_part2.Service.PONService;
import com.liantong.demo_part2.Utils.JsonResult;
import com.liantong.demo_part2.Utils.Result;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Api(tags = "打分、规划部分的接口")
@Controller
@RequestMapping("/Access/Score")
public class PlanController {

    @Autowired
    private PlanService planService;

    private Result result = new Result();


    /**
     * step1 传入sql，用sql进行查询
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/testSelect", method = RequestMethod.GET)
    @ApiOperation(value = "查询数据", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sql", value = "(必填)sql语句", paramType = "query", required = true, dataType = "String", defaultValue = "Select * from demo")
    })
    public void testSelect(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sql = request.getParameter("sql").toLowerCase() + TimeStamp.timeFlag;
        if (!planService.check_sql_grammar(sql)) {
            result.packetResult(false, "sql校验失败，空sql或非法sql", null, "50001");
            JsonResult.toJson(result, response);
        }
//        else if(planService.split_sql(sql) == null){
//            result.packetResult(false, "未传入正确表格，无法进行打分", null,"50001");
//            JsonResult.toJson(result, response);
//        }
        else {
            Map<String, Object> res = new HashMap<>();
            try {
                //查询原始数据
                List<Map<String, Object>> list = planService.testSelect(sql);
                //封装结果
                Set<String> filed = new HashSet<>();
                for (Map<String, Object> element : list) {
                    for (Map.Entry<String, Object> entry : element.entrySet()) {
                        filed.add(entry.getKey());
                    }
                }
                res.put("header", filed);
                res.put("list", list);
                if (sql != null) {
                    result.packetResult(true, "返回成功", res);
                    JsonResult.toJson(result, response);
                } else {
                    result.packetResult(false, "数据为空", null);
                    JsonResult.toJson(result, response);
                }
            } catch (Exception e) {
                System.out.println(e);
                result.packetResult(false, null, null);
                JsonResult.toJson(result, response);
            }
        }
    }

    /**
     * step2 根据传入的sql筛选的区域进行打分。
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/scoring_sql", method = RequestMethod.GET)
    @ApiOperation(value = "根据传入sql的筛选区域来打分", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sql", value = "(必填)sql语句", paramType = "query", required = true, dataType = "String", defaultValue = "Select * from pon_mark_parms"),
            @ApiImplicitParam(name = "meal_weight", value = "(选填)套餐权重参数", paramType = "query", dataType = "String", defaultValue = "3"),
            @ApiImplicitParam(name = "flow_weight", value = "(选填)流量权重参数", paramType = "query", dataType = "String", defaultValue = "1"),
            @ApiImplicitParam(name = "count_weight", value = "(选填)用户数量权重参数", paramType = "query", dataType = "String", defaultValue = "0.5")
    })
    public void scoring_sql(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1.获取4个参数；
        String sql = request.getParameter("sql").toLowerCase() + TimeStamp.timeFlag;
        String meal_weight = request.getParameter("meal_weight");
        String flow_weight = request.getParameter("flow_weight");
        String count_weight = request.getParameter("count_weight");
        //2.判断参数是否为空字符串；
        if ("".equals(meal_weight)) {
            meal_weight = null;
        }
        if ("".equals(flow_weight)) {
            flow_weight = null;
        }
        if ("".equals(count_weight)) {
            count_weight = null;
        }
        //3.获取用户和时间（测试时使用固定用户，一般情况下用一登陆用户做限定。）
        //        User user = (User) request.getSession().getAttribute("user");
        User user = new User(1, "admin", "1234560");
        String time = planService.get_time();
        //4.做关键字判定，并且把当前sql查询的数据insert到结果表中，更新此操作人和时间，但是不打分
        if (!planService.check_sql_grammar(sql)) {
            result.packetResult(false, "sql校验失败，空sql或非法sql", null, "50001");
            JsonResult.toJson(result, response);
        } else if (planService.select_data_insert_inot_result(sql, time, user.getLogin_name()) == -1) {
            result.packetResult(false, "未传入正确表格，无法进行打分", null, "50001");
            JsonResult.toJson(result, response);
        } else {
            Map<String, Object> res = new HashMap<>();
            try {
                //判断和更新传入的系数
                planService.update_factor_weight(meal_weight, flow_weight, count_weight);
                //更新打分sql，打分和返回数据
                List<Map<String, Object>> list = planService.Scoring_and_get_result(sql, time, user.getLogin_name());
                //封装结果
                Set<String> filed = new HashSet<>();
                for (Map<String, Object> element : list) {
                    for (Map.Entry<String, Object> entry : element.entrySet()) {
                        filed.add(entry.getKey());
                    }
                }
                res.put("header", filed);
                res.put("list", list);
                result.packetResult(true, "返回成功", res);
                JsonResult.toJson(result, response);
            } catch (Exception e) {
                result.packetResult(false, "打分出错", null, "50001");
                JsonResult.toJson(result, response);
            }
        }
    }

    /**
     * step3.1 查询打分结果表的区域信息
     */
    @RequestMapping(value = "/department_of_scored", method = RequestMethod.GET)
    @ApiOperation(value = "获取打分结果表中的区域信息", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operate_time", value = "(必填)打分操作时间", paramType = "query", dataType = "String", defaultValue = "2020-06-14-15-46-48")
    })
    public void get_department_of_scored(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String time = request.getParameter("operate_time");
        if ("".equals(time)) {
            result.packetResult(false, "传入信息有误", null, "10010");
            JsonResult.toJson(result, response);
        } else {
            List<Map<String, Object>> res;
            try {
                res = planService.get_department_of_scored(time);
                result.packetResult(true, "返回成功", res);
                JsonResult.toJson(result, response);
            } catch (Exception e) {
                result.packetResult(false, "获取打分结果的局站信息出错", null, "50002");
                JsonResult.toJson(result, response);
            }
        }
    }

    /**
     * step3.2 查询区域的局站信息
     */
    @RequestMapping(value = "/station_of_scored", method = RequestMethod.GET)
    @ApiOperation(value = "获取打分结果表中的局站信息", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "department", value = "(必填)区域", paramType = "query", dataType = "String", defaultValue = "北辰"),
            @ApiImplicitParam(name = "operate_time", value = "(必填)打分操作时间", paramType = "query", dataType = "String", defaultValue = "2020-06-14-15-46-48")
    })
    public void get_station_of_scored(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String department = request.getParameter("department");
        String time = request.getParameter("operate_time");
        if ("".equals(department) || "".equals(time)) {
            result.packetResult(false, "传入信息有误", null, "10010");
            JsonResult.toJson(result, response);
        } else {
            List<Map<String, Object>> res;
            try {
                res = planService.get_station_of_scored(department, time);
                result.packetResult(true, "返回成功", res);
                JsonResult.toJson(result, response);
            } catch (Exception e) {
                result.packetResult(false, "获取打分结果的区域信息出错", null, "50002");
                JsonResult.toJson(result, response);
            }
        }
    }


    /**
     * step4 根据传入的局站信息进行扩容规划
     *
     * @param request
     * @param response
     * @param data
     * @throws Exception
     */
//    POST请求提交接口，目前只能做到json数据为String、String的key和value类型数据。
    @RequestMapping(value = "/plan_v1", method = RequestMethod.POST)
    public void plan_v1(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> data) throws Exception {
//        List<List<Map<String, Object>>> res;
        Map<String, Object> res;
        //1.获取传入的局站名，以“，”分割，结尾是不是有，不影响
        String[] station_list;
        //2.获取操作人
        String operator = data.get("operator");
        //3.获取时间信息
        String operate_time = data.get("operate_time");
        // 4.获取"是否只展示必须扩容"的标识符
        String if_necessary_selected = data.get("if_necessary_selected");
//        //5.获取是否全选局站
        String if_select_all_station = data.get("if_select_all_station");
        //5.获取希望规划条数
        int nums = Integer.parseInt(data.get("nums"));

////
        if (if_select_all_station.equals("yes")) {
//
            List<String> temp = planService.selectAllStation();

            station_list = new String[temp.size()];
            for (int i = 0; i < temp.size(); i++) {
                station_list[i] = temp.get(i);
            }
        } else {
            String list = data.get("station_list");
            station_list = list.split(",");
        }
        res = planService.plan(station_list, operator, operate_time, if_necessary_selected, nums);
        try {

            try {
//                res =  planService.plan(station_list,operator,operate_time);
                result.packetResult(true, "规划成功", res);
                JsonResult.toJson(result, response);
            } catch (Exception e1) {
                result.packetResult(true, "规划失败，内部出现错误", null, "20000");
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取请求参数出错", null, "10100");
            JsonResult.toJson(result, response);
        }
    }


//    @RequestMapping(value = "/scoring", method = RequestMethod.GET)
//    @ApiOperation(value = "给选定区域的PON口打分", response = Result.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "department", value = "(选填)选择哪个区域", paramType = "query",  dataType = "String", defaultValue = "北辰"),
//            @ApiImplicitParam(name = "olt", value = "(选填)选择哪个OLT", paramType = "query",  dataType = "String", defaultValue = "BCBC_HW_OLT002")
//    })
//    public void Scoring_PON(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String department = request.getParameter("department");
//        String olt = request.getParameter("olt");
//        String time = planService.get_time();
////        User user = (User) request.getSession().getAttribute("user");
//        User user = new User(1,"admin","1234560");
//        if(user == null | user.getLogin_name() == "" | user.getLogin_name() == null){
//            result.packetResult(false, "未获取到登陆用户的信息", null,"10020");
//            JsonResult.toJson(result, response);
//            response.setStatus(200);
//        }
//        else {
//            String user_name = user.getLogin_name();
//            if ("".equals(department)) {
//                department = null;
//            }
//            if ("".equals(olt)) {
//                olt = null;
//            }
////            List<Map<String, Object>> res = planService.Scoring_PON(department,olt,user_name);
//
//            try {
//                List<Map<String, Object>> res = planService.Scoring_PON(department,olt,user_name,time);
//                if (res != null) {
//                    result.packetResult(true, "返回成功", res);
//                    JsonResult.toJson(result, response);
//                } else {
//                    result.packetResult(false, "数据为空", null);
//                    JsonResult.toJson(result, response);
//                }
//            } catch (Exception e) {
//                result.packetResult(false, "打分出错", null);
//                JsonResult.toJson(result, response);
//                response.setStatus(200);
//            }
//        }
//
//    }
//
//
//    @RequestMapping(value = "/Scoring_PON_by_time", method = RequestMethod.GET)
//    @ApiOperation(value = "根据时间和登陆用户筛选打分结果", response = Result.class)
//    @ApiImplicitParams({
//
//            @ApiImplicitParam(name = "start_time", value = "(必填)选择起始时间", paramType = "query",  dataType = "String", defaultValue = "2020-05-31-22-26-05"),
//            @ApiImplicitParam(name = "end_time", value = "(必填)选择终止时间", paramType = "query",  dataType = "String", defaultValue = "2020-05-31-22-28-05")
//    })
//    public void Scoring_PON_by_time(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String start_time = request.getParameter("start_time");
//        String end_time = request.getParameter("end_time");
////        User user = (User) request.getSession().getAttribute("user");
//        User user = new User(1,"admin","1234560");
//        if(user == null | user.getLogin_name() == "" | user.getLogin_name() == null){
//            result.packetResult(false, "未获取到登陆用户的信息", null,"10020");
//            JsonResult.toJson(result, response);
//            response.setStatus(200);
//        }
//        else if(start_time == "" | start_time == null |end_time == "" | end_time == null){
//            result.packetResult(false, "请求传参失败，请重新传入参数", null,"10080");
//            JsonResult.toJson(result, response);
//            response.setStatus(200);
//        }
//        else
//        {
//            String user_name = user.getLogin_name();
//
//            try {
//                List<Map<String, Object>> res = planService.Scoring_PON_by_time(start_time,end_time,user_name);
//                if (res != null) {
//                    result.packetResult(true, "返回成功", res);
//                    JsonResult.toJson(result, response);
//                } else {
//                    result.packetResult(false, "数据为空", null);
//                    JsonResult.toJson(result, response);
//                }
//            } catch (Exception e) {
//                result.packetResult(false, "筛选打分结果出错", null);
//                JsonResult.toJson(result, response);
//                response.setStatus(200);
//            }
//        }
//
//    }
}
