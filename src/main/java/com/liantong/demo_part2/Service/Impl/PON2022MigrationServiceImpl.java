package com.liantong.demo_part2.Service.Impl;

import com.liantong.demo_part2.Mapper.PON2022MigrationMapper;
import com.liantong.demo_part2.Service.PON2022MigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author qiqipi
 * @create 2022/7/10 6:12
 */
@Service
public class PON2022MigrationServiceImpl implements PON2022MigrationService {

    @Autowired
    PON2022MigrationMapper pon2022MigrationMapper;

    @Override
    public List<Map<String, Object>> getOLTChosenTable() {
        List<Map<String, Object>> aaaTable = pon2022MigrationMapper.getOLTChosenTable();
        return aaaTable;
    }


    public List<Map<String,Object>> getPlanTable(String[] OLT_names){
        List<Map<String,Object>> res = new ArrayList<>();
        double channelInPeekMax1 = 0;
        double channelInPeekMin1 = Double.MAX_VALUE;
        double channelInAvgMax1 = 0;
        double channelInAvgMin1 = Double.MAX_VALUE;
        double channelInPeekMax2 = 0;
        double channelInPeekMin2 = Double.MAX_VALUE;
        double channelInAvgMax2 = 0;
        double channelInAvgMin2 = Double.MAX_VALUE;
        double channelInPeekPreMax1 = 0;
        double channelInPeekPreMin1 = Integer.MAX_VALUE;
        int i = 9999;
        for(String OLT_name : OLT_names){
            List<Map<String, Object>> planTables = pon2022MigrationMapper.getPlanTable(OLT_name);
            for(Map<String,Object> planTable : planTables){
                String  PONBoard = (String) planTable.get("pon_board_number");
                String  PONPort = (String) planTable.get("pon_port_number");
                pon2022MigrationMapper.updatePred(OLT_name,"111.9","channel1_in_pred_max",PONBoard,PONPort);
                pon2022MigrationMapper.updatePred(OLT_name,"112.9","channel2_in_pred_max",PONBoard,PONPort);
                pon2022MigrationMapper.updatePred(OLT_name,String.valueOf(i),"pon_rank",PONBoard,PONPort);
                i--;
                planTable.put("channel1_in_pred_max",111.9);
                planTable.put("channel1_in_pred_max",112.9);
                planTable.put("channel1_tendency",1);
                planTable.put("channel2_tendency",0);
//                double predict1 = getPredict1(OLT_name, PONBoard, PONPort);
//                planTable.put("channel1_in_pred_max",predict1);
//                channelInPeekPreMax1 = Math.max(channelInPeekPreMax1,predict1);
//                channelInPeekPreMin1 = Math.min(channelInPeekPreMin1,predict1);
                channelInPeekMax1 = Math.max(channelInPeekMax1,Double.parseDouble((String) planTable.get("channel1_in_peek_max")));
                channelInPeekMin1 = Math.min(channelInPeekMin1,Double.parseDouble((String) planTable.get("channel1_in_peek_max")));
                channelInAvgMax1 = Math.max(channelInAvgMax1,Double.parseDouble((String) planTable.get("channel1_in_avg_max")));
                channelInAvgMin1 = Math.min(channelInAvgMin1,Double.parseDouble((String) planTable.get("channel1_in_avg_max")));
                if(planTable.containsKey("channel2_in_peek_max")){
                    channelInPeekMax2 = Math.max(channelInPeekMax2,Double.parseDouble((String) planTable.get("channel2_in_peek_max")));
                    channelInPeekMin2 = Math.min(channelInPeekMin2,Double.parseDouble((String) planTable.get("channel2_in_peek_max")));
                    channelInAvgMax2 = Math.max(channelInAvgMax2,Double.parseDouble((String) planTable.get("channel2_in_avg_max")));
                    channelInAvgMin2 = Math.min(channelInAvgMin2,Double.parseDouble((String) planTable.get("channel2_in_avg_max")));
                }
                res.add(planTable);
            }

        }
        for (Map<String ,Object> planTable : res){
            String  channel2_in_peek_max = "";
            String  channel2_in_avg_max = "";
            double channel1MaxIn = Double.parseDouble((String) planTable.get("channel1_in_peek_max"));
            double channel1AvgIn = Double.parseDouble((String) planTable.get("channel1_in_avg_max"));
//            double channel1PreIn = (double) planTable.get("channel1_in_pred_max");
            double channel1_in_peek_max = (channel1MaxIn - channelInPeekMin1) / (channelInPeekMax1 - channelInPeekMin1);
            double channel1_in_avg_max = (channel1AvgIn - channelInAvgMin1) / (channelInAvgMax1 - channelInAvgMin1);
//            planTable.put("channel1_in_pred_max",(channel1PreIn - channelInPeekPreMin1) / (channelInPeekPreMax1 - channelInPeekPreMin1));
            if(planTable.containsKey("channel2_in_peek_max")){
                double channel2MaxIn = Double.parseDouble((String) planTable.get("channel2_in_peek_max"));
                double channel2AvgIn = Double.parseDouble((String) planTable.get("channel2_in_avg_max"));
                channel2_in_peek_max = String.valueOf((channel2MaxIn - channelInPeekMin2) / (channelInPeekMax2 - channelInPeekMin2));
                channel2_in_avg_max = String.valueOf((channel2AvgIn - channelInAvgMin2) / (channelInAvgMax2 - channelInAvgMin2));
            }
            pon2022MigrationMapper.insertStandardOLTChosenTable((String) planTable.get("olt_concat"),(String) planTable.get("olt_name"),(String) planTable.get("pon_board_number"),
                    String.valueOf(channel1_in_peek_max),String.valueOf(channel1_in_avg_max),channel2_in_peek_max,channel2_in_avg_max,(Date) planTable.get("channel1_in_peek_max_time")
            ,(Date) planTable.get("channel2_in_peek_max_time"));
        }
        return res;
    }

