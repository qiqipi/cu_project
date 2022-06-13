package com.liantong.demo_part2.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 
 *
 */
public class JsonUtils {
	private final static ObjectMapper objectMapper = new ObjectMapper();
	 public static ObjectMapper getInstance() {
	        return objectMapper;
	    }
	/**
     * 把JSON数据转换成指定的java对象
     * @param jsonData JSON数据
     * @param clazz 指定的java对象
     * @return 指定的java对象
     */
    public static <T> T getJsonToBean(String jsonData, Class<T> clazz) {
        return JSON.parseObject(jsonData, clazz);
    }
    /**
     * List集合转换成jsonArray
     * @param obj
     * @return
     */
    public static JSONArray listToJson(Object obj) throws Exception {
        return JSON.parseArray(JSON.toJSONString(obj));
    }

    /**
     * java对象转换成JSON数据
     * @param object java对象
     * @return JSON数据
     */
    public static Object getBeanToJson(Object object) {
    	
        return JSONObject.toJSON(object);
    }

    /**
     * JSON数据转换成指定的java对象列表
     * @param jsonData JSON数据
     * @param clazz 指定的java对象
     * @return List<T>
     */
    public static <T> List<T> getJsonToList(String jsonData, Class<T> clazz) {
        return JSON.parseArray(jsonData, clazz);
    }

    /**
     * 把JSON数据转换成较为复杂的List<Map<String, Object>>
     * @param jsonData JSON数据
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getJsonToListMap(String jsonData) {

        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        //第一次分割
        String[] splitArray = jsonData.split("},");
        for(int i=0; i<splitArray.length;i++) {
        //	if(i==splitArray.length-1) {
        //		splitArray[i]=splitArray[i].substring(1, splitArray[i].length()-1);
        //	}else {
        //		splitArray[i]=splitArray[i].substring(1, splitArray[i].length());
        //	}
            splitArray[i]=splitArray[i].substring(1, splitArray[i].length());
            Map<String,Object> map = new HashMap<String,Object>();
            //第二次分割
            String[] mapArray = splitArray[i].split(",");
            for(int j=0 ;j<mapArray.length ;j++) {
                String str = mapArray[j].replaceAll("\"", "");
                //第三次分割,为了防止value为空，下面加了一个长度判断
                String[] keyValue = str.split(":");
                if(keyValue.length==2) map.put(keyValue[0], keyValue[1]);
                else map.put(keyValue[0], "");
            }
            list.add(map);
                }
        return list;
            }
}
