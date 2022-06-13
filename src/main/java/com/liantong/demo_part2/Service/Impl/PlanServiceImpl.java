package com.liantong.demo_part2.Service.Impl;

import com.liantong.demo_part2.Entity.TimeStamp;
import com.liantong.demo_part2.Mapper.PlanMapper;
import com.liantong.demo_part2.Service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;


import javax.rmi.PortableRemoteObject;
import javax.swing.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanMapper planMapper;
    private static Map<String, Integer> AN5516 = new HashMap<>();
    private static Map<String, Integer> C220 = new HashMap<>();
    private static Map<String, Integer> C300 = new HashMap<>();
    private static Map<String, Integer> MA5800 = new HashMap<>();
    private static Map<String, Integer> MA5680 = new HashMap<>();
    private static Map<String, Integer> MA5600 = new HashMap<>();


    static {

//        AN5516.put("1G", 8);
//        AN5516.put("10G", 8);
//        AN5516.put("board_count", 16);
//        C220.put("1G", 4);
//        C220.put("10G", 0);
//        C220.put("board_count", 10);
//        C300.put("1G", 8);
//        C300.put("10G", 8);
//        C300.put("board_count", 16);
//        MA5800.put("1G", 16);
//        MA5800.put("10G", 8);
//        MA5800.put("board_count", 17);
//        MA5680.put("1G", 8);
//        MA5680.put("10G", 8);
//        MA5680.put("board_count", 18);
//        MA5600.put("1G", 8);
//        MA5600.put("10G", 8);
//        MA5600.put("board_count", 16);
        AN5516.put("1000", 8);
        AN5516.put("10000", 8);
        AN5516.put("board_count", 16);
        C220.put("1000", 4);
        C220.put("10000", 0);
        C220.put("board_count", 10);
        C300.put("10000", 8);
        C300.put("10000", 8);
        C300.put("board_count", 16);
        MA5800.put("1000", 16);
        MA5800.put("10000", 8);
        MA5800.put("board_count", 17);
        MA5680.put("1000", 8);
        MA5680.put("10000", 8);
        MA5680.put("board_count", 18);
        MA5600.put("1000", 8);
        MA5600.put("10000", 8);
        MA5600.put("board_count", 16);
    }

    public String get_time() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
        String time = df.format(new Date());// new Date()为获取当前系统时间
        return time;
    }

    @Override
    public List<Map<String, Object>> testSelect(String sql) {
        List<Map<String, Object>> list = planMapper.testSelect(sql);
        return list;
    }

    public boolean check_sql_grammar(String sql) {
//        非空判定
        if (sql == null | sql.equals(""))
            return false;
//        不合法的修改数据判定
        if (sql.indexOf("update") != -1 | sql.indexOf("insert") != -1
                | sql.indexOf("delete") != -1 | sql.indexOf("alert") != -1
                | sql.indexOf("drop") != -1 | sql.indexOf("create") != -1
                | sql.indexOf("mysqldump") != -1 | sql.indexOf("source") != -1)
            return false;
        return true;
    }


    public String split_sql(String sql) {

//        判别是否存在表名，存在返回筛选条件部分的语句后缀，不存在则返回null。
        String[] str_splited = sql.split("pon_mark_parms" + TimeStamp.timeFlag);
        if (str_splited.length > 1) {
            String sql_new = str_splited[1];
            return sql_new;
        } else return null;
    }

    public String packet_new_sql(String sql, String name, String time) {
        String[] str_splited = sql.split("pon_mark_parms" + TimeStamp.timeFlag);
        if (str_splited.length == 1) {
            String sql_new = "INSERT INTO pon_mark_result" + TimeStamp.timeFlag + " select *,(3*(if(usernum_over_500m is NOT NULL,usernum_over_500m,0)/usernum_total)+(if(usernum_top10 is NULL, 0 ,usernum_top10)/usernum_total)+0.5*(if(usernum_top10 is NULL, 0 ,usernum_top10)/32))*1000 as score,";
            sql_new += "\'" + time + "\'";
            sql_new += ",\'" + name + "\'";
            sql_new += "from pon_mark_parms" + TimeStamp.timeFlag;
            return sql_new;
        } else if (str_splited.length > 1) {
            String sql_new = "INSERT INTO pon_mark_result" + TimeStamp.timeFlag + " select *,(3*(if(usernum_over_500m is NOT NULL,usernum_over_500m,0)/usernum_total)+(if(usernum_top10 is NULL, 0 ,usernum_top10)/usernum_total)+0.5*(if(usernum_top10 is NULL, 0 ,usernum_top10)/32))*1000 as score,";
            sql_new += "\'" + time + "\'";
            sql_new += ",\'" + name + "\'";
            sql_new += "from pon_mark_parms" + TimeStamp.timeFlag;
            sql_new += str_splited[1];
            return sql_new;
        } else return null;
    }


    @Override
    public int select_data_insert_inot_result(String sql, String time, String operator) {
        String sql_pre = "insert ignore into  pon_mark_result" + TimeStamp.timeFlag +
                " (olt_name,pon_port,station,bandwidth,model,vendor,up_avg,up_peak,down_avg,down_peak,department,usernum_over_500m,usernum_over_1000m,usernum_top10,usernum_total,priority,operate_time,operator)  " +
                "select olt_name,pon_port,station,bandwidth,model,vendor,up_avg,up_peak,down_avg,down_peak,department,usernum_over_500m,usernum_over_1000m,usernum_top10,usernum_total,priority, " +
                "\'" + time + "\',\'" + operator + "\'" +
                " from pon_mark_parms" + TimeStamp.timeFlag;
        System.out.println(time + "这是第一次插入result表的time");
        System.out.println("sql_pre");
        System.out.println(sql_pre);
        String[] str_list = sql.split("pon_mark_parms" + TimeStamp.timeFlag);
        String sql_final;
        if (str_list.length == 1) {
            sql_final = sql_pre;
            return planMapper.select_data_insert_inot_result(sql_final, TimeStamp.timeFlag);
        }
        //拼接传入sql中做限定的查询条件，如where等
        else if (str_list.length == 2) {
            sql_final = sql_pre + str_list[1];
            return planMapper.select_data_insert_inot_result(sql_final, TimeStamp.timeFlag);
        } else
            return -1;

    }

    @Override
    public void update_factor_weight(String meal_weight, String flow_weight, String count_weight) {
        planMapper.update_factor_weight(meal_weight, flow_weight, count_weight, TimeStamp.timeFlag);
    }

    @Override
    public List<Map<String, Object>> Scoring_and_get_result(String sql, String time, String operator) {
        Map<String, Object> factors = planMapper.get_factors(TimeStamp.timeFlag);
        System.out.println(time + "这是第二次更新result表的time");
        String sql_final = "update pon_mark_result" + TimeStamp.timeFlag + " set score = (" +
                factors.get("meal_weight").toString() + "*(if(usernum_over_500m is NOT NULL,usernum_over_500m,0)/usernum_total)+" +
                factors.get("flow_weight").toString() + "*(if(usernum_top10 is NULL, 0 ,usernum_top10)/usernum_total)+" +
                factors.get("count_weight").toString() + "*(if(usernum_top10 is NULL, 0 ,usernum_top10)/32))*1000  where operate_time=" +
                "\'" + time + "\' and operator = " + "\'" + operator + "\'";

        planMapper.update_score(sql_final);
        System.out.println(sql_final);

//        String[] sql_split = sql.split("where");
//        if(sql_split.length ==2){
//            String sql_return = "select * from 20200531_pon_mark_result where operate_time="+ "\'"+time+"\' and operator = "+"\'"+operator+"\' and"+sql_split[1];
//        }
//        else {
//            String sql_return = "select * from 20200531_pon_mark_result where operate_time="+ "\'"+time+"\' and operator = "+"\'"+operator+"\'";
//            return planMapper.testSelect(sql_return);
//        }
        String sql_return = "select * from pon_mark_result" + TimeStamp.timeFlag + " where operate_time=" + "\'" + time + "\' and operator = " + "\'" + operator + "\'";
        System.out.println(sql_return);
        return planMapper.testSelect(sql_return);
    }

    @Override
    public Map<String, Object> plan(String[] station_list, String operator, String operate_time, String if_necessary_selected, int nums) {
        List<Object> treeList = new ArrayList<>();
        List<Object> formList = new ArrayList<>();
        List<Map<String, Object>> PON_board = new ArrayList<>();
        Map<String, Object> tree = new LinkedHashMap<>();
        List<Map<String, Object>> resultTree = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        //一、遍历每个局站信息，以局站为单位做规划。粒度为局站
        for (String station : station_list) {
            //1.获取pon流量表中所有和传入局站有关的OLT数据。由于数据格式问题，无法自动去重，需要手动去重(粒度OLT)
            List<Map<String, Object>> olt_data_from_pon_table;
            olt_data_from_pon_table = planMapper.find_olt_info_by_station(station, TimeStamp.timeFlag);
            //2.去重olt_name。（保证OLT名不重复）
            List<Map<String, Object>> olt_data_from_pon_table_unique = unique_olt_name(olt_data_from_pon_table, "olt_pon_name");
            //3.统计oltPON板，PON口使用情况。
            List<Map<String, Object>> olt_state = cal_pon_board_and_port(olt_data_from_pon_table_unique, station);
            //4.筛选出打分结果表中和此局站有关的结果数据，根据传入操作人员，操作时间做限定
            List<Map<String, Object>> score_result_by_station = planMapper.select_score_result_by_station(station, operator, operate_time, TimeStamp.timeFlag, nums);
//            System.out.println(score_result_by_station);
//            System.out.println(score_result_by_station.size());
            //5.统计当前局站内所有OLT的PON板使用情况的辅助map定义
            Map<String, Object> mp = new HashMap<>();
            Integer PON_borad_used = 0;
            Integer PON_board_spare = 0;
            Integer PON_board_add = 0;
            Integer OLT_add = 0;
            mp.put("PON_borad_used", PON_borad_used);
            mp.put("PON_board_spare", PON_board_spare);
            mp.put("PON_board_add", PON_board_add);
            mp.put("OLT_add", OLT_add);
            PON_board.add(mp);
            //6.更新的打分结果添加到返回列表
            Map<String, Object> temp = update_pon_board_and_port(station, score_result_by_station, olt_state, mp, if_necessary_selected);
            treeList.add(temp.get("树状图结构"));
            formList.add(temp.get("表格结构"));
        }
        result.put("PON_board", PON_board);
        tree.put("name", "机房规划结果");
        tree.put("value", "");
        tree.put("children", treeList);
        resultTree.add(tree);
        result.put("tree", resultTree);
        result.put("form", formList);
        return result;
//        return list;
    }

    /**
     * 2.辅助去重函数,根据传入的字段名来去重
     */
    public List<Map<String, Object>> unique_olt_name(List<Map<String, Object>> olt_data_from_pon_table, String unique_field) {
        //辅助集合，去重olt_name,保证olt唯一
        Set<String> olt_name_unique_set = new HashSet<>();
        List<Map<String, Object>> olt_data_from_pon_table_unique = new ArrayList<>();
        for (int i = 0; i < olt_data_from_pon_table.size(); i++) {
            if (olt_name_unique_set.contains(olt_data_from_pon_table.get(i).get(unique_field))) {
                continue;
            } else {
                olt_data_from_pon_table_unique.add(olt_data_from_pon_table.get(i));
            }
        }
        return olt_data_from_pon_table_unique;
    }

    /**
     * 3.统计该局站每个oltPON板，PON口使用情况;人为规定新增PON板为10GPON板；若新增OLT，新增华为MA5800的OLT
     */
    public List<Map<String, Object>> cal_pon_board_and_port(List<Map<String, Object>> olt_data_from_pon_table_unique, String station) {

        List<Map<String, Object>> result = new ArrayList<>();
        //1.根据olt型号确认PON板数，PON口数，粒度为OLT
        for (int i = 0; i < olt_data_from_pon_table_unique.size(); i++) {
            Map<String, Object> temp_map = new HashMap<>();
            //传入olt_name,查出所有的信息
            List<Map<String, Object>> olt_pon_port_data = planMapper.select_pon_port_data_by_olt_name(olt_data_from_pon_table_unique.get(i).get("olt_pon_name").toString(), TimeStamp.timeFlag);
            //可能会需要去重，否则程序有可能出错，但不是代码的问题，是数据库数据一致性的问题
            List<Map<String, Object>> olt_pon_data = planMapper.select_pon_data_by_olt_name(olt_data_from_pon_table_unique.get(i).get("olt_pon_name").toString(), TimeStamp.timeFlag);
            //根据查询处的设备类型，判定业务板槽数。
            int[] olt_board;
            if (olt_pon_port_data.get(0).get("model").toString().contains("AN5516"))
                olt_board = new int[AN5516.get("board_count")];
            else if (olt_pon_port_data.get(0).get("model").toString().contains("C220"))
                olt_board = new int[C220.get("board_count")];
            else if (olt_pon_port_data.get(0).get("model").toString().contains("C300"))
                olt_board = new int[C300.get("board_count")];
            else if (olt_pon_port_data.get(0).get("model").toString().contains("MA5800"))
                olt_board = new int[MA5800.get("board_count")];
            else if (olt_pon_port_data.get(0).get("model").toString().contains("MA5680"))
                olt_board = new int[MA5680.get("board_count")];
            else if (olt_pon_port_data.get(0).get("model").toString().contains("MA5600"))
                olt_board = new int[MA5600.get("board_count")];
            else {
                //为了防止程序检测的可能没出现初始化的情况，实际上不包含这种情况。
                olt_board = new int[24];
            }
            //PON板辅助数组初始化全为0
            for (int index = 0; index < olt_board.length; index++) {
                olt_board[index] = 0;
            }
            //去重
            List<Map<String, Object>> olt_pon_data_unique = unique_olt_name(olt_pon_data, "pon_board_number");
            int[][] olt_port = new int[olt_board.length][];
            //初始化PON_port辅助数组，有没有会出现5800型号1G的PON板情况，有的话，需要改一下辅助数组的长度；如果没有，则不需要更改。
            if (olt_pon_port_data.get(0).get("model").toString().contains("C220")) {
                for (int temp = 0; temp < olt_port.length; temp++) {
                    int[] list = new int[4];
                    for (int temp2 = 0; temp2 < list.length; temp2++) {
                        list[temp2] = 0;
                    }
                    olt_port[temp] = list;
                }
            } else {
                for (int temp = 0; temp < olt_port.length; temp++) {
                    int[] list = new int[8];
                    for (int temp2 = 0; temp2 < list.length; temp2++) {
                        list[temp2] = 0;
                    }
                    olt_port[temp] = list;
                }
            }
            for (int j = 0; j < olt_board.length; j++) {
                //默认数据库板下标从1开始，连续使用pon_board_number个PON板如1：17直接记为业务板槽
                /**
                 * todolist：需要确认数据库格式和
                 */
                //1.遍历olt_pon_data_unique，如果出现PON板序号和j+1相等的情况，olt_port[j]置1或2表示原先就已经使用的1GPON板和１０GPON板；否则置0表示未使用
                //2.同时给port数组进行初始化。
                for (Map mp : olt_pon_data_unique) {
                    if (mp.get("pon_board_number").toString().equals(String.valueOf(j + 1))) {
                        if (mp.get("bandwidth").toString().contains("10000")) {
                            olt_board[j] = 2;
                            //10GPON板全为8端口。
                            olt_port[j] = new int[8];
                        } else if (mp.get("bandwidth").toString().contains("1000")) {
                            olt_board[j] = 1;
                            if (olt_pon_port_data.get(0).get("model").toString().contains("MA5800"))
                                olt_port[j] = new int[16];
                            else if (olt_pon_port_data.get(0).get("model").toString().contains("C220"))
                                olt_port[j] = new int[4];
                            else
                                olt_port[j] = new int[8];
                        } else {
                            //非1G和10G的情况，可能会出现，暂时没想好怎么处理。
                            olt_board[j] = -1;
                        }
                        //初始化
                        for (int k = 0; k < olt_port[j].length; k++) {
                            olt_port[j][k] = 0;
                        }
                    }
                }
                //数据库中华为机器的port下标从0开始；中兴从1开始
                //给当前PON板的端口辅助数组赋值
                //华为、中心的数据pon口起始下标不一样，需要在这里做判定和调整
                if (olt_pon_port_data.size() != 0 & olt_pon_port_data.get(0).get("olt_pon_name").toString().contains("_HW_")) {
                    for (Map mp : olt_pon_port_data) {
                        if (mp.get("pon_board_number").toString().equals(String.valueOf(j + 1)))
//                            System.out.println("board: "+mp.get("pon_board_number").toString() +"   port:"+mp.get("pon_port_number").toString());
                            olt_port[j][Integer.parseInt(mp.get("pon_port_number").toString())] = 1;
                    }
                } else {
                    for (Map mp : olt_pon_port_data) {
                        if (mp.get("pon_board_number").toString().equals(String.valueOf(j + 1)))
//                            System.out.println("board: "+mp.get("pon_board_number").toString() +"   port:"+mp.get("pon_port_number").toString());
                            olt_port[j][Integer.parseInt(mp.get("pon_port_number").toString()) - 1] = 1;
                    }
                }

            }
//            System.out.println();
            temp_map.put("olt_name", olt_pon_data_unique.get(0).get("olt_pon_name").toString());
            temp_map.put("array_board", olt_board);
            temp_map.put("array_port", olt_port);
            result.add(temp_map);


//            //把每一个OLT的数据调整成固定格式插入新表20200604_plan中
//            String board_str = Arrays.toString(olt_board);
//            String port_str = get_list_str(olt_port);
//            System.out.println("board_str: "+board_str);
//            System.out.println("port_str: "+port_str);
//            planMapper.insert_into_plan_table(olt_pon_data_unique.get(0).get("olt_pon_name").toString(),board_str,port_str,station);
        }
//        System.out.println();
        return result;
    }

    //小工具，二维数组转String
    public String get_list_str(int[][] list) {
        String return_str = "[";
        for (int i = 0; i < list.length; i++) {
            return_str += Arrays.toString(list[i]);
            if (i != list.length - 1)
                return_str += ",";
        }
        return_str += "]";
        return return_str;
    }

    //若要新建OLT，则新建华为5800型号，默认为17业务槽位、8个10GPON口。
    //sql语句已经保证了千兆打分结果排在最前面。
    public Map<String, Object> update_pon_board_and_port(String station, List<Map<String, Object>> score_result_by_station, List<Map<String, Object>> olt_state, Map<String, Object> PON_board_status, String if_necessary_selected) {
        int new_olt_index = 1;
        //1.打分结果记录中加入新字段，初始化。
        for (Map score : score_result_by_station) {
            score.put("if_necessary", "");//是否必须扩容
            score.put("operate_type", "");//操作类型
            score.put("target_OLT", "");//目标OLT名称
            score.put("target_port", "");//目标端口
        }
        //根据打分结果表的待升级PON口顺序，从olt_state列表中找到空闲PON口，空闲PON板，或新建OLT来升级。
        //2.遍历每条需要扩容的打分记录，进行局站内打分结果的扩容规划
        for (Map score : score_result_by_station) {
            //2.1打分结果中添加是否必须扩容字段
            if (score.get("priority").toString().equals("1")) {
                score.put("if_necessary", "yes");//是否必须扩容
            } else {
                score.put("if_necessary", "no");//是否必须扩容
            }
            boolean this_record_planed = false;
            //2.2优先同一OLT进行割接扩容，割接到其他空闲10GPON板的PON口下
            out_self1:
            for (Map mp : olt_state) {
                if (mp.get("olt_name").toString().equals(score.get("olt_name").toString())) {
                    //2.2.1得到当前OLT的PON板，PON口使用情况约定如下：
                    //PON板：原本未使用的为0；已使用的1GPON板为1；已使用的10GPON板为2；扩容新增（必须扩容）的10GPON板为3;扩容新增（非必须扩容）的10GPON板为4;
                    //PON口：原本未使用的为0；已使用的为1；扩容新增使用的（必须扩容）为2；扩容新增使用的（非必须扩容）为3；原有必须扩容撤下的PON口为-2；原有必须扩容撤下的PON口为-3
                    int[] board_temp = (int[]) mp.get("array_board");
                    int[][] port_temp = (int[][]) mp.get("array_port");
                    //2.2.2找空闲10GPON口直接割接
                    for (int i = 0; i < board_temp.length; i++) {
                        //2.2.2.1找10GPON板
                        if (board_temp[i] == 2 || board_temp[i] == 3 || board_temp[i] == 4) {
                            for (int j = 0; j < port_temp[i].length; j++) {
                                //2.2.2.1.1找空闲PON口；由于撤下的原有使用PON口都是非10G的，所以不用考虑
                                if (port_temp[i][j] == 0) {
                                    //使用空闲端口进行扩容，必须扩容设为2，非必须扩容设为3
                                    //恢复原本端口的使用状态，需不许扩容恢复的为-2；非必须扩容的回复为-3
                                    if (score.get("if_necessary").toString().equals("yes")) {

                                        port_temp[i][j] = 2;
                                        //如果中兴设备，PON口下标从1开始；其余从0开始
                                        String[] pon_port = score.get("pon_port").toString().split("--");
                                        if (mp.get("olt_name").toString().contains("_ZX_")) {
                                            port_temp[Integer.parseInt(pon_port[0]) - 1][Integer.parseInt(pon_port[1]) - 1] = -2;
                                            score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(j + 1));//目标端口
                                        } else {
                                            port_temp[Integer.parseInt(pon_port[0]) - 1][Integer.parseInt(pon_port[1])] = -2;
                                            score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(j));//目标端口
                                        }
                                    } else {
                                        port_temp[i][j] = 3;
                                        //如果中兴设备，PON口下标从1开始；其余从0开始
                                        String[] pon_port = score.get("pon_port").toString().split("--");
                                        if (mp.get("olt_name").toString().contains("_ZX_")) {
                                            port_temp[Integer.parseInt(pon_port[0]) - 1][Integer.parseInt(pon_port[1]) - 1] = -3;
                                            score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(j + 1));//目标端口
                                        } else {
                                            port_temp[Integer.parseInt(pon_port[0]) - 1][Integer.parseInt(pon_port[1])] = -3;
                                            score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(j));//目标端口
                                        }
                                    }
                                    //更新打分结果中的字段数据
                                    score.put("operate_type", "原有的OLT上找到空闲端口进行扩容");//操作类型
                                    score.put("target_OLT", mp.get("olt_name").toString());//目标OLT名称
                                    this_record_planed = true;
                                    mp.put("array_port", port_temp);
                                    break out_self1;
                                }
                            }
                        }
                    }
                    //2.2.3若直接找空闲PON口割接不成功，同OLT找空闲PON板新建PON板割接
                    for (int i = 0; i < board_temp.length; i++) {
                        //2.2.3.1找空闲PON板
                        if (board_temp[i] == 0) {

                            //使用空闲端口进行扩容，必须扩容设为2，非必须扩容设为3
                            //恢复原本端口的使用状态，需不许扩容恢复的为-2；非必须扩容的回复为-3
                            if (score.get("if_necessary").toString().equals("yes")) {
                                System.out.println(port_temp.length);
                                System.out.println(port_temp[0].length);

                                board_temp[i] = 3;
                                port_temp[i][0] = 2;
                                //如果中兴设备，PON口下标从1开始；其余从0开始
                                String[] pon_port = score.get("pon_port").toString().split("--");
                                System.out.println(pon_port[0]);
                                port_temp[Integer.parseInt(pon_port[0]) - 1][1] = -2;

                                //port_temp[Integer.parseInt(pon_port[0]) - 1][1] = -2;
                                if (mp.get("olt_name").toString().contains("_ZX_")) {
                                    score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(1));//目标端口
                                } else {
                                    score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(0));//目标端口
                                }
                            } else {
                                board_temp[i] = 4;
                                port_temp[i][0] = 3;
                                //如果中兴设备，PON口下标从1开始；其余从0开始
                                String[] pon_port = score.get("pon_port").toString().split("--");
                                port_temp[Integer.parseInt(pon_port[0]) - 1][1] = -3;
                                if (mp.get("olt_name").toString().contains("_ZX_")) {
                                    score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(1));//目标端口
                                } else {
                                    score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(0));//目标端口
                                }
                                //更新打分结果中的字段数据
                                score.put("operate_type", "原有的OLT上新建PON板进行扩容");//操作类型
                                score.put("target_OLT", mp.get("olt_name").toString());//目标OLT名称
                                this_record_planed = true;
                                mp.put("array_port", port_temp);
                                mp.put("array_board", board_temp);
                                break out_self1;
                            }
                        }
                    }
                }
            }
            //2.3由于是非同一OLT，需要获取olt_state中和打分结果匹配的OLTPON板、PON口使用情况的数组，做旧PON口撤除的数据更新
            //由于打分结果表和pon表目前不是基于同一时间的数据，OLT可能不匹配，后续的逻辑需要加入非空判定。
            int[] board_score_OLT = null;
            int[][] port_score_OLT = null;
            int OLT_index = 0;
            find_score:
            for (Map mp : olt_state) {
                if (mp.get("olt_name").toString().equals(score.get("olt_name").toString())) {
                    board_score_OLT = (int[]) mp.get("array_board");
                    port_score_OLT = (int[][]) mp.get("array_port");
                    OLT_index = olt_state.indexOf(mp);
                    break find_score;
                }
            }
            //2.4如果同OLT割接不成功，找同局其他设备的空闲PON口进行割接
            if (!this_record_planed) {
                out:
                for (Map mp : olt_state) {
                    //2.4.1获取当前OLT的PON板、PON口使用情况
                    int[] board_temp = (int[]) mp.get("array_board");
                    int[][] port_temp = (int[][]) mp.get("array_port");
                    //2.4.2找空闲10GPON口直接割接
                    for (int i = 0; i < board_temp.length; i++) {
                        //2.4.2.1找10GPON板
                        if (board_temp[i] == 2 || board_temp[i] == 3 || board_temp[i] == 4) {
                            for (int j = 0; j < port_temp[i].length; j++) {
                                //如果找到了空闲端口，则进行端口直接割接,并记录割接结果
                                if (port_temp[i][j] == 0) {
                                    //使用空闲端口进行扩容，必须扩容设为2，非必须扩容设为3
                                    //恢复原本端口的使用状态，需不许扩容恢复的为-2；非必须扩容的回复为-3
                                    if (score.get("if_necessary").toString().equals("yes")) {
                                        port_temp[i][j] = 2;
                                        //如果中兴设备，PON口下标从1开始；其余从0开始
                                        String[] pon_port = score.get("pon_port").toString().split("--");
                                        if (mp.get("olt_name").toString().contains("_ZX_")) {
                                            score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(j + 1));//目标端口
                                            if (port_score_OLT != null)
                                                port_score_OLT[Integer.parseInt(pon_port[0]) - 1][Integer.parseInt(pon_port[1]) - 1] = -2;
                                        } else {
                                            score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(j));//目标端口
                                            if (port_score_OLT != null)
                                                port_score_OLT[Integer.parseInt(pon_port[0]) - 1][Integer.parseInt(pon_port[1])] = -2;
                                        }
                                    } else {
                                        port_temp[i][j] = 3;
                                        //如果中兴设备，PON口下标从1开始；其余从0开始
                                        String[] pon_port = score.get("pon_port").toString().split("--");
                                        if (mp.get("olt_name").toString().contains("_ZX_")) {
                                            score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(j + 1));//目标端口
                                            if (port_score_OLT != null)
                                                port_score_OLT[Integer.parseInt(pon_port[0]) - 1][Integer.parseInt(pon_port[1]) - 1] = -3;
                                        } else {
                                            score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(j));//目标端口
                                            if (port_score_OLT != null)
                                                port_score_OLT[Integer.parseInt(pon_port[0]) - 1][Integer.parseInt(pon_port[1])] = -3;
                                        }
                                    }
                                    //判断是否是新建OLT扩容
                                    if (mp.get("olt_name").toString().contains("new_olt")) {
                                        score.put("operate_type", "新建OLT进行扩容");//操作类型
                                    } else if (board_temp[i] == 2) {
                                        score.put("operate_type", "原有的OLT上找到空闲端口进行扩容");//操作类型
                                    } else {
                                        score.put("operate_type", "原有的OLT上新建PON板进行扩容");//操作类型
                                    }
                                    score.put("target_OLT", mp.get("olt_name").toString());//目标OLT名称
                                    this_record_planed = true;
                                    mp.put("array_port", port_temp);
                                    //如果非空，更新撤下PON口的OLTPON口状态数组。
                                    if (port_score_OLT != null) {
                                        Map<String, Object> mp2 = olt_state.get(OLT_index);
                                        mp2.put("array_port", port_score_OLT);
                                    }
                                    break out;
                                }
                            }
                        }
                    }
                }
            }
            //2.5如果找不到空闲PON口，找空闲PON板
            if (!this_record_planed) {
                out2:
                for (Map mp : olt_state) {
                    //2.5.1获取当前OLT的PON板、PON口使用情况
                    int[] board_temp = (int[]) mp.get("array_board");
                    int[][] port_temp = (int[][]) mp.get("array_port");
                    //2.5.2找空闲10GPON口直接割接
                    for (int i = 0; i < board_temp.length; i++) {
                        //2.5.2.1找10GPON板
                        if (board_temp[i] == 0) {
                            //使用空闲端口进行扩容，必须扩容设为2，非必须扩容设为3
                            //恢复原本端口的使用状态，需不许扩容恢复的为-2；非必须扩容的回复为-3
                            if (score.get("if_necessary").toString().equals("yes")) {
                                board_temp[i] = 3;
                                port_temp[i][0] = 2;
                                //如果中兴设备，PON口下标从1开始；其余从0开始
                                String[] pon_port = score.get("pon_port").toString().split("--");
                                if (mp.get("olt_name").toString().contains("_ZX_")) {
                                    score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(1));//目标端口
                                    if (port_score_OLT != null)
                                        port_score_OLT[Integer.parseInt(pon_port[0]) - 1][Integer.parseInt(pon_port[1]) - 1] = -2;
                                } else {
                                    score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(0));//目标端口
                                    if (port_score_OLT != null)
                                        port_score_OLT[Integer.parseInt(pon_port[0]) - 1][Integer.parseInt(pon_port[1])] = -2;
                                }
                            } else {
                                board_temp[i] = 4;
                                port_temp[i][0] = 3;
                                //如果中兴设备，PON口下标从1开始；其余从0开始
                                String[] pon_port = score.get("pon_port").toString().split("--");
                                if (mp.get("olt_name").toString().contains("_ZX_")) {
                                    score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(1));//目标端口
                                    if (port_score_OLT != null)
                                        port_score_OLT[Integer.parseInt(pon_port[0]) - 1][Integer.parseInt(pon_port[1]) - 1] = -3;
                                } else {
                                    score.put("target_port", String.valueOf(i + 1) + "--" + String.valueOf(0));//目标端口
                                    if (port_score_OLT != null)
                                        port_score_OLT[Integer.parseInt(pon_port[0]) - 1][Integer.parseInt(pon_port[1])] = -3;
                                }
                            }
                            //更新打分结果中的字段数据
                            if (mp.get("olt_name").toString().contains("new_olt")) {
                                score.put("operate_type", "新建OLT进行扩容");//操作类型
                            } else {
                                score.put("operate_type", "原有的OLT上新建PON板进行扩容");//操作类型
                            }
                            score.put("target_OLT", mp.get("olt_name").toString());//目标OLT名称
                            this_record_planed = true;
                            mp.put("array_port", port_temp);
                            mp.put("array_board", board_temp);
                            //如果非空，更新撤下PON口的OLTPON口状态数组。
                            if (port_score_OLT != null) {
                                Map<String, Object> mp2 = olt_state.get(OLT_index);
                                mp2.put("array_port", port_score_OLT);
                            }
                            break out2;
                        }
                    }
                }
            }
            //2.6找不到空闲PON板，扩建OLT。
            out3:
            if (!this_record_planed) {
                //初始化
                Map<String, Object> new_olt_state = new HashMap<>();
                new_olt_state.put("olt_name", "new_olt_HW_" + new_olt_index);
                new_olt_index++;
                //定义新OLT的辅助数组,并初始化
                int[] olt_board;
                olt_board = new int[MA5800.get("board_count")];
                int[][] olt_port = new int[olt_board.length][8];
                for (int i = 0; i < olt_board.length; i++) {
                    olt_board[i] = 0;
                    for (int temp = 0; temp < 8; temp++)
                        olt_port[i][temp] = 0;
                }
                new_olt_state.put("station", station);
                //使用空闲端口进行扩容，必须扩容设为2，非必须扩容设为3
                //恢复原本端口的使用状态，需不许扩容恢复的为-2；非必须扩容的回复为-3

                if (score.get("if_necessary").toString().equals("yes")) {
                    olt_board[0] = 3;
                    olt_port[0][0] = 2;
                    //华为设备，设置规划结果的目标端口。
                    String[] pon_port = score.get("pon_port").toString().split("--");
                    if (port_score_OLT != null)
                        port_score_OLT[Integer.parseInt(pon_port[0]) - 1][Integer.parseInt(pon_port[1])] = -2;
                } else {
                    olt_board[0] = 4;
                    olt_port[0][0] = 3;
                    //华为设备，设置规划结果的目标端口。
                    String[] pon_port = score.get("pon_port").toString().split("--");
                    if (port_score_OLT != null)
                        port_score_OLT[Integer.parseInt(pon_port[0]) - 1][Integer.parseInt(pon_port[1])] = -3;

                }
                //使用该新增的olt端口
                new_olt_state.put("array_port", olt_port);
                new_olt_state.put("array_board", olt_board);
                //olt_state状态列表新增该OLT。
                olt_state.add(new_olt_state);
                this_record_planed = true;
                score.put("target_port", String.valueOf(1) + "--" + String.valueOf(0));//目标端口
                score.put("operate_type", "新建OLT进行扩容");//操作类型
                score.put("target_OLT", new_olt_state.get("olt_name").toString());//目标OLT名称
                //如果非空，更新撤下PON口的OLTPON口状态数组。
                if (port_score_OLT != null) {
                    Map<String, Object> mp2 = olt_state.get(OLT_index);
                    mp2.put("array_port", port_score_OLT);
                }
                break out3;
            }
            //打分结果表基于2019年12月的数据，而查询结果目前根据2018年9月数据。

        }

        //3.更新PON板使用情况
        int PON_borad_used = 0;
        int PON_board_spare = 0;
        int PON_board_add = 0;
        int OLT_add = 0;
        if (if_necessary_selected.equals("yes")) {
            for (Map mp : olt_state) {
                int[] board_temp = (int[]) mp.get("array_board");
                if (mp.get("olt_name").toString().contains("new")) {
                    OLT_add += 1;
                    for (int i = 0; i < board_temp.length; i++) {
                        if (board_temp[i] == 3) {
                            PON_board_add += 1;
                        }
                    }
                } else {
                    for (int i = 0; i < board_temp.length; i++) {
                        if (board_temp[i] == 0) {
                            PON_board_spare += 1;
                        } else if (board_temp[i] == 1 || board_temp[i] == 2) {
                            PON_borad_used += 1;
                        } else if (board_temp[i] == 3) {
                            PON_board_add += 1;
                        }
                    }
                }
            }
        } else {
            for (Map mp : olt_state) {
                int[] board_temp = (int[]) mp.get("array_board");
                if (mp.get("olt_name").toString().contains("new")) {
                    OLT_add += 1;
                    for (int i = 0; i < board_temp.length; i++) {
                        if (board_temp[i] == 3 || board_temp[i] == 4) {
                            PON_board_add += 1;
                        }
                    }
                } else {
                    for (int i = 0; i < board_temp.length; i++) {
                        if (board_temp[i] == 0) {
                            PON_board_spare += 1;
                        } else if (board_temp[i] == 1 || board_temp[i] == 2) {
                            PON_borad_used += 1;
                        } else if (board_temp[i] == 3 || board_temp[i] == 4) {
                            PON_board_add += 1;
                        }
                    }
                }
            }
        }

        PON_board_status.put("PON_borad_used", PON_borad_used);
        PON_board_status.put("PON_board_spare", PON_board_spare);
        PON_board_status.put("PON_board_add", PON_board_add);
        PON_board_status.put("OLT_add", OLT_add);
        //把结果转成合适的返回数据。从olt_state里面寻找数据和封装。

        //把结果转成合适的返回数据，目前封装为树状图
        Map<String, Object> total = new LinkedHashMap<>();
        Map<String, Object> tree = new LinkedHashMap<>();
        List<List<Map<String, Object>>> form = new ArrayList<>();
        tree.put("name", station + "统计结果");
        tree.put("value", "");
        List<Map<String, Object>> children = new ArrayList<>();
        Map<String, Object> highTree = new LinkedHashMap<>();
        Map<String, Object> lowTree = new LinkedHashMap<>();
        List<Map<String, Object>> highList = new ArrayList<>();
        List<Map<String, Object>> lowList = new ArrayList<>();
        highTree.put("name", "最高优先级");
        highTree.put("value", "");
        lowTree.put("name", "次高优先级");
        lowTree.put("value", "");
        Map<String, String> color = new HashMap<>();
        color.put("color", "#E6B621");
        List<Object> size = new ArrayList<>();
        size.add(140);
        size.add(70);
