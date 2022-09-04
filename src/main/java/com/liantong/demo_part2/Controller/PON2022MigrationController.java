package com.liantong.demo_part2.Controller;

import com.liantong.demo_part2.Mapper.PON2022MigrationMapper;
import com.liantong.demo_part2.Service.PON2022MigrationService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author qiqipi
 * @create 2022/7/10 5:54
 */
@Api(tags = "PON迁移部分的接口")
@Controller
@RequestMapping("/Access/Migration")
public class PON2022MigrationController {

    private Result result = new Result();

    @Autowired
    private PON2022MigrationService pon2022MigrationService;



    @RequestMapping(value = "/merge",method = RequestMethod.GET)
    @ApiOperation(value = "创建中间操作表和规划表", response = Result.class)
    @ResponseBody
    public void createMergeTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag;
        try {
            flag = pon2022MigrationService.createMergeTable();
            if(flag){
                result.packetResult(true, "中间表建立成功",flag,"204");
                JsonResult.toJson(result, response);
            }
        }catch (Exception e){
            System.out.println(e);
            result.packetResult(false, "中间表建立过程错误","20000");
            JsonResult.toJson(result, response);
        }

    }


    @RequestMapping(value = "/getRegion",method = RequestMethod.GET)
    @ApiOperation(value = "获得地区信息", response = Result.class)
    @ResponseBody
    public Result getRegion(){
        Result<List<Map<String, Object>>> listResult;
        List<Map<String, Object>> region = pon2022MigrationService.getRegion();
        if(region.isEmpty()){
            return new Result(false,null,"创建表格失败","400");
        }else{
            listResult = new Result(true,region,"获取数据成功","200");
        }
        return listResult;
    }

    @RequestMapping(value = "/getOltChosen",method = RequestMethod.GET)
    @ApiOperation(value = "获取OLTChosen(表一)")
    @ResponseBody
    public Result getAaaTable(){
        boolean flag;
        try {
            flag = pon2022MigrationService.createOLTChosenTable();
        }catch (Exception e){
            System.out.println(e);
            return new Result(false,null,"创建表格失败","400");
        }
        List<Map<String, Object>> OLTChosenTable = pon2022MigrationService.getOLTChosenTable();
        Result<List<Map<String, Object>>> listResult = new Result(true,OLTChosenTable,"获取数据成功","200");
        return listResult;
    }

    @RequestMapping(value = "/getPlanTable",method = RequestMethod.GET)
    @ApiOperation(value = "获取PlanTable(表二)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "OLT_names",value = "（必填）OLT_names",paramType = "query",required = true,type = "String",allowMultiple = true,defaultValue = "HPXXL_HW_OLT13,NKHMJ_HW_OLT07"),
            @ApiImplicitParam(name = "time",value = "（必填）time",paramType = "query",required = true,type = "String",allowMultiple = true,defaultValue = "2019-02-12fafaffd,2022-02-2222fafaffd")
    })

    @ResponseBody

    public Result getPlanTable(@RequestParam String[] OLT_names,@RequestParam String[] time){
        boolean flag;
        try {
            flag = pon2022MigrationService.createPlanTable(time);
        }catch (Exception e){
            System.out.println(e);
            return new Result(false,null,"创建表格失败","400");

        }
        List<Map<String, Object>> OLTPlanTable = pon2022MigrationService.getPlanTable(OLT_names);
        Result<List<Map<String, Object>>> listResult = new Result(true,OLTPlanTable,"获取数据成功","200");

        return listResult;
    }

    @RequestMapping(value = "/getRankTable",method = RequestMethod.GET)
    @ApiOperation(value = "获取排序表(表三)")
    @ApiImplicitParam(name = "values",value = "(必填)values",paramType = "query",required = true,type = "String",defaultValue = "0.1,0.2,0.3,0.4")
    @ResponseBody
    public Result getRankTable(@RequestParam String[] values){
        List<Map<String, Object>> rank_table = null;
        try {
            rank_table = pon2022MigrationService.getRank_table(values);
        } catch (Exception e) {
            System.out.println(e);
            return new Result(false,null,"排序失败","404");
        }
        return new Result(true,rank_table,"获取数据成功","200");
    }

    @RequestMapping(value = "/getMigrationTable",method = RequestMethod.GET)
    @ApiImplicitParam(name = "values",value = "(必填)values",paramType = "query",required = true,type = "String",defaultValue = "18,6,14,8,16")
    @ResponseBody
    @ApiOperation(value = "获取迁移结果")
    public Result getMigrationTable(@RequestParam String[] values){
        List<Map<String ,Object>> migrationTable = null;
        try {
            migrationTable = pon2022MigrationService.getMigrationTable(values);
        }catch (Exception e){
            System.out.println(e);
            return new Result(false,null,"排序失败","404");
        }
        return new Result(true,migrationTable,"获取数据成功","200");
    }


    @RequestMapping(value = "/getTable2Fields",method = RequestMethod.GET)
    @ApiOperation(value = "获取设置权重的字段")
    @ResponseBody
    public String[] getChosenFields(){
        String s[] = new String[8];
        s[0] = "通道1流入峰值最大值";
        s[1] = "通道2流入峰值最大值";
        s[2] = "通道1均值最大值";
        s[3] = "通道2均值最大值";
        s[4] = "通道1预测流量最大值";
        s[5] = "通道2预测流量最大值";
        s[6] = "通道1趋势线";
        s[7] = "通道2趋势线";
        return s;
    }

}
