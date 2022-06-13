package com.liantong.demo_part2.Controller;

import com.liantong.demo_part2.Entity.User;
import com.liantong.demo_part2.Service.PONService;
import com.liantong.demo_part2.Utils.JsonResult;
import com.liantong.demo_part2.Utils.Result;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/Access/NewPON")
@Api(tags = "PON端口小区设备统计文档")
public class PONNewFuncController {
    private Result result = new Result();

    @Autowired
    private PONService ponService;


    @RequestMapping(value = "/usernum_of_different_meal", method = RequestMethod.GET)
    @ApiOperation(value = "10G PON 板（口）下分类型用户数量分布", response = Result.class)
    public void UserNumOfDifferentMeal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<Set<Map<String, Object>>> list = ponService.UserNumOfDifferentMeal();
            if (list != null) {
                result.packetResult(true, "返回成功", list);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "数据为空", null);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, null, null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }

    @RequestMapping(value = "/returnDepartment", method = RequestMethod.GET)
    @ApiOperation(value = "查询区域", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableIndex", value = "(必填)选择哪个表", paramType = "query", required = true, dataType = "String", defaultValue = "1000M", example = "100M"),
    })
    public void returnDepartment(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableIndex = request.getParameter("tableIndex");
        try {
            List<Map<String, Object>> list = ponService.returnDepartmentOf_Table(tableIndex);
            if (list != null) {
                result.packetResult(true, "返回成功", list);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "数据为空", null);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, null, null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }


    @RequestMapping(value = "/returnOLTNameOf_Table", method = RequestMethod.GET)
    @ApiOperation(value = "查询OLT名称", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableIndex", value = "(必填)选择哪个表", paramType = "query", required = true, dataType = "String", defaultValue = "1000M"),
            @ApiImplicitParam(name = "department", value = "(必填)选择哪个区域", paramType = "query", required = true, dataType = "String", defaultValue = "红桥")
    })
    public void returnOLTNameOf_Table(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableIndex = request.getParameter("tableIndex");
        String department = request.getParameter("department");
        try {
            List<Map<String, Object>> list = ponService.returnOLTNameOf_Table(tableIndex, department);
            if (list != null) {
                result.packetResult(true, "返回成功", list);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "数据为空", null);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, null, null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }


    @RequestMapping(value = "/returnPON_PortNameOf_Table", method = RequestMethod.GET)
    @ApiOperation(value = "查询PON_Port名称", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableIndex", value = "(必填)选择哪个表", paramType = "query", required = true, dataType = "String", defaultValue = "1000M"),
            @ApiImplicitParam(name = "department", value = "(必填)选择哪个区域", paramType = "query", required = true, dataType = "String", defaultValue = "红桥"),
            @ApiImplicitParam(name = "OLT_name", value = "(必填)选择哪个OLT", paramType = "query", required = true, dataType = "String", defaultValue = "WQDY_HW_OLT01")
    })
    public void returnPON_PortNameOf_Table(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableIndex = request.getParameter("tableIndex");
        String department = request.getParameter("department");
        String OLT_name = request.getParameter("OLT_name");
        try {
            List<Map<String, Object>> list = ponService.returnPON_PortNameOf_Table(tableIndex, department, OLT_name);
            if (list != null) {
                result.packetResult(true, "返回成功", list);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "数据为空", null);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, null, null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }


    @RequestMapping(value = "/return_AllData", method = RequestMethod.GET)
    @ApiOperation(value = "查询数据", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableIndex", value = "(必填)选择哪个表", paramType = "query", required = true, dataType = "String", defaultValue = "1000M"),
            @ApiImplicitParam(name = "department", value = "(必填)选择哪个区", paramType = "query", dataType = "String", defaultValue = "塘沽"),
            @ApiImplicitParam(name = "OLT_name", value = "(必填)选择哪个OLT", paramType = "query", dataType = "String", defaultValue = "TGBSQ_HW_OLT01"),
            @ApiImplicitParam(name = "board", value = "(必填)选择哪个PON板", paramType = "query", dataType = "String", defaultValue = "14"),
            @ApiImplicitParam(name = "port", value = "(必填)选择哪个PON口", paramType = "query", dataType = "String", defaultValue = "3")
    })
    public void return_AllData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = request_Parms_Dealing(request);
        try {

            Map<String, Object> res = packet_AllData_result(map);

            if (res != null) {
                result.packetResult(true, "返回成功", res);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "数据为空", null);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, null, null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }

    @RequestMapping(value = "/return_AllData_second", method = RequestMethod.GET)
    @ApiOperation(value = "查询数据", response = Result.class)
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableIndex", value = "(必填)选择哪个表", paramType = "query", required = true, dataType = "String", defaultValue = "1000M"),
            @ApiImplicitParam(name = "department", value = "(必填)选择哪个区", paramType = "query", dataType = "String", defaultValue = "塘沽"),
            @ApiImplicitParam(name = "OLT_name", value = "(必填)选择哪个OLT", paramType = "query", dataType = "String", defaultValue = "TGBSQ_HW_OLT01"),
            @ApiImplicitParam(name = "board", value = "(必填)选择哪个PON板", paramType = "query", dataType = "String", defaultValue = "14"),
            @ApiImplicitParam(name = "port", value = "(必填)选择哪个PON口", paramType = "query", dataType = "String", defaultValue = "3")
    })
    public void return_AllData_second(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = request_Parms_Dealing(request);
        List<String> second_Object_list = ponService.second_Object_Name_List(map);
        if (second_Object_list == null) {
            result.packetResult(false, "此层级不可展示二级数据", null);
            JsonResult.toJson(result, response);
        } else {
            Map<String, Object> res = new HashMap<>();
            if (second_Object_list.get(0) == "department") {

                for (int i = 1; i < second_Object_list.size(); i++) {
                    map.put("department", second_Object_list.get(i));
                    res.put(second_Object_list.get(i), packet_AllData_result(map));
                }
            } else if (second_Object_list.get(0) == "OLT_name") {
                for (int i = 1; i < second_Object_list.size(); i++) {
                    map.put("OLT_name", second_Object_list.get(i));
                    res.put(second_Object_list.get(i), packet_AllData_result(map));
                }
            }
            result.packetResult(true, "二级数据填充成功", res);
            JsonResult.toJson(result, response);
        }

    }


    @RequestMapping(value = "/return_1000M_Table", method = RequestMethod.GET)
    @ApiOperation(value = "返回1000M表10G、非10G数据", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableIndex", value = "(必填)选择哪个表", paramType = "query", required = true, dataType = "String", defaultValue = "1000M"),
            @ApiImplicitParam(name = "department", value = "(必填)选择哪个区", paramType = "query", dataType = "String", defaultValue = "塘沽"),
            @ApiImplicitParam(name = "OLT_name", value = "(必填)选择哪个OLT", paramType = "query", dataType = "String", defaultValue = "TGBSQ_HW_OLT01"),

    })
    public void return_1000M_Table(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = request_Parms_Dealing(request);
//        String tableIndex = request.getParameter("tableIndex");
//        String department = request.getParameter("department");
//        String OLT_name = request.getParameter("OLT_name");
//        //新增传参判定，若出现传参为空字符串，但是非null时，设为null，防止Mapper.xml识别不到
//        if("".equals(department)){
//            department = null;
//        }
//        if("".equals(OLT_name)){
//            OLT_name = null;
//        }
        Map<String, List<Map<String, Object>>> res;
        try {
            res = ponService.return_1000M_Table(map.get("tableIndex"), map.get("department"), map.get("OLT_name"));
            if (res != null) {
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
            response.setStatus(400);
        }
    }

    public static Map<String, String> request_Parms_Dealing(HttpServletRequest request) {
        //tableIndex作为预留字段，到时候判定查哪个表的数据。
        String tableIndex = request.getParameter("tableIndex");
        String department = request.getParameter("department");
//        System.out.println(department);
        String OLT_name = request.getParameter("OLT_name");
        String board = request.getParameter("board");
        String port = request.getParameter("port");
        //新增传参判定，若出现传参为空字符串，但是非null时，设为null，防止Mapper.xml识别不到
        if ("".equals(department)) {
            department = null;
        }
        if (department != null) {
//            department = department.replace("分公司", "通信分公司");
            department = department.trim();
        }
        if ("".equals(OLT_name)) {
            OLT_name = null;
        }
        if ("".equals(board)) {
            board = null;
        }
        if ("".equals(port)) {
            port = null;
        }
        Map<String, String> result = new HashMap<>();
        result.put("tableIndex", tableIndex);
        result.put("department", department);
        result.put("OLT_name", OLT_name);
        result.put("board", board);
        result.put("port", port);
        System.out.println((result));
        return result;
    }

    public Map<String, Object> packet_AllData_result(Map<String, String> map) {
        Map<String, Object> map1 = ponService.return_1000MTable_userNum(map.get("department"), map.get("OLT_name"), map.get("board"), map.get("port"));
//        System.out.println("这是第一");
        System.out.println(map1);
        Map<String, Object> map2 = ponService.return_1000MTable_Not10G_OLTNum(map.get("department"), map.get("OLT_name"));
//        System.out.println("这是第二");
        System.out.println(map2);
        Map<String, Object> map3 = ponService.return_1000MTable_10G_PonNum(map.get("department"), map.get("OLT_name"));
        System.out.println("这是第三");
        System.out.println(map3);
        List<Map<String, Object>> list4 = ponService.return_request_parms(map.get("tableIndex"), map.get("department"), map.get("OLT_name"), map.get("board"), map.get("port"));
        System.out.println("这是第四");
        System.out.println("list4: " + list4);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("userNum", map1);
        result.put("OLTNum", map2);
        result.put("PonNum", map3);
        result.put("request_parms", list4.get(0));
        return result;

    }


}
