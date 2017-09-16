package cn.edu.seu.display.controller;

/*
根据前端所给参数  从mysql返回所需数据
 */
import cn.edu.seu.display.model.Timedata;
import net.sf.json.JSONArray;

import java.sql.*;
import java.util.ArrayList;

public class TimeController {

    public static String driverName = "com.mysql.jdbc.Driver";

    public static JSONArray timeGet(String time, String city) {                                                          //参数为时间与城市，返回JSON格式数据
        Connection con = null;
        JSONArray resJSON = new JSONArray();
        try {
            String url = "jdbc:mysql://WuRui001:3306/logs?useUnicode=true&characterEncoding=utf-8";
            String user = "root";
            String pwd = "123456";
            Class.forName(driverName);
            con = DriverManager.getConnection(url, user, pwd);                       //注意数据库名称，此处为logs
            Statement stmt = con.createStatement();

            String getContent = "SELECT * FROM Time" + time + " WHERE city=" + city;
            ResultSet res = stmt.executeQuery(getContent);
            int data[] = new int[24];
            ArrayList<Timedata> datalist=new ArrayList<Timedata>();
            for(int i=0;i<24;i++) {
                datalist.add(new Timedata());
                datalist.get(i).setHour(i);
            }
            int i=0;
            while (res.next()) {
                int hour = res.getInt("time");
                datalist.get(hour).addPVnum(res.getInt("num"));
                datalist.get(hour).addUVnum(1);
            }

            resJSON = JSONArray.fromObject(datalist);
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

    public static void main(String args[]) {
        System.out.println(timeGet("20170102","20"));
    }
}

