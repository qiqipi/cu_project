package com.liantong.demo_part2.Controller;

import com.liantong.demo_part2.Service.TestService;
import com.liantong.demo_part2.Utils.JsonResult;
import com.liantong.demo_part2.Utils.Result;
import io.swagger.annotations.Api;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/Access")
@Api(tags="这部分用于请求转发，完成java接口到python后台映射的过程。前端不需要看")
public class TestController {

    private Result result=new Result();
    @Autowired
    private TestService testService;

    @RequestMapping("/firstTest")
    public void test1(HttpServletRequest request, HttpServletResponse response) throws Exception{
        try{
            List<Object> list = testService.listAll();
            result.setSuccess(true);
            result.setData(list);
        }
        catch (Exception e){
            result.setSuccess(false);
            result.setData(null);
            System.out.println(e.toString());
        }
        finally {
            JsonResult.toJson(result, response);
        }

    }

    /**
     * java http请求，用于拦截所有静态界面发送的请求，经过处理后转发给python后台
     * python后台默认是使用get方式请求，因此转发实现get方式。
     * 关于数据传输的问题，如何将请求得到的数据结果返回给java，java得到结果怎么再返回给前端。
     */

    @RequestMapping("/RequestRedirectToPython")
    @ResponseBody
//
//                                          ,@RequestParam(name = "name") String name
    public String RequestRedirectToPython(HttpServletRequest request, HttpServletResponse response)throws Exception{


        System.out.println("name in RequestRedirectToPython :"+request.getAttribute("name")+"============");
        String redirectPath = "http://localhost:8880"+request.getAttribute("name");
        System.out.println("redirectPath :"+redirectPath);
        String pythonResult = "null";
        // 创建httpClient实例对象
            HttpClient httpClient = new HttpClient();
            // 设置httpClient连接主机服务器超时时间：15000毫秒
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
            // 创建post请求方法实例对象
            GetMethod getMethod = new GetMethod(redirectPath);
            // 设置post请求超时时间
            getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
            getMethod.addRequestHeader("Content-Type", "application/json");
            httpClient.executeMethod(getMethod);

            pythonResult = getMethod.getResponseBodyAsString();
            getMethod.releaseConnection();

        System.out.println(pythonResult+"++++++++++");
        return pythonResult;
    }
    /**
     * 使用过拦截器拦截原始请求，拆分后重定向到某个方法中并不行。因为请求发送后，
     * 路径映射优先于拦截器捕获。因此会直接显示访问不到路径
     */



    /**
     * 用户类mapper测试
     */
    @RequestMapping("/test3")
    @ResponseBody
    public String test3(){
        testService.AllUser();
        return "success";
    }

    /**
     * 过滤器测试
     */
    @RequestMapping("/test4")
    @ResponseBody
    public String test4(){
        return "success444";
    }

    /**
     * 过滤器测试
     */
    @RequestMapping("/test5")
    @ResponseBody
    public String test5(){
        return "success555";
    }

}
