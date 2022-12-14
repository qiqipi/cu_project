package com.liantong.demo_part2.Service.Impl;

import com.liantong.demo_part2.Mapper.PON2022MigrationMapper;
import com.liantong.demo_part2.Service.PON2022MigrationService;
import com.liantong.demo_part2.Utils.ComputeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

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
        double channelInPeekPreMin1 = Double.MAX_VALUE;
        double channelInPeekPreMin2 = 0;
        double channelInPeekPreMax2 = Double.MAX_VALUE;
        double channel1TendencyMin = 0;
        double channel1TendencyMax = Double.MAX_VALUE;
        double channel2TendencyMin = 0;
        double channel2TendencyMax = Double.MAX_VALUE;
        for(String OLT_name : OLT_names){
            List<Map<String, Object>> planTables = pon2022MigrationMapper.getPlanTable(OLT_name);
            for(Map<String,Object> planTable : planTables){
                String  PONBoard = (String) planTable.get("pon_board_number");
                String  PONPort = (String) planTable.get("pon_port_number");
                String  OLTName = (String) planTable.get("olt_name");
                //????????????
                String trafficPred1 = ComputeUtils.getPredict(pon2022MigrationMapper.getChannel1InPeek(OLTName, PONBoard, PONPort));
                pon2022MigrationMapper.updatePlanTable(OLT_name,trafficPred1,"channel1_out_pred_max",PONBoard,PONPort);
                planTable.put("channel1_out_pred_max",trafficPred1);
                //?????????
                planTable.put("channel1_tendency",1);
                //???????????????
                String trafficUsage1 = ComputeUtils.computeTrafficUsage1((String) planTable.get("channel1_out_peek_max"),(String) planTable.get("technical"));
                planTable.put("channel1_out_peek_max_usage", trafficUsage1);
                pon2022MigrationMapper.updatePlanTable(OLT_name,trafficUsage1,"channel1_out_peek_max_usage",PONBoard,PONPort);
                if(planTable.containsKey("channel2_out_peek_max")){
                    //??????2??????
                    String trafficPred2 = ComputeUtils.getPredict(pon2022MigrationMapper.getChannel2InPeek(OLTName, PONBoard, PONPort));
                    pon2022MigrationMapper.updatePlanTable(OLT_name,trafficPred2,"channel2_out_pred_max",PONBoard,PONPort);
                    planTable.put("channel2_out_pred_max",trafficPred2);
                    //??????2?????????
                    String trafficUsage2 = ComputeUtils.computeTrafficUsage2((String) planTable.get("channel2_out_peek_max"),(String) planTable.get("technical"));
                    planTable.put("channel2_out_peek_max_usage", trafficUsage2);
                    pon2022MigrationMapper.updatePlanTable(OLT_name,trafficUsage2,"channel2_out_peek_max_usage",PONBoard,PONPort);

                    //??????2?????????
                    channelInPeekPreMax2 = Math.max(channelInPeekPreMax1,Double.valueOf(trafficPred2));
                    channelInPeekPreMin2 = Math.min(channelInPeekPreMin1,Double.valueOf(trafficPred2));
                    channelInPeekMax2 = Math.max(channelInPeekMax2,Double.parseDouble((String) planTable.get("channel2_out_peek_max")));
                    channelInPeekMin2 = Math.min(channelInPeekMin2,Double.parseDouble((String) planTable.get("channel2_out_peek_max")));
                    channelInAvgMax2 = Math.max(channelInAvgMax2,Double.parseDouble((String) planTable.get("channel2_out_avg_max")));
                    channelInAvgMin2 = Math.min(channelInAvgMin2,Double.parseDouble((String) planTable.get("channel2_out_avg_max")));
                }

//                ????????????
                channelInPeekPreMax1 = Math.max(channelInPeekPreMax1,Double.valueOf(trafficPred1));
                channelInPeekPreMin1 = Math.min(channelInPeekPreMin1,Double.valueOf(trafficPred1));
                channelInPeekMax1 = Math.max(channelInPeekMax1,Double.parseDouble((String) planTable.get("channel1_out_peek_max")));
                channelInPeekMin1 = Math.min(channelInPeekMin1,Double.parseDouble((String) planTable.get("channel1_out_peek_max")));
                channelInAvgMax1 = Math.max(channelInAvgMax1,Double.parseDouble((String) planTable.get("channel1_out_avg_max")));
                channelInAvgMin1 = Math.min(channelInAvgMin1,Double.parseDouble((String) planTable.get("channel1_out_avg_max")));


                res.add(planTable);
            }

        }
        for (Map<String ,Object> planTable : res){
            String  channel2_out_peek_max = "0";
            String  channel2_out_avg_max = "0";
            String  channel2_out_pred_max = "0";
            double channel1MaxIn = Double.parseDouble((String) planTable.get("channel1_out_peek_max"));
            double channel1AvgIn = Double.parseDouble((String) planTable.get("channel1_out_avg_max"));
            double channel1PreIn = Double.parseDouble((String) planTable.get("channel1_out_pred_max"));
            double channel1_out_peek_max = (channel1MaxIn - channelInPeekMin1) / (channelInPeekMax1 - channelInPeekMin1);
            double channel1_out_avg_max = (channel1AvgIn - channelInAvgMin1) / (channelInAvgMax1 - channelInAvgMin1);
            double channel1_out_pred_max = (channel1PreIn - channelInPeekPreMin1) / (channelInPeekPreMax1 - channelInPeekPreMin1);
            if(planTable.containsKey("channel2_out_peek_max")){
                double channel2MaxIn = Double.parseDouble((String) planTable.get("channel2_out_peek_max"));
                double channel2AvgIn = Double.parseDouble((String) planTable.get("channel2_out_avg_max"));
                channel2_out_peek_max = String.valueOf((channel2MaxIn - channelInPeekMin2) / (channelInPeekMax2 - channelInPeekMin2));
                channel2_out_avg_max = String.valueOf((channel2AvgIn - channelInAvgMin2) / (channelInAvgMax2 - channelInAvgMin2));
                double channel2PreIn = Double.parseDouble((String) planTable.get("channel2_out_pred_max"));
                channel2_out_pred_max = String.valueOf((channel2PreIn - channelInPeekPreMin2) / (channelInPeekPreMax2 - channelInPeekPreMin2));
            }
            pon2022MigrationMapper.insertStandardOLTChosenTable((String) planTable.get("olt_concat"),(String) planTable.get("olt_name"),(String) planTable.get("pon_board_number"),(String)planTable.get("pon_port_number"),
                    String.valueOf(channel1_out_peek_max),String.valueOf(channel1_out_avg_max),channel2_out_peek_max,channel2_out_avg_max,(String)planTable.get("channel1_out_peek_max_time")
            ,(String) planTable.get("channel2_out_peek_max_time"),String.valueOf(channel1_out_pred_max),channel2_out_pred_max,"1","1");
        }
        return res;
    }


    @Override
    public List<Map<String, Object>> getRank_table(String values[]){
        pon2022MigrationMapper.updateStandardOLTChosenTable(values[0],values[1],values[2],values[3]);
        System.out.println("?????????????????????");
        List<Map<String, Object>> standardOLTChosenTable;
        if(values[4].equals("1")){
            standardOLTChosenTable = pon2022MigrationMapper.getStandardOLTChosenTable1();
        }else {
            standardOLTChosenTable = pon2022MigrationMapper.getStandardOLTChosenTable2();
        }

        return standardOLTChosenTable;
    }

    @Override
    public List<Map<String, Object>> getMigrationTable(String[] values) {
        List<Map<String,Object>> res = new ArrayList<>();
        double door = 0.3;
        double door1 = 0.1;
        for(String value : values){
            Map<String, Object> migrationTable = pon2022MigrationMapper.getMigrationTable(value);
            String tech = (String) migrationTable.get("technical");
            double flow1 = Double.parseDouble((String) migrationTable.getOrDefault("channel1_out_peek_max","0")) * 5;
            double flow2 = Double.parseDouble((String) migrationTable.getOrDefault("channel2_out_peek_max","0")) * 5;
            if(tech.equals("EPON")){
                migrationTable.put("result","PON?????????10G EPON?????????????????????");
            }
            if(tech.equals("GPON")){
                migrationTable.put("result","PON?????????Combo PON?????????????????????");
            }
            if(tech.equals("XG PON") || tech.equals("XGS PON")){
                if(flow1 > door * 10000){
                    migrationTable.put("result","????????????");
                }else{
                    migrationTable.put("result","??????");
                }
            }
            if(tech.equals("10G EPON")){
                if(flow1 > 2500 * door1 && flow2 > 10000 * door){
                    migrationTable.put("result","??????1???????????????2???????????????");
                }
                if(flow1 > 2500 * door1 && flow2 < 10000 * door){
                    migrationTable.put("result","??????1???????????????2");
                }
                if(flow1 < 2500 * door1 && flow2 > 10000 * door){
                    migrationTable.put("result","????????????");
                }
                if(flow1 < 2500 * door1 && flow2 < 10000 * door){
                    migrationTable.put("result","??????");
                }
            }
            if(tech.equals("Combo PON(XG PON)") || tech.equals("Combo PON(XGS PON)")){
                if(flow1 > 2500 * door1 && flow2 > 10000 * door){
                    migrationTable.put("result","??????1???????????????2???????????????");
                }
                if(flow1 > 2500 * door1&& flow2 < 10000 * door){
                    migrationTable.put("result","??????1???????????????2");
                }
                if(flow1 < 2500 * door1 && flow2 > 10000 * door){
                    migrationTable.put("result","????????????");
                }
                if(flow1 < 2500 * door1 && flow2 < 10000 * door){
                    migrationTable.put("result","??????");
                }
            }
            migrationTable.remove("channel1_out_peek_max");
            migrationTable.remove("channel2_out_peek_max");
            res.add(migrationTable);
        }

        return res;
    }





    @Override
    public boolean createMergeTable() throws Exception {
        pon2022MigrationMapper.dropMergeTable();
        pon2022MigrationMapper.createMergeTable();
        pon2022MigrationMapper.initMergeTable();
        pon2022MigrationMapper.createCoList();
        try {
        }catch (Exception e){
            throw new Exception("??????merge?????????");
        }
        System.out.println("merge????????????");
        return true;
    }


    @Override
    public boolean createOLTChosenTable() throws Exception {

        pon2022MigrationMapper.createOLTChosenTable();
        pon2022MigrationMapper.insertOLTChosenTable();
        try {
        }catch (Exception e){
            throw new Exception("??????OLTChosen?????????");
        }
        System.out.println("OLTChosen??????");
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
            throw new Exception("??????PlanTable?????????");
        }
        System.out.println("PlanTable??????");
        return true;
    }



}
