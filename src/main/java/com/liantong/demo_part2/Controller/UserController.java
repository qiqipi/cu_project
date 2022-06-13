package com.liantong.demo_part2.Controller;

import com.liantong.demo_part2.Entity.TimeStamp;
import com.liantong.demo_part2.Entity.User;
import com.liantong.demo_part2.Service.UserService;
import com.liantong.demo_part2.Utils.JsonResult;
import com.liantong.demo_part2.Utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


@Api(tags="用户管理的接口")
@Controller
@RequestMapping("/Access")
public class UserController {

    /**
     * 是否考虑加锁的问题。或者说分情况讨论，对于用户权限操作，我们默认并发量几乎不存在，不会出现数据不同步的问题，
     * 因此使用同一的一个对象来封装返回数据即可。
     */
    private Result result=new Result();

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/AddUser",method = RequestMethod.GET)
    @ApiOperation(value = "输入用户名和密码，后台查询是否存在用户，如果不存在，则新增普通用户",response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "(必填)用户名",paramType = "query",required = true,dataType = "String",defaultValue="testUser"),
            @ApiImplicitParam(name = "password",value = "(必填)密码",paramType = "query",required = true,dataType = "String",defaultValue="testpassword")
    })
    @ResponseBody
    public void AddUser(@RequestParam String username, @RequestParam String password,
                      HttpServletRequest request, HttpServletResponse response)throws Exception{
        if(StringUtils.isEmpty(username) | StringUtils.isEmpty(password)){
            result.setSuccess(false);
            result.setMsg("用户名或密码为空，请重新输入");
            result.setData(null);

            JsonResult.toJson(result, response);
        }
        else {
            User user = userService.getUserByName(username);
            System.out.println("user :"+user);
            if (user != null ){
                result.setSuccess(false);
                result.setMsg("当前用户名已存在，请重新添加");
                result.setData(null);
                JsonResult.toJson(result, response);
            }
            else {
                User user1 = new User(username,password);
                user1.setIs_admin("n");
                user1.setLevel("second");
                int temp = userService.addUser(user1);
                if(temp == 1){
                    result.setSuccess(true);
                    result.setMsg("添加成功");
                    result.setData(null);
                }
                else {
                    result.setSuccess(true);
                    result.setMsg("添加失败");
                    result.setData(null);
                }
                JsonResult.toJson(result, response);
            }
        }
    }

    @RequestMapping(value = "/user/AlterUser",method = RequestMethod.GET)
    @ApiOperation(value = "输入用户名和密码，更改后台对应id的用户，如果不存在则不操作",response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_id",value = "(必填)user_id",paramType = "query",required = true,dataType = "int",defaultValue="4"),
            @ApiImplicitParam(name = "username",value = "(必填)用户名",paramType = "query",required = true,dataType = "String",defaultValue="testUser2"),
            @ApiImplicitParam(name = "password",value = "(必填)密码",paramType = "query",required = true,dataType = "String",defaultValue="testpassword2")
    })
    @ResponseBody
    public void AlterUser(@RequestParam String username, @RequestParam String password,@RequestParam Integer user_id,
                        HttpServletRequest request, HttpServletResponse response)throws Exception{
        if(StringUtils.isEmpty(username) | StringUtils.isEmpty(password)){
            result.setSuccess(false);
            result.setMsg("用户名或密码为空，请重新输入");
            result.setData(null);
            JsonResult.toJson(result, response);
        }
        else {
            if(userService.getUserByUser_id(user_id) == null){
                result.setSuccess(false);
                result.setMsg("此用户id在数据库中不存在");
                result.setData(null);
            }
            else {
                User user = new User(user_id,username,password);
                int temp = userService.AlterUserInfo(user);
                if(temp == 1){
                    result.setSuccess(true);
                    result.setMsg("修改成功");
                    result.setData(null);
                }
                else {
                    result.setSuccess(true);
                    result.setMsg("修改失败");
                    result.setData(null);
                }
            }
            JsonResult.toJson(result, response);
        }
    }
    @RequestMapping(value = "/user/AllUser",method = RequestMethod.GET)
    @ApiOperation(value = "查询所有用户信息",response = Result.class)
    @ResponseBody
    public void AllUser( HttpServletRequest request, HttpServletResponse response)throws Exception{
        List<User> userlist = userService.AllUser();
        result.setSuccess(true);
        result.setMsg("返回成功");
        result.setData(userlist);
        JsonResult.toJson(result, response);


    }

    @RequestMapping(value = "/user/DeleteUser",method = RequestMethod.GET)
    @ApiOperation(value = "输入用户名,后台对应的用户，如果不存在则不操作",response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "(必填)用户名",paramType = "query",required = true,dataType = "String",defaultValue = "testUser2"),
    })
    @ResponseBody
    public void DeleteUser(@RequestParam String username,
                          HttpServletRequest request, HttpServletResponse response)throws Exception{
        User user = userService.getUserByName(username);
        if(user == null){
            result.setSuccess(false);
            result.setMsg("传入用户名有误，请重新尝试");
            result.setData(null);
        }
        else {
            int temp = userService.deleteUserByName(username);
            if(temp == 1){
                result.setSuccess(true);
                result.setMsg("删除成功");
                result.setData(null);
            }
            else {
                result.setSuccess(true);
                result.setMsg("删除失败");
                result.setData(null);
            }
        }
        JsonResult.toJson(result, response);
    }





    @RequestMapping(value = "/NotAdmin",method = RequestMethod.GET)
    @ApiOperation(value = "Session中判定当前用户非管理员时，不允许操作，跳转到此接口，设置统一返回信息。",response = Result.class)
//    @ResponseBody
    public void NotAdmin(HttpServletRequest request, HttpServletResponse response)throws Exception{
        System.out.println("转移到NotAdmin模块");
        result.setSuccess(false);
        result.setMsg("当前用户非管理员，不允许设置，请先登陆管理员用户。");
        result.setData(null);
        response.setStatus(401);
        JsonResult.toJson(result, response);
    }

    @RequestMapping(value = "/testReturnMap")
    @ResponseBody
    public void testTemp(HttpServletRequest request, HttpServletResponse response)throws Exception{
        List<Map<String,Object>> data = userService.testReturnMap();
        result.setSuccess(true);
        result.setMsg("success.");
        result.setData(data);
        JsonResult.toJson(result, response);
    }

    @RequestMapping(value = "/modifyTime")
    @ResponseBody
    public void modifyTime(HttpServletRequest request, HttpServletResponse response)throws Exception{
        String note = request.getParameter("note");
        String modifyTime=userService.getModifyTime(note);

        String time="_"+modifyTime;
        System.out.println(time);
        TimeStamp.setTimeFlag(time);

        result.packetResult(true, "更新成功", null);
        JsonResult.toJson(result, response);
    }
}
