package com.liantong.demo_part2.Service.Impl;

import com.liantong.demo_part2.Entity.TimeStamp;
import com.liantong.demo_part2.Mapper.PONMapper;
import com.liantong.demo_part2.Service.PONService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PONServiceImpl implements PONService {

    @Autowired
    private PONMapper ponMapper;

    @Override
    public List<Set<Map<String, Object>>> UserNumOfDifferentMeal() {
        List<Set<Map<String, Object>>> list = new ArrayList<>();

        Set<Map<String, Object>> s1 = new LinkedHashSet<>();
        Map<String, Object> o1 = new HashMap<>();
        o1.put("name", "C300");
        o1.put("value", 335);
        s1.add(o1);

        Map<String, Object> o2 = new HashMap<>();
        o2.put("name", "DT541");
        o2.put("value", 310);
        s1.add(o2);


        Map<String, Object> o3 = new HashMap<>();
        o3.put("name", "HG850E");
        o3.put("value", 135);
        s1.add(o3);

        Map<String, Object> o4 = new HashMap<>();
        o4.put("name", "ZTE-F420");
        o4.put("value", 234);
        s1.add(o4);

        Map<String, Object> o5 = new HashMap<>();
        o5.put("name", "F817");
        o5.put("value", 1548);
        s1.add(o5);

        Set<Map<String, Object>> s2 = new LinkedHashSet<>();
        Map<String, Object> o11 = new HashMap<>();
        o11.put("name", "200M");
        o11.put("value", 335);
        o11.put("selected", true);
        Map<String, Object> o12 = new HashMap<>();
        o12.put("name", "300M");
        o12.put("value", 679);
        Map<String, Object> o13 = new HashMap<>();
        o13.put("name", "500M");
        o13.put("value", 1548);
        s2.add(o11);
        s2.add(o12);
        s2.add(o13);
        list.add(s1);
        list.add(s2);

        return list;

    }

    @Override
    public List<Map<String, Object>> returnDepartmentOf_Table(String tableIndex) {
//        System.out.println(tableIndex);
        if ("1000M".equals(tableIndex)) {
            return ponMapper.returnDepartmentOf_1000M_Table(TimeStamp.timeFlag);
        } else if ("500M".equals(tableIndex)) {
            return ponMapper.returnDepartmentOf_500M_Table(TimeStamp.timeFlag);
        } else if ("all".equals(tableIndex)) {
            return ponMapper.returnDepartmentOf_HighDemand_Table(TimeStamp.timeFlag);
        } else return null;

    }

    @Override
    public List<Map<String, Object>> returnOLTNameOf_Table(String tableIndex, String department) {
        System.out.println(tableIndex);
        if ("1000M".equals(tableIndex) && department != null) {
            return ponMapper.returnOLTNameOfOf_1000M_Table(department,TimeStamp.timeFlag);
        } else if ("500M".equals(tableIndex) && department != null) {
            return ponMapper.returnOLTNameOfOf_500M_Table(department,TimeStamp.timeFlag);
        } else if ("all".equals(tableIndex) && department != null) {
            return ponMapper.returnOLTNameOfOf_HighDemand_Table(department,TimeStamp.timeFlag);
        } else return null;
    }

    @Override
    public List<Map<String, Object>> returnPON_PortNameOf_Table(String tableIndex, String department, String OLT_name) {
        System.out.println(tableIndex);
        if ("1000M".equals(tableIndex) && department != null && OLT_name != null) {
            return ponMapper.returnPON_PortNameOf_1000M_Table(department, OLT_name,TimeStamp.timeFlag);
        } else if ("500M".equals(tableIndex) && department != null && OLT_name != null) {
            return ponMapper.returnPON_PortNameOf_500M_Table(department, OLT_name,TimeStamp.timeFlag);
        } else if ("all".equals(tableIndex) && department != null && OLT_name != null) {
            return ponMapper.returnPON_PortNameOf_HighDemand_Table(department, OLT_name,TimeStamp.timeFlag);
        } else return null;

    }


    @Override
    public Map<String,Object> return_1000MTable_userNum(String department, String OLT_name, String board, String port) {
        Map<String,Object> map = new HashMap<>();
        List<Map<String, Object>> list1 = ponMapper.return_1000MTable_10G_userNum(department, OLT_name, board, port,TimeStamp.timeFlag);
        System.out.println("这是list1");
        System.out.println(list1);
        List<Map<String, Object>> list2 = ponMapper.return_1000MTable_Not10G_userNum(department, OLT_name, board, port,TimeStamp.timeFlag);
        System.out.println("这是list2");
        System.out.println(list2);
        if(list1.get(0)!= null){
            map.putAll(list1.get(0));
        }
        else {
            Map<String,Object> map1 = new HashMap<>();
            map1.put("10G_user_num",0);
            map.putAll(map1);
        }
        if(list2.get(0)!= null){
            map.putAll(list2.get(0));
        }
        else {
            Map<String,Object> map1 = new HashMap<>();
            map1.put("Not_10G_user_num",0);
            map.putAll(map1);
        }
        return map;
    }

    @Override
    public Map<String,Object> return_1000MTable_Not10G_OLTNum(String department, String OLT_name) {
        List<Map<String, Object>> list = ponMapper.return_1000MTable_Not10G_OLTNum(department, OLT_name,TimeStamp.timeFlag);
        //统计OLT总数、新旧数，厂家数
        int OLT_total = 0;
        int new_OLT_tatol = 0;
        int old_OLT_total = 0;
        int vendor_total = 0;
        Set<String> vendorSet = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            OLT_total += Integer.parseInt(list.get(i).get("count").toString());
            if (list.get(i).get("model").toString().contains("5800") || list.get(i).get("model").toString().contains("C600")) {
                new_OLT_tatol += Integer.parseInt(list.get(i).get("count").toString());
            } else {
                old_OLT_total += Integer.parseInt(list.get(i).get("count").toString());
            }
            vendorSet.add(list.get(i).get("vendor").toString());
        }
        vendor_total = vendorSet.size();
        Map<String, Object> map = new HashMap<>();
        map.put("OLT_total", OLT_total);
//        Map<String, Object> map2 = new HashMap<>();
        map.put("new_OLT_tatol", new_OLT_tatol);
//        Map<String, Object> map3 = new HashMap<>();
        map.put("old_OLT_total", old_OLT_total);
//        Map<String, Object> map4 = new HashMap<>();
        map.put("vendor_total", vendor_total);

        return map;
    }

    @Override
    public Map<String,Object> return_1000MTable_10G_PonNum(String department, String OLT_name) {
        Integer Not_10G_Pon_Board_Num = ponMapper.return_1000MTable_Not10G_Pon_BoardNum(department, OLT_name,TimeStamp.timeFlag);
//        System.out.println("执行第一次");
        Integer Not_10G_Pon_port_Num = ponMapper.return_1000MTable_Not10G_Pon_PortNum(department, OLT_name,TimeStamp.timeFlag);
//        System.out.println("执行第二次");
        Integer Is_10G_Pon_Board_Num = ponMapper.return_1000MTable_10G_Pon_BoardNum(department,OLT_name,TimeStamp.timeFlag);
//        System.out.println("执行第三次");
        Integer Is_10G_Pon_port_Num = ponMapper.return_1000MTable_10G_Pon_PortNum(department,OLT_name,TimeStamp.timeFlag);
//        System.out.println("执行第四次");
//        if(Not_10G_Pon_Board_Num. = null){
//            Not_10G_Pon_Board_Num = 0;
//        }
        Map<String, Object> map1 = new HashMap<>();
        map1.put("Not_10G_Pon_Board_Num", Not_10G_Pon_Board_Num);
        map1.put("Is_10G_Pon_Board_Num",Is_10G_Pon_Board_Num);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("Not_10G_Pon_port_Num", Not_10G_Pon_port_Num);
        map2.put("Is_10G_Pon_port_Num",Is_10G_Pon_port_Num);
        Map<String,Object> result = new HashMap<>();
        result.put("Pon_Board", map1);
        result.put("Pon_Port", map2);
        return result;
    }

    @Override
    public List<Map<String, Object>> return_request_parms(String tableIndex,String department, String OLT_name, String board, String port) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String,Object>  map = new HashMap<>();
        if (tableIndex != null){

            map.put("tableIndex",tableIndex);
            result.add(map);
        }
        if (department != null){

            map.put("department",department);
            result.add(map);
        }
        if (OLT_name != null){

            map.put("OLT_name",OLT_name);
            result.add(map);
        }
        if (board != null){

            map.put("board",board);
            result.add(map);
        }
        if (port != null){

            map.put("port",port);
            result.add(map);
        }
        return result;
    }

    @Override
    public Map<String,List<Map<String,Object>>> return_1000M_Table(String tableIndex, String department, String OLT_name) {
        Map<String,List<Map<String,Object>>> map = new HashMap<>();
        List<Map<String,Object>> list_Not_10G = new ArrayList<>();
        List<Map<String,Object>> list_10G = new ArrayList<>();
        if("1000M".equals(tableIndex)){
            list_Not_10G = ponMapper.return_1000M_table_not_10G(department,OLT_name,TimeStamp.timeFlag);
            list_10G = ponMapper.return_1000M_table_10G(department,OLT_name,TimeStamp.timeFlag);
            map.put("10G",list_10G);
            map.put("1G",list_Not_10G);
        }
        return map;
    }

    @Override
    public List<String> second_Object_Name_List(Map<String, String> map) {
        List<String> list = new ArrayList<>();
        if(map.get("department") == null){
            list.add("department");
            List<Map<String,Object>> data = ponMapper.return_1000M_table_All_Department(TimeStamp.timeFlag);
            for (Map<String,Object> i :data){
                list.add(i.get("department").toString());
            }
        }
        else if(map.get("department") != null && map.get("OLT_name") == null){
            list.add("OLT_name");
            List<Map<String,Object>> data = ponMapper.return_1000M_table_All_OLTName(map.get("department"),TimeStamp.timeFlag);
            if(data != null){
                for (Map<String,Object> i :data){
                    list.add(i.get("OLT_name").toString());
                }
            }

        }
        else {
            list = null;
        }
//        System.out.println(list);
        return list;
    }


}
