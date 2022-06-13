package com.liantong.demo_part2.Mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PONMapper {

    /**
     * 样例接口，返回元素为单一map格式的list。
     * @return
     */
    List<Map<String,Object>> testReturnMap();


    /**
     * 默认1000M用户流量表、500M用户流量表和所有高流量用户的流量表数据分开查询
     * 返回1000M用户表的区域信息
     */
    List<Map<String,Object>> returnDepartmentOf_1000M_Table(String timeFlag);


    /**
     * 默认500M用户流量表、500M用户流量表和所有高流量用户的流量表数据分开查询
     * 返回500M用户表的区域信息
     */
    List<Map<String,Object>> returnDepartmentOf_500M_Table(String timeFlag);


    /**
     * 默认500M用户流量表、500M用户流量表和所有高流量用户的流量表数据分开查询
     * 返回500M用户表的区域信息
     */
    List<Map<String,Object>> returnDepartmentOf_HighDemand_Table(String timeFlag);

    /**
     * 传入1000M用户表的区域信息，返回OLT名称列表
     */
    List<Map<String,Object>> returnOLTNameOfOf_1000M_Table(String department,String timeFlag);

    /**
     * 传入500M用户表的区域信息，返回OLT名称列表
     */
    List<Map<String,Object>> returnOLTNameOfOf_500M_Table(String department,String timeFlag);

    /**
     * 传入1000M用户表的区域信息，返回OLT名称列表
     */
    List<Map<String,Object>> returnOLTNameOfOf_HighDemand_Table(String department,String timeFlag);


    /**
     * 传入1000M用户表的区域信息，分公司信息，OLT名称信息，返回PON_port信息列表
     */
    List<Map<String,Object>> returnPON_PortNameOf_1000M_Table(String department,String OLT_name,String timeFlag);

    /**
     * 传入500M用户表的区域信息，分公司信息，OLT名称信息，返回PON_port信息列表
     */
    List<Map<String,Object>> returnPON_PortNameOf_500M_Table(String department,String OLT_name,String timeFlag);

    /**
     * 传入所有高流量用户表的区域信息，分公司信息，OLT名称信息，返回PON_port信息列表
     */
    List<Map<String,Object>> returnPON_PortNameOf_HighDemand_Table(String department,String OLT_name,String timeFlag);


    /**
     * 1000M用户表统计
     *输入区信息（department），OLT信息，PON板信息，PON口信息，
     * 返回非10G用户数
     */
    List<Map<String,Object>> return_1000MTable_Not10G_userNum(String department,String OLT_name,String board,String port,String timeFlag);

    /**
     * 1000M用户表统计
     *输入区信息（department），OLT信息，PON板信息，PON口信息，
     * 返回10G用户数
     */
    List<Map<String,Object>> return_1000MTable_10G_userNum(String department,String OLT_name,String board,String port,String timeFlag);




    /**
     * 1000M用户表统计
     * 输入区信息（department），OLT信息
     * 返回OLT总数，新OLT数，旧OLT数，厂家数
     */

    List<Map<String,Object>> return_1000MTable_Not10G_OLTNum(String department,String OLT_name,String timeFlag);

    /**
     * 1000M用户表统计
     * 输入区信息（department），OLT信息，
     * 返回非10G、10PON板数
     */

    Integer return_1000MTable_Not10G_Pon_BoardNum(String department,String OLT_name,String timeFlag);

    Integer return_1000MTable_10G_Pon_BoardNum(String department,String OLT_name,String timeFlag);


    /**
     * 1000M用户表统计
     * 输入区信息（department），OLT信息，
     * 返回非10G、10GPON口数
     */
    Integer return_1000MTable_Not10G_Pon_PortNum(String department,String OLT_name,String timeFlag);

    Integer return_1000MTable_10G_Pon_PortNum(String department,String OLT_name,String timeFlag);

    /**
     *1000M用户表统计
     * 输入区信息（department），OLT信息，
     * 返回满足筛选条件的非10GPON表格所有数据
     */

    List<Map<String,Object>> return_1000M_table_not_10G(String department,String OLT_name,String timeFlag);


    /**
     *1000M用户表统计
     * 输入区信息（department），OLT信息，
     * 返回满足筛选条件的10GPON表格所有数据
     */
    List<Map<String,Object>> return_1000M_table_10G(String department,String OLT_name,String timeFlag);

    /**
     * 筛选出所有的department名称
     */
    List<Map<String,Object>> return_1000M_table_All_Department(String timeFlag);

    /**
     * 筛选出所有的department名称
     */
    List<Map<String,Object>> return_1000M_table_All_OLTName(String department,String timeFlag);

//    void update_result_time_and_operator(String time, String operator);
//
//    Map<String,Object> get_factors();





}
