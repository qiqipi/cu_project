package com.liantong.demo_part2.Service;

import java.util.List;
import java.util.Map;

public interface PONResourceService {

    /**
     * OLT槽位利用率
     * 统计不同分公司、OLT下的PON板资源利用率（槽位利用率）
     */
    Map<String, Object> PON_board_rate(String deparment, String olt, String time);


    /**
     * PON端口资源利用率
     * 统计不同分公司、OLT下的PON口资源利用率
     */
    Map<String, Object> PON_port_rate(String department, String olt, String time);

    List<Map<String, Object>> PON_Port_Used(String department, String station, String olt);

    /**
     * PON端口速率分布
     * 统计200M，300M，500PON口速率分布
     */
    Map<String, Object> trafficMassage(String speed);


    /**
     * 统计全部区域信息
     */
    List<String> returnDepartment();

    /**
     * 统计某个区域的所有OLT信息
     */
    List<String> returnOLT(String department);

    /**
     * 返回树状图所有信息
     *
     * @param department
     * @return
     */
    Map<String, Object> returnTree(String department);

    /**
     * 测试时间戳功能
     */
    List<Map<String, Object>> returnTime(String time, String option);

    /**
     * 返回首页地图信息
     */
    List<List<Object>> returnMap();

    /**
     * 返回首页地图天津和平区信息
     *
     * @return
     */
    List<Map<String, Object>> returnTianjin();

    /**
     * 返回首页地图纠偏后天津和平区信息
     *
     * @return
     */
    List<Map<String, Object>> returnNewTianjin();

    /**
     * 返回首页地图天津和平区新老信息数量
     *
     * @return
     */
    List<Map<String, Object>> returnTianjinNumber();

    /**
     * 保存首页地图天津纠偏信息
     *
     * @return
     */
    boolean returnRectification(String name, String lng, String lat);

    /**
     * 清空new_tianjin_hepin_map表中数据
     */
    void truncateTable();

    List<Map<String, Object>> homeMassage();

    List<Map<String, Object>> returnVenderMassage(String department, String station);

    List<Map<String, Object>> returnIPTVMassage(String department, String station,String olt);

    List<Map<String, Object>> userType(String department, String station);

    List<Map<String, Object>> returnModelMassage(String department, String station);

    List<Map<String, Object>> pon_type(String department, String station);

    List<Map<String, Object>> returnUserMassage(String department);

    List<Map<String, Object>> returnUser(String mode, double k);

    List<Object> departmentMassage();

    List<Object> stationMassage(String department);

    List<Map<String, Object>> downloadTable(String name, String suffix);


}