    @Override
    public double getPredict1(String OLTName, String PONBoard, String PONPort) {
        List<Double> list = pon2022MigrationMapper.getChannel1InPeek(OLTName,PONBoard,PONPort);
        double [] data = new double[list.size()];
        for(int i = 0;i < list.size();i++){
            data[i] = list.get(i);
        }
        double[] newData = data.clone();
        int count = newData.length;
        double s = 0.0;
        int i = 0;
        while (i<=30){
            s = 0.0;
            for (int j = 0; j < count; j++)
            {
                s += (j + 1) * newData[j];
            }
            s /= (count * (count + 1) / 2);
            for (int j = 0; j < count - 1; j++)
            {
                newData[j] = newData[j + 1];
            }
            newData[count - 1] = s;

            double nd =newData[newData.length-1];
            double cs = 0;
            int js = 0;
            for(int h=0; h<newData.length-1; h++){
                cs = newData[h];
                if(nd == cs){
                    js++;
                }
            }
            if(js == newData.length-1){
                break;
            }
            i++;
        }
        String  str = String.format("%.2f",s);
        return Double.parseDouble(str);
    }



    @Override
    public boolean createMergeTable() throws Exception {
        pon2022MigrationMapper.dropMergeTable();
        pon2022MigrationMapper.createMergeTable();
        pon2022MigrationMapper.initMergeTable();

        try {
        }catch (Exception e){
            throw new Exception("生成merge表出错");
        }
        System.out.println("merge表格完成");
        return true;
    }


    @Override
    public boolean createOLTChosenTable() throws Exception {

        pon2022MigrationMapper.createOLTChosenTable();
        pon2022MigrationMapper.insertOLTChosenTable();
        try {
        }catch (Exception e){
            throw new Exception("生成OLTChosen表出错");
        }
        System.out.println("OLTChosen完成");
        return true;
    }


    public List<Map<String,Object>> getRegion(){
        List<Map<String, Object>> region = pon2022MigrationMapper.getRegion();
        return region;
    }


    public boolean createPlanTable(String []time) throws Exception {
        String time1 = time[0].substring(0,10);
        String time2 = time[1].substring(0,10);
        pon2022MigrationMapper.createPlanTable();
        pon2022MigrationMapper.insertPlanTable1(time1,time2);
        pon2022MigrationMapper.createStandardOLTChosenTable();
        try {
        }catch (Exception e){
            throw new Exception("生成PlanTable表出错");
        }
        System.out.println("PlanTable完成");
        return true;
    }

}
