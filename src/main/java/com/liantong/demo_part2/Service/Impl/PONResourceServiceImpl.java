package com.liantong.demo_part2.Service.Impl;

import com.liantong.demo_part2.Entity.TimeStamp;
import com.liantong.demo_part2.Mapper.PONMapper;
import com.liantong.demo_part2.Mapper.PONResourceMapper;
import com.liantong.demo_part2.Service.DataBaseService;
import com.liantong.demo_part2.Service.PONResourceService;
import io.swagger.models.auth.In;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import java.sql.ClientInfoStatus;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class PONResourceServiceImpl implements PONResourceService {

    @Autowired
    private PONResourceMapper ponResourceMapper;

    @Override
    public List<Map<String, Object>> returnVenderMassage(String department, String station) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> venders = ponResourceMapper.returnVender(department, station, TimeStamp.timeFlag);
        for (String vender : venders) {
            Map<String, Object> temp = new HashMap<>();
            List<Map<String, Object>> massage = ponResourceMapper.returnVenderMassage(vender, department, station, TimeStamp.timeFlag);
            List<Object> data = new ArrayList<>();
            List<Object> dep = new ArrayList<>();
            for (Map map : massage) {
                data.add(map.get("num"));
                dep.add(map.get("station"));
            }
            temp.put("name", vender);
            temp.put("type", "bar");
            temp.put("stack", "总量");
            Map<String, Object> elements = new HashMap<>();
            elements.put("show", "true");
            elements.put("position", "insideRight");
            temp.put("label", elements);
            temp.put("data", data);
            temp.put("dep", dep);
            result.add(temp);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> returnIPTVMassage(String department, String station,String olt) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> IPTVTypes = new ArrayList<>();
        IPTVTypes.add("是");
        IPTVTypes.add("否");
        for (String types : IPTVTypes) {
            Map<String, Object> temp = new HashMap<>();
            List<Map<String, Object>> massage = ponResourceMapper.returnIPTVMassage(types, department, station, olt,TimeStamp.timeFlag);
            List<Object> data = new ArrayList<>();
            List<Object> dep = new ArrayList<>();
            for (Map map : massage) {
                data.add(map.get("num"));
                dep.add(map.get("station"));
            }
            temp.put("name", types);
            temp.put("type", "bar");
            temp.put("stack", "总量");
            Map<String, Object> elements = new HashMap<>();
            elements.put("show", "true");
            elements.put("position", "insideRight");
            temp.put("label", elements);
            temp.put("data", data);
            temp.put("dep", dep);
            result.add(temp);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> userType(String department, String station) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> types = ponResourceMapper.returnTypes(department, station, TimeStamp.timeFlag);
        for (String type : types) {
            Map<String, Object> temp = new HashMap<>();
            List<Map<String, Object>> massage = ponResourceMapper.userType(type, department, station, TimeStamp.timeFlag);
            List<Object> data = new ArrayList<>();
            List<Object> dep = new ArrayList<>();
            for (Map map : massage) {
                data.add(map.get("num"));
                dep.add(map.get("station"));
            }
            temp.put("name", type);
            temp.put("type", "bar");
            temp.put("stack", "总量");
            Map<String, Object> elements = new HashMap<>();
            elements.put("show", "true");
            elements.put("position", "insideRight");
            temp.put("label", elements);
            temp.put("data", data);
            temp.put("dep", dep);
            result.add(temp);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> returnModelMassage(String department, String station) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> models = ponResourceMapper.returnModel(department, station, TimeStamp.timeFlag);
        for (String model : models) {
            Map<String, Object> temp = new HashMap<>();
            List<Map<String, Object>> massage = ponResourceMapper.returnModelMassage(model, department, station, TimeStamp.timeFlag);
            List<Object> data = new ArrayList<>();
            List<Object> dep = new ArrayList<>();
            for (Map map : massage) {
                data.add(map.get("num"));
                dep.add(map.get("station"));
            }
            temp.put("name", model);
            temp.put("type", "bar");
            temp.put("stack", "总量");
            Map<String, Object> elements = new HashMap<>();
            elements.put("show", "true");
            elements.put("position", "insideRight");
            temp.put("label", elements);
            temp.put("data", data);
            temp.put("dep", dep);
            result.add(temp);
        }
        return result;
    }

    @Override
    public Map<String, Object> PON_board_rate(String department, String olt, String time) {
//        List<Map<String, Object>> map = ponResourceMapper.PON_board_rate(department, olt, TimeStamp.timeFlag);
        Map<String, Object> result = new HashMap();
        int boardnum = ponResourceMapper.returnBoardNum(department, olt, TimeStamp.timeFlag);
        int totalnum = ponResourceMapper.returnTotalNum(department, olt, TimeStamp.timeFlag);
//        for (Map<String, Object> element : map) {
//            boardnum += (long) element.get("bordnum");
//            totalnum += (int) element.get("totalnum");
//        }
        double rate = (double) boardnum / totalnum;
        result.put("rate", String.format("%.2f", rate));
        return result;
    }

    @Override
    public List<Map<String, Object>> returnUserMassage(String department) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            int count = (int) (Math.pow(10, i * (-1)) * ponResourceMapper.countUser(TimeStamp.timeFlag));
            List<Map<String, Object>> users = ponResourceMapper.returnUserMassage(count, department, TimeStamp.timeFlag);
            Map<String, Object> temp = new HashMap<>();
            List<Object> data = new ArrayList<>();
            List<Object> dep = new ArrayList<>();
            for (Map user : users) {
                data.add(user.get("num"));
                dep.add(user.get("department"));
            }
//            DecimalFormat df = new DecimalFormat("0.0%");
            temp.put("name", Math.pow(10, i * (-1)) * 100 + "%");
            temp.put("type", "bar");
            temp.put("stack", "总量");
            Map<String, Object> elements = new HashMap<>();
            elements.put("show", "true");
            elements.put("position", "insideRight");
            temp.put("label", elements);
            temp.put("data", data);
            temp.put("dep", dep);
            result.add(temp);
        }
        return result;
    }

    @Override
    public Map<String, Object> PON_port_rate(String department, String olt, String time) {
        Map<String, Object> map = ponResourceMapper.PON_port_rate(department, olt, TimeStamp.timeFlag);
        return map;
    }

    @Override
    public List<Map<String, Object>> returnUser(String mode, double k) {
        int count = (int) (ponResourceMapper.countUser(TimeStamp.timeFlag) * k);
        System.out.println(count);
        System.out.println("============");
        System.out.println(mode);
        List<Map<String, Object>> result = ponResourceMapper.returnUser(TimeStamp.timeFlag, mode, count+"");
        for (Map<String, Object> map : result) {
            map.put("type", k * 100 + "%");
        }
        return result;
    }

    @Override
    public List<Object> departmentMassage() {
        List<Object> result = ponResourceMapper.departmentMassage();
        return result;
    }

    @Override
    public List<Object> stationMassage(String department) {
        List<Object> result = ponResourceMapper.stationMassage(department);
        return result;
    }

    @Override
    public List<Map<String, Object>> downloadTable(String name, String suffix) {
        List<Map<String, Object>> result = ponResourceMapper.downloadTable(name, suffix);
        return result;
    }

    @Override
    public Map<String, Object> trafficMassage(String speed) {
        Map<String, Object> res = new LinkedHashMap<>();

        List<Double> count = ponResourceMapper.countTraffic(speed);
        List<String> date = ponResourceMapper.countDate(speed);
        double avg = 0;
        double peak = 0;
        List<Double> avg_traffic = ponResourceMapper.countAvg(speed);
        List<Double> peak_traffic = ponResourceMapper.countPeak(speed);
        for (int i = 0; i < count.size(); i++) {
            double a = Double.parseDouble(String.format("%.2f", avg_traffic.get(i) / count.get(i)));
            double b = Double.parseDouble(String.format("%.2f", peak_traffic.get(i) / count.get(i)));
            avg_traffic.set(i, a);
            peak_traffic.set(i, b);
            avg += a;
            peak += b;
        }
        avg = avg / count.size();
        peak = peak / count.size();
        res.put("avg", avg_traffic);
        res.put("avg_mean", Double.parseDouble(String.format("%.2f", avg)));
        res.put("date", date);
        res.put("peak", peak_traffic);
        res.put("peak_mean", Double.parseDouble(String.format("%.2f", peak)));

        return res;
    }

    @Override
    public List<Map<String, Object>> PON_Port_Used(String department, String station, String olt) {
        List<Map<String, Object>> massage = ponResourceMapper.PON_Port_Used(TimeStamp.timeFlag, department, station, olt);

//        int total = Integer.parseInt(String.valueOf(massage.get("totalPort")));
//        int used_port_count = Integer.parseInt(String.valueOf(massage.get("usedPort")));
//        int idle_port_count = total - used_port_count;
        List<Map<String, Object>> result = new ArrayList<>();
        List<Object> data = new ArrayList<>();
        List<Object> data1 = new ArrayList<>();
        List<Object> dep = new ArrayList<>();
        Map<String, Object> temp = new HashMap<>();
        Map<String, Object> temp1 = new HashMap<>();
        for (Map map : massage) {
            int total = Integer.parseInt(String.valueOf(map.get("totalPort")));
            int used_port_count = Integer.parseInt(String.valueOf(map.get("usedPort")));
            int idle_port_count = total - used_port_count;
            data.add(used_port_count);
            data1.add(idle_port_count);
            dep.add(map.get("dep"));
        }
        temp.put("name", "占用端口");
        temp1.put("name", "空闲端口");
        temp.put("type", "bar");
        temp1.put("type", "bar");
        temp.put("stack", "总量");
        temp1.put("stack", "总量");
        Map<String, Object> elements = new HashMap<>();
        elements.put("show", "true");
        elements.put("position", "insideRight");
        temp.put("label", elements);
        temp1.put("label", elements);
        temp.put("data", data);
        temp1.put("data", data1);
        temp.put("dep", dep);
        temp1.put("dep", dep);
        result.add(temp);
        result.add(temp1);
        return result;
//        Map<String, Object> result = new HashMap<>();
//        result.put("idle_port_count", idle_port_count);
//        result.put("used_port_count", used_port_count);
//        double rate = total == 0 ? 0 : (double) used_port_count / total;
//        result.put("rate", rate);
//        return result;

    }


    @Override
    public List<String> returnDepartment() {
        List<String> list = ponResourceMapper.returnDepartment(TimeStamp.timeFlag);
        return list;
    }

    @Override
    public List<String> returnOLT(String department) {
        List<String> list = ponResourceMapper.returnOLT(department, TimeStamp.timeFlag);
        return list;
    }

    @Override
    public List<Map<String, Object>> pon_type(String department, String station) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> massage = ponResourceMapper.pon_type(TimeStamp.timeFlag, department, station);
        List<Object> data = new ArrayList<>();
        List<Object> data1 = new ArrayList<>();
        List<Object> dep = new ArrayList<>();
        Map<String, Object> temp = new HashMap<>();
        Map<String, Object> temp1 = new HashMap<>();
        for (Map map : massage) {
            data.add(map.get("1GE_count"));
            data1.add(map.get("10GE_count"));
            dep.add(map.get("dep"));
        }
        temp.put("name", "EPON");
        temp1.put("name", "10GEPON");
        temp.put("type", "bar");
        temp1.put("type", "bar");
        temp.put("stack", "总量");
        temp1.put("stack", "总量");
        Map<String, Object> elements = new HashMap<>();
        elements.put("show", "true");
        elements.put("position", "insideRight");
        temp.put("label", elements);
        temp1.put("label", elements);
        temp.put("data", data);
        temp1.put("data", data1);
        temp.put("dep", dep);
        temp1.put("dep", dep);
        result.add(temp);
        result.add(temp1);
        return result;
    }


    @Override
    public List<Map<String, Object>> homeMassage() {
        List<Map<String, Object>> message = ponResourceMapper.homeMassage(TimeStamp.timeFlag);
        Map<String, Object> total = new HashMap<>();
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        for (Map<String, Object> map : message) {
//            a += Integer.parseInt(String.valueOf(map.get("a")));
            b += Integer.parseInt(String.valueOf(map.get("b")));
            c += Integer.parseInt(String.valueOf(map.get("c")));
            d += Integer.parseInt(String.valueOf(map.get("d")));
            e += Integer.parseInt(String.valueOf(map.get("e")));

        }
        total.put("a", "Acity");
        total.put("b", b);
        total.put("c", c);
        total.put("d", d);
        total.put("e", e);
        message.add(0, total);
        return message;
    }

    @Override
    public Map<String, Object> returnTree(String department) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> oltList = ponResourceMapper.OLTTree(department, TimeStamp.timeFlag);
        int total = 0;
        result.put("name", "OLT");
        for (Map<String, Object> map : oltList) {
            Map<String, Object> temp = new LinkedHashMap<>();
            System.out.println(oltList);
            String OLTName = (String) map.get("设备类型");
//            String OLTName = (String) map.get("设备名称");
            int OLTNum = Integer.parseInt(map.get("number").toString());
            total += OLTNum;
            temp.put("name", OLTName);
            temp.put("value", OLTNum);
            System.out.println(temp);
            List<Map<String, Object>> list1 = new ArrayList<>();
            Map<String, Object> onePonBoardMap = new LinkedHashMap<>();
            Map<String, Object> tenPonBoardMap = new LinkedHashMap<>();
//            System.out.println(1);
            Map<String, Object> PONBoardList = ponResourceMapper.PONBoardTree(department, OLTName, TimeStamp.timeFlag);
            System.out.println(PONBoardList);
            int oneGPON = PONBoardList.get("1G") == null ? 0 : Integer.parseInt(PONBoardList.get("1G").toString());
            int tenGPON = PONBoardList.get("10G") == null ? 0 : Integer.parseInt(PONBoardList.get("10G").toString());
            onePonBoardMap.put("name", "1GPON板");
            onePonBoardMap.put("value", oneGPON);
            List<Map<String, Object>> list2 = new ArrayList<>();
            tenPonBoardMap.put("name", "10GPON板");
            tenPonBoardMap.put("value", tenGPON);
            Map<String, String> color = new HashMap<>();
            color.put("color", "#D33513");
            tenPonBoardMap.put("itemStyle", color);
            List<Map<String, Object>> list3 = new ArrayList<>();
//            temp.put("1GPON板",oneGPON);
//            temp.put("10GPON板",tenGPON);
            Map<String, Object> onePonPortMap = new LinkedHashMap<>();
            Map<String, Object> tenPonPortMap = new LinkedHashMap<>();
            Map<String, Object> totalList = ponResourceMapper.totalTree(department, OLTName, TimeStamp.timeFlag);
            int onePONPort = totalList.get("1GPONPort") == null ? 0 : Integer.parseInt(totalList.get("1GPONPort").toString());
            int oneUser = totalList.get("1GTotalUser") == null ? 0 : Integer.parseInt(totalList.get("1GTotalUser").toString());
            int tenPONPort = totalList.get("10GPONPort") == null ? 0 : Integer.parseInt(totalList.get("10GPONPort").toString());
            int tenUser = totalList.get("10GTotalUser") == null ? 0 : Integer.parseInt(totalList.get("10GTotalUser").toString());
            onePonPortMap.put("name", "1GPON口");
            onePonPortMap.put("value", onePONPort);
            List<Map<String, Object>> list4 = new ArrayList<>();
            tenPonPortMap.put("name", "10GPON口");
            tenPonPortMap.put("value", tenPONPort);
            tenPonPortMap.put("itemStyle", color);
            List<Map<String, Object>> list5 = new ArrayList<>();
//            temp.put("1GPON口",onePONPort);
            Map<String, Object> oneGUserMap = new LinkedHashMap<>();
            Map<String, Object> tenGUserMap = new LinkedHashMap<>();
            oneGUserMap.put("name", "用户");
            oneGUserMap.put("value", oneUser);
            oneGUserMap.put("children", null);
            tenGUserMap.put("name", "用户");
            tenGUserMap.put("value", tenUser);
            tenGUserMap.put("itemStyle", color);
            tenGUserMap.put("children", null);
            list4.add(oneGUserMap);
            list5.add(tenGUserMap);
            onePonPortMap.put("children", list4);
            tenPonPortMap.put("children", list5);
            list2.add(onePonPortMap);
            list3.add(tenPonPortMap);
            onePonBoardMap.put("children", list2);
            tenPonBoardMap.put("children", list3);
            list1.add(onePonBoardMap);
            list1.add(tenPonBoardMap);
            temp.put("children", list1);
//            temp.put("1G用户数",oneUser);
////            temp.put("10GPON口",tenPONPort);
//            temp.put("10G用户数",tenUser);
            list.add(temp);
        }
        result.put("value", total);
        result.put("children", list);
        return result;
    }

    @Override
    public List<Map<String, Object>> returnTime(String time, String option) {
        List<Map<String, Object>> list;
        if (option.equals("根据表名")) {
            time = "Timestamp" + time;
            System.out.println(time);
            list = ponResourceMapper.returnTimeByTable(time);
            for (Map<String, Object> map : list) {
                map.put("content", "库表数据");
            }

        } else {
            System.out.println(time);
            if (time.equals("20190708")) {
                list = ponResourceMapper.returnTimeByFiled1(time);
            } else {
                list = ponResourceMapper.returnTimeByFiled2(time);
            }

            for (Map<String, Object> map : list) {
                map.put("content", "字段数据");
            }
        }

        return list;
    }

    @Override
    public List<List<Object>> returnMap() {
        List<List<Object>> result = new ArrayList<>();
        List<String> olt = ponResourceMapper.mapOLT();
        List<Map<String, Object>> list = ponResourceMapper.returnMap();
        for (String OLT : olt) {
            List<Object> temp = new ArrayList<>();
            for (Map<String, Object> message : list) {
                if (message.get("olt").equals(OLT)) {
                    temp.add(message);
                }
            }
            result.add(temp);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> returnTianjin() {
        List<Map<String, Object>> list = ponResourceMapper.returnTianjin();
        return list;
    }

    @Override
    public List<Map<String, Object>> returnNewTianjin() {
        List<Map<String, Object>> list = ponResourceMapper.returnNewTianjin();
        return list;
    }

    @Override
    public boolean returnRectification(String name, String lng, String lat) {

        try {
            ponResourceMapper.returnRectification(name, lng, lat);
            return true;

        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public void truncateTable() {
        ponResourceMapper.truncateTable();
    }

    @Override
    public List<Map<String, Object>> returnTianjinNumber() {
        List<Map<String, Object>> list1 = ponResourceMapper.returnOldTianjinNumber();
        List<Map<String, Object>> list2 = ponResourceMapper.returnNewTianjinNumber();
        list1.addAll(list2);
        return list1;

    }


}
