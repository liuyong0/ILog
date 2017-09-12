package cn.edu.seu.display.controller;

import net.sf.json.JSONArray;

import java.sql.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TimeController {

    public static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static JSONArray timeGet(String time, String city) {
        Connection con = null;
        JSONArray resJSON =new JSONArray();
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection("jdbc:hive2://WuRui001:10000/logs", "root", "123456");                       //注意数据库名称，此处为logs
            Statement stmt = con.createStatement();

            String getContent = "SELECT * FROM time" + time + " WHERE city=" + city;
            ResultSet res = stmt.executeQuery(getContent);
            int PV[] = new int[24];
            int UV[] = new int[24];
            while (res.next()) {
                int hour = res.getInt("hour");
                PV[hour] += res.getInt("num");
                UV[hour] += 1;
            }
            Map<String,Object>  pvmap= new TreeMap<String, Object>(
                    new Comparator<String>() {
                        public int compare(String obj1, String obj2) {
                            return obj1.compareTo(obj2);
                        }
                    }
            );
            for(int i=0;i<24;i++){
                pvmap.put(i<10?'0'+String.valueOf(i):String.valueOf(i),PV[i]);
            }
            Map<String,Object>  uvmap= new TreeMap<String, Object>(
                    new Comparator<String>() {
                        public int compare(String obj1, String obj2) {
                            return obj1.compareTo(obj2);
                        }
                    }
            );
            for(int i=0;i<24;i++){
                uvmap.put(i<10?'0'+String.valueOf(i):String.valueOf(i),UV[i]);
            }
            Map<String, Map<String,Object>> map = new HashMap<String, Map<String,Object>>();
            map.put("PV", pvmap);
            map.put("UV", uvmap);
            resJSON = JSONArray.fromObject(map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resJSON;
    }

    public static void main(String args[]){
       System.out.print(timeGet("20170105","20"));
    }
}
