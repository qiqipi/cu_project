package com.liantong.demo_part2.Controller;

import com.liantong.demo_part2.Entity.User;
import com.liantong.demo_part2.Service.TestService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "登陆部分的接口")
@Controller
@RequestMapping("/Access")
public class LoginController {

    /**
     * 是否考虑加锁的问题。对于登陆权限操作，如果后面需要多地多设备登陆，
     * 则应修改成每个方法内返回一个独立的数据封装对象。如果单点登陆不考虑并发，则不用修改。
     */
    private Result result = new Result();

    @Autowired
    private TestService testService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ApiOperation(value = "输入用户名和密码，后台查询是否存在用户，并返回对应数值", response = Result.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "(必填)用户名", paramType = "query", required = true, dataType = "String", defaultValue = "admin"),
            @ApiImplicitParam(name = "password", value = "(必填)密码", paramType = "query", required = true, dataType = "String", defaultValue = "admin")
    })
    @ResponseBody
    public void login(@RequestParam String username, @RequestParam String password, HttpSession session,
                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtils.isEmpty(username) | StringUtils.isEmpty(password)) {
            result.setSuccess(false);
            result.setMsg("用户名或密码为空，请重新输入");
            result.setData(null);
            response.setStatus(400);
            JsonResult.toJson(result, response);
        } else {
            User user = testService.getUserByName(username);
//            System.out.println("user :"+user);
            if (user == null || (!password.equals(user.getPassword()))) {
                result.setSuccess(false);
                result.setMsg("用户名或密码错误，请重新输入");
                result.setData(null);
                response.setStatus(400);
                JsonResult.toJson(result, response);
            } else {
                request.getSession().setAttribute("user", user);
//                session.setAttribute("user",user);
                List<Object> list = new ArrayList<>();
                Map<String, String> sessionmap = new HashMap<>();
                sessionmap.put("sessionId", username + "_" + session.getId());
                list.add(sessionmap);
//                System.out.println(session.getId());
                result.setSuccess(true);
                result.setMsg("登陆成功");
                result.setData(list);
                JsonResult.toJson(result, response);
            }
        }
    }

    @RequestMapping(value = "/goLogin", method = RequestMethod.GET)
    @ApiOperation(value = "Session中未查询到用户时，跳转到此接口，设置统一返回信息。", response = Result.class)
    public void gologin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("转移到Login模块");
        result.setSuccess(false);
        result.setMsg("请先进行登陆");
        result.setData(null);
        response.setStatus(400);
        JsonResult.toJson(result, response);
    }

}
