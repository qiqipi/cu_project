package com.liantong.demo_part2.Controller;

import com.liantong.demo_part2.Service.PON2022MigrationService;
import com.liantong.demo_part2.Utils.JsonResult;
import com.liantong.demo_part2.Utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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


    @RequestMapping(value = "/createOltChosen",method = RequestMethod.GET)
    @ApiOperation(value = "创建OLTChosen(表一)", response = Result.class)
    @ResponseBody
    public void createOLTChosenTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag;
        try {
            flag = pon2022MigrationService.createOLTChosenTable();
            if(flag){
                result.packetResult(true, "OLTChosen(表一)建立成功",flag,"204");
                JsonResult.toJson(result, response);
            }
        }catch (Exception e){
            System.out.println(e);
            result.packetResult(false, "OLTChosen(表一)建立过程错误","20000");
            JsonResult.toJson(result, response);
        }
    }

    @RequestMapping(value = "/selectOltChosen",method = RequestMethod.GET)
    @ApiOperation(value = "获取OLTChosen(表一)")
    @ResponseBody
    public Result getAaaTable(){
        List<Map<String, Object>> OLTChosenTable = pon2022MigrationService.getOLTChosenTable();
        Result<List<Map<String, Object>>> listResult = new Result(true,OLTChosenTable,"获取数据成功","200");
        return listResult;
    }
}
