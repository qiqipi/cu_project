package com.liantong.demo_part2.Mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PONResourceMapper {


    /**
     * OLT槽位资源利用率统计
     */
//    List<Map<String, Object>> PON_board_rate(String department, String olt, String timeFlag);
    int returnBoardNum(String department, String olt, String timeFlag);

    int returnTotalNum(String department, String olt, String timeFlag);


    /**
     * PON端口利用率统计（统计当前已使用PON板中的PON口利用率情况）
     */

    Map<String, Object> PON_port_rate(String department, String olt, String timeFlag);

    List<Map<String, Object>> PON_Port_Used(String timeFlag, String department,String station,  String olt);

    /**
     * 返回所有区域信息
     */
    List<String> returnDepartment(String timeFlag);

    /**
     * 返回某个区域的所有OLT信息
     */
    List<String> returnOLT(String department, String timeFlag);

    /**
     * 返回某个区域的所有树状图中OLT的种类和数量
     */
    List<Map<String, Object>> OLTTree(String department, String timeFlag);

    /**
     * 返回首页信息
     */
    List<Map<String, Object>> homeMassage(String timeFlag);

    /**
     * 返回某个区域高流量用户信息
     */
    List<Map<String, Object>> returnUser(String timeFlag, String mode,  String count);

    /**
     * 返回PON信息
     */
    List<Map<String, Object>> pon_type(String timeFlag, String department, String station);


    /**
     * 返回某个区域树状图中某个OLT下的10G和1GPON板数
     */
    Map<String, Object> PONBoardTree(String department, String OLTName, String timeFlag);

    /**
     * 返回某个区域树状图中某个OLT下的10G和1GPON口数和下挂用户数
     */
    Map<String, Object> totalTree(String department, String OLTName, String timeFlag);

    /**
     * 返回某个套餐的各区人数
     */
    List<Double> countTraffic(String speed);

    /**
     * 返回某个套餐的各区人数
     */
    List<Double> countAvg(String speed);

    /**
     * 返回某个套餐的各区人数
     */
    List<Double> countPeak(String speed);

    /**
     * 返回时间坐标
     */
    List<String> countDate(String speed);


    /**
     * 测试时间戳功能
     */
    List<Map<String, Object>> returnTimeByTable(String time);

    /**
     * 测试时间戳功能
     */
    List<Map<String, Object>> returnTimeByFiled1(String time);

    List<Map<String, Object>> returnTimeByFiled2(String time);

    /**
     * 返回首页地图信息
     */
    List<Map<String, Object>> returnMap();

    /**
     * 返回首页地图中的所有OLT信息
     */
    List<String> mapOLT();


    List<Object> departmentMassage();

    List<Object> stationMassage(String department);

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
     * 返回首页天津和平区原始信息地址数量
     *
     * @return
     */
    List<Map<String, Object>> returnOldTianjinNumber();

    /**
     * 返回首页纠偏天津和平区原始信息地址数量
     *
     * @return
     */
    List<Map<String, Object>> returnNewTianjinNumber();


    /**
     * 保存首页地图天津纠偏信息
     *
     * @return
     */
    void returnRectification(String name, String lng, String lat);

    /**
     * 清空new_tianjin_hepin_map表中数据
     */
    void truncateTable();

    List<String> returnVender(String department, String station, String timeFlag);

    List<String> returnTypes(String department, String station, String timeFlag);

    List<Map<String, Object>> returnVenderMassage(String vender, String department, String station, String timeFlag);

    List<Map<String, Object>> returnIPTVMassage(String type, String department, String station, String olt, String timeFlag);

    List<Map<String, Object>> userType(String type, String department, String station, String timeFlag);

    List<String> returnModel(String department, String station, String timeFlag);

    int countUser(String timeFlag);

    List<Map<String, Object>> returnModelMassage(String model, String department, String station, String timeFlag);

    List<Map<String, Object>> returnUserMassage(int count, String department, String timeFlag);

    List<Map<String, Object>> downloadTable(String name, String suffix);
}
