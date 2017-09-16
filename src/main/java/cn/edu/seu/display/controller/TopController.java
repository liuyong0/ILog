package cn.edu.seu.display.controller;

import cn.edu.seu.display.model.Urldata;
import net.sf.json.JSONArray;

import java.sql.*;
import java.util.*;

public class TopController {

    public static String driverName = "com.mysql.jdbc.Driver";

    public static JSONArray topGet(String time, String kind) {                                                          //参数为时间与城市，返回JSON格式数据
        Connection con = null;
        JSONArray resJSON = new JSONArray();
        try {
            String url = "jdbc:mysql://WuRui001:3306/logs?useUnicode=true&characterEncoding=utf-8";
            String user = "root";
            String pwd = "123456";
            Class.forName(driverName);
            con = DriverManager.getConnection(url, user, pwd);                      //注意数据库名称，此处为logs
            Statement stmt = con.createStatement();


            String getContent = "SELECT url,num FROM Top" + time;
            ResultSet res = stmt.executeQuery(getContent);
            Map<String,Integer> urlnum = new HashMap<String,Integer>();
            while (res.next()) {
               if(kind=="PV"){
                   String urlnow=res.getString("url");
                   if(urlnum.containsKey(urlnow)){
                       int oldnum=urlnum.get(urlnow);
                       int newnum=oldnum+res.getInt("num");
                       urlnum.put(urlnow,newnum);
                   } else {
                       urlnum.put(urlnow,res.getInt("num"));
                   }
               }
               if(kind=="UV"){
                   String urlnow=res.getString("url");
                   if(urlnum.containsKey(urlnow)){
                       int oldnum=urlnum.get(urlnow);
                       urlnum.put(urlnow,oldnum+1);
                   } else {
                       urlnum.put(urlnow,1);
                   }
               }
            }
            ArrayList<Map.Entry<String,Integer>> entries= sortMap(urlnum);
            ArrayList<Urldata> resArray = new ArrayList<Urldata>();
            for( int i=0;i<20;i++){
                Urldata tmp = new Urldata(i+1,entries.get(i).getKey(),entries.get(i).getValue());
                resArray.add(tmp);
            }
            resJSON=JSONArray.fromObject(resArray);
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

    public static ArrayList<Map.Entry<String,Integer>> sortMap(Map map){
        List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> obj1 , Map.Entry<String, Integer> obj2) {
                return obj2.getValue() - obj1.getValue();
            }
        });
        return (ArrayList<Map.Entry<String, Integer>>) entries;
    }

    public static void main(String args[]) {
       System.out.println(topGet("20170102","PV"));
    }
}