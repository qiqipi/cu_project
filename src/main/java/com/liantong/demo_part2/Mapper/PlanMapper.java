package com.liantong.demo_part2.Mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PlanMapper {


    /**
     * 封装的查询语句
     */
    List<Map<String, Object>> testSelect(String sql);


    /**
     * 在结果表中插入当前选择的查询数据（包括时间和操作人，但不打分）
     *
     * @param sql
     * @return
     */
    int select_data_insert_inot_result(String sql, String timeFlag);

    /**
     * 更新传入的权重数据
     *
     * @param meal_weight
     * @param flow_weight
     * @param count_weight
     */
    void update_factor_weight(String meal_weight, String flow_weight, String count_weight, String timeFlag);

    /**
     * 获取当前的权重数据
     *
     * @return
     */
    Map<String, Object> get_factors(String timeFlag);


    /**
     * 打分
     *
     * @param sql
     */
    void update_score(String sql);

    /**
     * 局站下所有OLT，局站，型号，厂家
     */
    List<Map<String, Object>> find_olt_info_by_station(String station, String timeFlag);

    List<Map<String, Object>> select_pon_data_by_olt_name(String olt_name, String timeFlag);

    List<Map<String, Object>> select_pon_port_data_by_olt_name(String olt_name, String timeFlag);

    List<Map<String, Object>> select_score_result_by_station(String station, String operator, String date_time, String timeFlag, int nums);

    List<Map<String, Object>> select_olt_state_by_station(String station);

    /**
     * 把PON板、port使用情况插入新表
     */
    int insert_into_plan_table(String olt_name, String board_str, String port_str, String station);

    /**
     * 获取打分结果表的所有区域信息
     *
     * @return
     */
    List<Map<String, Object>> get_department_of_scored(String time, String timeFlag);

    /**
     * 传如区域，获取打分结果表的所有局站信息
     *
     * @param department
     * @return
     */
    List<Map<String, Object>> get_station_of_scored(String department, String time, String timeFlag);


    /**
     * 获取所有局站信息
     *
     * @param
     * @return
     */
    List<String> selectAllStation(String timeFlag);


}
