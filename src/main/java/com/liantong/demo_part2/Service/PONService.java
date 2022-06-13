package com.liantong.demo_part2.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PONService {

    /**
     * 10G PON 板（口）下分类型用户数量分布
     * 不同套餐类型下的用户数分布
     * @return
     */
    List<Set<Map<String,Object>>> UserNumOfDifferentMeal();



    /**
     * 给出所有用户的区,传入tableIndex表示是1000M用户表的区域、还是500M用户表的区域，还是全高流量用户表的区域
     * tableIndex选值有三，1000M,500M,all.
     */
    List<Map<String,Object>> returnDepartmentOf_Table(String tableIndex);



    /**
     * 给出所有用户的区,传入tableIndex表示是1000M用户表的区域、还是500M用户表的区域，还是全高流量用户表的区域
     * tableIndex选值有三，1000M,500M,all.
     */
    List<Map<String,Object>> returnOLTNameOf_Table(String tableIndex,String department);



    /**
     * 给出所有用户的区,传入tableIndex表示是1000M用户表的区域、还是500M用户表的区域，还是全高流量用户表的区域
     * tableIndex选值有三，1000M,500M,all.
     */
    List<Map<String,Object>> returnPON_PortNameOf_Table(String tableIndex,String department,String OLT_name);


    /**
     * 返回选定区域的用户数，包括10G和非10G用户
     */
    Map<String,Object> return_1000MTable_userNum(String department,String OLT_name,String board, String port);


    /**
     *
     */
    Map<String,Object> return_1000MTable_Not10G_OLTNum(String department,String OLT_name);


    /**
     * 返回10G、非10G的Pon板数,Pon口数
     */

    Map<String,Object> return_1000MTable_10G_PonNum(String department,String OLT_name);

    /**
     * 封装请求传入参数
     */
    List<Map<String,Object>> return_request_parms(String tableIndex,String department,String OLT_name,String board, String port);

    /**
     *
     */
    Map<String,List<Map<String,Object>>> return_1000M_Table(String tableIndex,String department,String OLT_name);


    /**
     * 若一级菜单定位到department，则查询二级菜单下的所有OLT名称
     * 若一级菜单定位到天津，则查询二级菜单下的所有区名称
     *
     */
    List<String> second_Object_Name_List(Map<String,String> map);








}
