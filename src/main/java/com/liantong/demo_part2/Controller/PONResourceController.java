package com.liantong.demo_part2.Controller;

import com.liantong.demo_part2.Entity.TimeStamp;
import com.liantong.demo_part2.Service.PONResourceService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import java.util.*;

@Controller
@RequestMapping("/Access/PONResource")
@Api(tags = "PON利用率统计文档")
public class PONResourceController {

    private Result result = new Result();

    @Autowired
    private PONResourceService ponResourceService;


    @RequestMapping(value = "/PON_board_rate", method = RequestMethod.GET)
    @ApiOperation(value = "统计不同分公司、OLT下的PON板资源利用率（槽位利用率）", response = Result.class)
    @ApiImplicitParams({

            @ApiImplicitParam(name = "department", value = "(选填)选择哪个区域", paramType = "query", dataType = "String", defaultValue = "南开通信分公司"),
            @ApiImplicitParam(name = "olt", value = "(选填)选择哪个OLT", paramType = "query", dataType = "String", defaultValue = "西营门局OLT15-C300")
    })
    public void PONBoardRate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String department = request.getParameter("department");
        String olt = request.getParameter("olt");
        if ("".equals(department)) {
            department = null;
        }
        if ("".equals(olt)) {
            olt = null;
        }
        try {
            Map<String, Object> res = ponResourceService.PON_board_rate(department, olt, TimeStamp.timeFlag);
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


    @RequestMapping(value = "/PON_port_rate", method = RequestMethod.GET)
    @ApiOperation(value = "统计不同分公司、OLT下的PON口资源利用率（在已使用的板上）", response = Result.class)
    @ApiImplicitParams({

            @ApiImplicitParam(name = "department", value = "(选填)选择哪个区域", paramType = "query", dataType = "String", defaultValue = "南开通信分公司"),
            @ApiImplicitParam(name = "olt", value = "(选填)选择哪个OLT", paramType = "query", dataType = "String", defaultValue = "西营门局OLT15-C300")
    })
    public void PON_port_rate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String department = request.getParameter("department");
        String olt = request.getParameter("olt");
        if ("".equals(department)) {
            department = null;
        }
        if ("".equals(olt)) {
            olt = null;
        }
        try {
            Map<String, Object> res = ponResourceService.PON_port_rate(department, olt, TimeStamp.timeFlag);
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


    @RequestMapping(value = "/PON_Port_Used", method = RequestMethod.GET)
    @ApiOperation(value = "空闲PON端口利用率", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "department", value = "(选填)选择哪个区域", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "olt", value = "(选填)选择哪个OLT", paramType = "query", dataType = "String")
    })
    public void PON_Port_Used(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String department = request.getParameter("department");
        String station = request.getParameter("station");
        String olt = request.getParameter("olt");
        List<Map<String, Object>> res = ponResourceService.PON_Port_Used(department, station, olt);
        try {

            if (res != null) {
                result.packetResult(true, "返回成功", res);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "数据为空", null);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "出现异常", null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }


    @RequestMapping(value = "/totalRate", method = RequestMethod.GET)
    @ApiOperation(value = "统计不同分公司、OLT下的PON口资源利用率（在已使用的板上）和PON板资源利用率（槽位利用率）", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "department", value = "(选填)选择哪个区域", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "olt", value = "(选填)选择哪个OLT", paramType = "query", dataType = "String")
    })
    public void totalRate(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "department", required = false) String department, @RequestParam(value = "olt", required = false) String olt) throws Exception {
        //String department = request.getParameter("department");
        //String olt = request.getParameter("olt");
        System.out.println(department);
        System.out.println(olt);
        Map<String, Map<String, Object>> res = new HashMap<>();
        Map<String, Object> port_rate = ponResourceService.PON_port_rate(department, olt, TimeStamp.timeFlag);
        Map<String, Object> board_rate = ponResourceService.PON_board_rate(department, olt, TimeStamp.timeFlag);
        try {


            if (port_rate != null && board_rate != null) {
                res.put("port_rate", port_rate);
                res.put("board_rate", board_rate);
            }
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


    @RequestMapping(value = "/returnDepartment", method = RequestMethod.GET)
    @ApiOperation(value = "查询所有区域信息", response = Result.class)
    public void returnDepartment(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<String> list = ponResourceService.returnDepartment();
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

    @RequestMapping(value = "/returnOLT", method = RequestMethod.GET)
    @ApiOperation(value = "查询某个区域的全部OLT信息", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "department", value = "(必填)选择哪个表", paramType = "query", required = true, dataType = "String", defaultValue = "南开通信分公司", example = "南开通信分公司")
    })
    public void returnOLT(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String department = request.getParameter("department");
        try {
            List<String> list = ponResourceService.returnOLT(department);
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

    @RequestMapping(value = "/returnTree", method = RequestMethod.GET)
    @ApiOperation(value = "返回树状图信息", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "department", value = "(必填)选择哪个局站", paramType = "query", required = true, dataType = "String", defaultValue = "河北", example = "河北")
    })
    public void returnTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String department = request.getParameter("department").trim();
        try {
            Map<String, Object> list = ponResourceService.returnTree(department);
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


    @RequestMapping(value = "/returnTime", method = RequestMethod.GET)
    @ApiOperation(value = "测试时间戳功能", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time", value = "(必填)选择时间", paramType = "query", required = true, dataType = "String", defaultValue = "20190708", example = "20190708"),
            @ApiImplicitParam(name = "option", value = "(必填)选择时间", paramType = "query", required = true, dataType = "String", defaultValue = "根据表名", example = "根据表名")
    })
    public void returnTime(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String time = request.getParameter("time");
        String option = request.getParameter("option");

        try {
            List<Map<String, Object>> list = ponResourceService.returnTime(time, option);
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


    @RequestMapping(value = "/returnMap", method = RequestMethod.GET)
    @ApiOperation(value = "首页地图信息", response = Result.class)
    public void returnMap(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<List<Object>> list = ponResourceService.returnMap();
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

    @RequestMapping(value = "/returnTianjin", method = RequestMethod.GET)
    @ApiOperation(value = "首页地图天津和平区信息", response = Result.class)
    public void returnTianjin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<Map<String, Object>> list = ponResourceService.returnTianjin();
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

    @RequestMapping(value = "/returnNewTianjin", method = RequestMethod.GET)
    @ApiOperation(value = "首页地图天津和平区信息", response = Result.class)
    public void returnNewTianjin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<Map<String, Object>> list = ponResourceService.returnNewTianjin();
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

    @RequestMapping(value = "/returnTianjinNumber", method = RequestMethod.GET)
    @ApiOperation(value = "首页地图天津和平区新老信息数量", response = Result.class)
    public void returnTianjinNumber(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<Map<String, Object>> list = ponResourceService.returnTianjinNumber();
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


    @RequestMapping(value = "/returnRectification", method = RequestMethod.GET)
    @ApiOperation(value = "存储首页地图天津纠偏信息", response = Result.class)
    public void returnRectification(HttpServletRequest request, HttpServletResponse response, @RequestParam("name") String name, @RequestParam("lng") String lng, @RequestParam("lat") String lat, @RequestParam("clear") Integer clear) throws Exception {
        boolean flag;
        try {

            flag = ponResourceService.returnRectification(name, lng, lat);
            if (flag) {
                result.packetResult(true, "存储成功", null);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "存储失败", null);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, null, null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }

    @RequestMapping(value = "/venderMassage", method = RequestMethod.GET)
    @ApiOperation(value = "厂家分区信息", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "department", value = "(选填)选择哪个分公司", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "station", value = "(选填)选择哪个局站", paramType = "query", dataType = "String")
    })
    public void returnVenderMassage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> massage;
        String department = request.getParameter("department");
        String station = request.getParameter("station");
        System.out.println(department);
        System.out.println(station);
        try {
            massage = ponResourceService.returnVenderMassage(department, station);
            if (massage.size() != 0) {
                result.packetResult(true, "获取信息成功", massage);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "获取信息失败", massage);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取信息出现错误", null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }

    @RequestMapping(value = "/userType", method = RequestMethod.GET)
    @ApiOperation(value = "大工商类型统计", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "department", value = "分公司", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "station", value = "局站", paramType = "query", dataType = "String")
    })
    public void userType(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> massage;
        String department = request.getParameter("department");
        String station = request.getParameter("station");
        try {
            massage = ponResourceService.userType(department, station);
            if (massage.size() != 0) {
                result.packetResult(true, "获取信息成功", massage);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "获取信息失败", massage);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取信息出现错误", null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }

    @RequestMapping(value = "/returnIPTVMassage", method = RequestMethod.GET)
    @ApiOperation(value = "IPTV用户统计", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "department", value = "分公司", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "station", value = "局站", paramType = "query", dataType = "String")
    })
    public void returnIPTVMassage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> massage;
        String department = request.getParameter("department");
        String station = request.getParameter("station");
        String olt = request.getParameter("olt");
        massage = ponResourceService.returnIPTVMassage(department, station, olt);
        try {

            if (massage.size() != 0) {
                result.packetResult(true, "获取信息成功", massage);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "获取信息失败", massage);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取信息出现错误", null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }

    @RequestMapping(value = "/modelMassage", method = RequestMethod.GET)
    @ApiOperation(value = "设备分区信息", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "department", value = "(选填)选择哪个分公司", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "station", value = "(选填)选择哪个局站", paramType = "query", dataType = "String")
    })
    public void returnModelMassage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> massage;
        String department = request.getParameter("department");
        String station = request.getParameter("station");
        System.out.println(department);
        System.out.println(station);
        try {
            massage = ponResourceService.returnModelMassage(department, station);
            if (massage.size() != 0) {
                result.packetResult(true, "获取信息成功", massage);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "获取信息失败", massage);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取信息出现错误", null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }

    @RequestMapping(value = "/UserMassage", method = RequestMethod.GET)
    @ApiOperation(value = "用户流量分区信息", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "department", value = "(选填)选择哪个分公司", paramType = "query", dataType = "String")
    })
    public void returnUserMassage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> massage;
        String department = request.getParameter("department");
        System.out.println(department);
        massage = ponResourceService.returnUserMassage(department);
        try {
            if (massage.size() != 0) {
                result.packetResult(true, "获取信息成功", massage);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "获取信息失败", massage);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取信息出现错误", null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }

    @RequestMapping(value = "/returnUser", method = RequestMethod.GET)
    @ApiOperation(value = "高流量用户信息", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mode", value = "(选填)上行还是下行", paramType = "query", dataType = "String")
    })
    public void returnUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> massage;
        String mode = request.getParameter("mode");
        String per = request.getParameter("per");
        double k;
        if (per.equals("0.1%")) {
            k = 0.001;
        } else if (per.equals("1%")) {
            k = 0.01;
        } else {
            k = 0.1;
        }
        massage = ponResourceService.returnUser(mode, k);
        try {
            if (massage.size() != 0) {
                result.packetResult(true, "获取信息成功", massage);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "获取信息为空", massage);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取信息出现错误", null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }


    @RequestMapping(value = "/homeMassage", method = RequestMethod.GET)
    @ApiOperation(value = "首页分区信息", response = Result.class)
    public void homeMassage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> massage;
        try {
            massage = ponResourceService.homeMassage();
            if (massage.size() != 0) {
                result.packetResult(true, "获取信息成功", massage);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "获取信息失败", massage);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取信息出现错误", null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }


    @RequestMapping(value = "/pon_type", method = RequestMethod.GET)
    @ApiOperation(value = "PON口信息", response = Result.class)
    public void pon_type(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> massage;
        String department = request.getParameter("department");
        String station = request.getParameter("station");
        massage = ponResourceService.pon_type(department, station);
        try {
            if (massage.size() != 0) {
                result.packetResult(true, "获取信息成功", massage);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "获取信息失败", massage);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取信息出现错误", null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }

    @RequestMapping(value = "/trafficMassage", method = RequestMethod.GET)
    @ApiOperation(value = "流量信息", response = Result.class)
    public void trafficMassage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> massage = new ArrayList<>();
        Map<String, Object> temp;
        temp = ponResourceService.trafficMassage("200M");
        massage.add(temp);
        temp = ponResourceService.trafficMassage("300M");
        massage.add(temp);
        temp = ponResourceService.trafficMassage("500M");
        massage.add(temp);
        try {
            if (massage.size() != 0) {
                result.packetResult(true, "获取信息成功", massage);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "获取信息失败", massage);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取信息出现错误", null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }


    @RequestMapping(value = "/downloadTable", method = RequestMethod.GET)
    @ApiOperation(value = "流量信息", response = Result.class)
    public void downloadTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("name");
        String suffix = request.getParameter("suffix");
        List<Map<String, Object>> massage = ponResourceService.downloadTable(name, suffix);
        try {
            if (massage.size() != 0) {
                result.packetResult(true, "获取信息成功", massage);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "获取信息失败", massage);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取信息出现错误", null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }

    @RequestMapping(value = "/departmentMassage", method = RequestMethod.GET)
    @ApiOperation(value = "分公司信息", response = Result.class)
    public void departmentMassage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<Object> massage = ponResourceService.departmentMassage();
            if (massage.size() != 0) {
                result.packetResult(true, "获取信息成功", massage);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "获取信息失败", massage);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取信息出现错误", null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }

    @RequestMapping(value = "/stationMassage", method = RequestMethod.GET)
    @ApiOperation(value = "局站信息", response = Result.class)
    public void stationMassage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String department = request.getParameter("department");
            List<Object> massage = ponResourceService.stationMassage(department);
            if (massage.size() != 0) {
                result.packetResult(true, "获取信息成功", massage);
                JsonResult.toJson(result, response);
            } else {
                result.packetResult(false, "获取信息失败", massage);
                JsonResult.toJson(result, response);
            }
        } catch (Exception e) {
            result.packetResult(false, "获取信息出现错误", null);
            JsonResult.toJson(result, response);
            response.setStatus(400);
        }
    }



}