//        int newBoard = 0;
//        int newPort = 0;


        for (Map<String, Object> olt : olt_state) {
            int newBoard = 0;
            int newPort = 0;
            Map<String, Object> tree_element = new LinkedHashMap<>();

            List<Map<String, Object>> formOLT = new ArrayList<>();

            boolean priority = false;

//            System.out.println(OLTFlag);

            List<Map<String, Object>> PonBoardTreeList = new ArrayList<>();

//            List<Map<String, Object>> formList = new ArrayList<>();

            int[] board_temp = (int[]) olt.get("array_board");

//            System.out.println(Arrays.toString(board_temp));

            for (int i = 0; i < board_temp.length; i++) {

                Map<String, Object> formMap = new LinkedHashMap<>();

                Map<String, Object> PonBoardTreeMap = new LinkedHashMap<>();
                boolean PonBoardFlag;

                if (board_temp[i] == 2) {
                    PonBoardFlag = false;
                } else if ((board_temp[i] == 3)) {
                    PonBoardFlag = true;
                    priority = true;
                    newBoard++;
                } else if ((board_temp[i] == 4)) {
                    PonBoardFlag = true;
                    newBoard++;
                } else {
                    continue;
                }
                PonBoardTreeMap.put("name", i + "号板");

                String boardValue = PonBoardFlag ? "新建设备" : "已有设备";

                PonBoardTreeMap.put("value", boardValue);

                if (PonBoardFlag) {
                    PonBoardTreeMap.put("itemStyle", color);

                }

                int[][] port_temp = (int[][]) olt.get("array_port");
                List<Map<String, Object>> PonPortTreeList = new ArrayList<>();
                Map<String, Object> PonPortEnterMap = new LinkedHashMap<>();
                Map<String, Object> PonPortOriginalMap = new LinkedHashMap<>();
                PonPortOriginalMap.put("name", "原有Pon口");
                PonPortEnterMap.put("name", "迁入Pon口");
                int original = 0;
                int enter = 0;
                for (int j = 0; j < port_temp[i].length; j++) {

                    if (port_temp[i][j] == 1) {
                        original++;
                    }
                    if (port_temp[i][j] == 2) {
                        enter++;
                        newPort++;
                    }
                    if (port_temp[i][j] == 3) {
                        enter++;
                        newPort++;
                    }
                }

                PonPortOriginalMap.put("value", original);
                PonPortEnterMap.put("value", enter);
                PonPortEnterMap.put("itemStyle", color);

                Map<String, Object> formElement = new LinkedHashMap<>();
                formElement.put("原有10GPon口数量", original);
                formElement.put("迁入10GPon口数量", enter);
                formMap.put("olt", olt.get("olt_name").toString());
                formMap.put("pon", "Pon板" + i);
                formMap.put("原有10G", original);
                formMap.put("迁入10G", enter);
                formOLT.add(formMap);

                PonPortOriginalMap.put("children", "");
                PonPortEnterMap.put("children", "");
//                PonPortTreeMap.put("name","原有Pon口 "+enter+", 迁入Pon口 "+original);
//                PonPortTreeMap.put("value", null);
//                PonPortTreeMap.put("children", null);
                PonPortTreeList.add(PonPortOriginalMap);
                PonPortTreeList.add(PonPortEnterMap);
                PonBoardTreeMap.put("children", PonPortTreeList);
                PonBoardTreeList.add(PonBoardTreeMap);
            }

            if (PonBoardTreeList.size() == 0) {
                continue;
            }

            tree_element.put("name", olt.get("olt_name").toString() + "\n" + "新建Pon板总数: " + newBoard + "\n" + "迁入Pon口总数: " + newPort);
            boolean OLTFlag = olt.get("olt_name").toString().contains("new");
            String oltValue = OLTFlag ? "新建设备" : "已有设备";
            int formValue = OLTFlag ? 1 : 0;
            tree_element.put("value", oltValue);
//            formMap.put("name", olt.get("olt_name").toString() + "(" + oltValue + ")");
            for (Map<String, Object> map : formOLT) {
                map.put("all", "新建OLT：" + formValue + "\n" + "新建Pon板总数：" + newBoard + "\n" + "迁入Pon口总数：" + newPort);
            }

//            formTemp.put("name", olt.get("olt_name").toString() + "(" + oltValue + ")");
//            for (Map<String, Object> map : formList) {
//                formTemp.putAll(map);
//            }
//            formTemp.put("新建Pon板总数", newBoard);
//            formTemp.put("迁入Pon口总数", newPort);

            if (OLTFlag) {
                tree_element.put("itemStyle", color);
            }
            tree_element.put("symbolSize", size);
            tree_element.put("children", PonBoardTreeList);
//            tree_element.put("新建Pon板总数", newBoard);
//            tree_element.put("迁入Pon口总数", newPort);
            if (priority) {
                highList.add(tree_element);
            } else {
                lowList.add(tree_element);
            }

            form.add(formOLT);
        }
        highTree.put("children", highList);
        lowTree.put("children", lowList);
        children.add(highTree);
        children.add(lowTree);
        tree.put("children", children);
        total.put("树状图结构", tree);
        total.put("表格结构", form);
        return total;
//        return score_result_by_station;
    }


    public void opex_plan(List<Map<String, Object>> olt_state) {
        System.out.println();

    }

    @Override
    public List<Map<String, Object>> get_department_of_scored(String time) {
        return planMapper.get_department_of_scored(time, TimeStamp.timeFlag);
    }

    @Override
    public List<Map<String, Object>> get_station_of_scored(String department, String time) {
        return planMapper.get_station_of_scored(department, time, TimeStamp.timeFlag);
    }

    @Override
    public List<String> selectAllStation() {
        return planMapper.selectAllStation(TimeStamp.timeFlag);
    }


}
