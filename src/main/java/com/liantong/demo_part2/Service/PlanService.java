package com.liantong.demo_part2.Service;

import java.util.List;
import java.util.Map;

public interface PlanService {
    /**
     **有注释的为目前使用的方法。
     */

    List<Map<String,Object>> testSelect(String sql);


    // 检查sql语法，不能包含insert、update、select、alert等关键字
    boolean check_sql_grammar(String sql);
    // 判断传入表名是否是基础数据表。
    String split_sql(String sql);



    //获取当前时间时间
    String get_time();

    // 在结果表中插入当前选择的查询数据（包括时间和操作人，但不打分）
    int select_data_insert_inot_result(String sql,String time,String operator);

    // 更新传入的权重数据
    void update_factor_weight(String meal_weight,String flow_weight,String count_weight);
    // 进行打分，并返回打分数据的结果
    List<Map<String, Object>> Scoring_and_get_result(String sql,String time,String operator);


    Map<String, Object> plan(String []station_list,String operator,String operate_time,String if_necessary_selected,int nums);

    List<Map<String,Object>> get_department_of_scored(String time);

    List<Map<String,Object>> get_station_of_scored(String department, String time);

    List<String> selectAllStation();







//    List<Map<String, Object>> Scoring_PON(String department, String olt, String user_name,String time);
//    List<Map<String, Object>> Scoring_PON_by_time(String start_time,String end_time,String user_name);
//    List<Map<String,Object>> Scoring_and_return(String sql,String time,String user_name);
//    String packet_new_sql (String sql,String name,String time);




}
