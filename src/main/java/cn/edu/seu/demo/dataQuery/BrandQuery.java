package cn.edu.seu.demo.dataQuery;

import java.sql.*;

public class BrandQuery {
    public  static String driverName = "org.apache.hive.jdbc.HiveDriver";


    /**
     * 查询全国所有用户的品牌或型号总量数据
     * 时间格式:20170101
     * 返回内容格式：
     * 20170101:华为:12	三星:10	中兴:9	LG:8	联想:7	小米:6	魅族:6	OPPO:5	vivo:5	金立:4
     * 20170102:华为:12	三星:10	中兴:9	LG:8	联想:7	魅族:6	OPPO:5	小米:5	vivo:5	金立:4
     * 20170103:华为:12	三星:10	中兴:9	LG:8	联想:7	魅族:6	OPPO:5	小米:5	vivo:5	金立:4
     */
    public static String queryBrandCountry(String type,String timeStart,String timeEnd){
        Connection con = null;
        ResultSet rs = null;
        String str = "";
        for(int time =Integer.valueOf(timeStart);time<=Integer.valueOf(timeEnd);time++) {
            str += time + ":";
            try {
                Class.forName(driverName);
                con = DriverManager.getConnection("jdbc:hive2://ilog:10000/logs", "root", "123456"); //注意数据库名称，此处为logs
                Statement stmt = con.createStatement();

                //查询棑前10的手机品牌
                String querySQL = "select "+ type +",count(*) as userNum from " + time + "_b group by "+type+
                        " order by userNum desc limit 10";

                rs = stmt.executeQuery(querySQL);

                while (rs.next()) {
                    str += rs.getString("brand") + ":" + rs.getString("userNum") + "\t";
                }

                str += "\n";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(str);
        return str;
    }



    /**
     * 查询省用户的品牌或型号总量数据
     * 参数：省 品牌或型号 开始日期 结束日期
     * 返回内容格式：
     * 20170101:中兴:2	联想:1	格力:1	LG:1	vivo:1
     * 20170102:中兴:2	联想:1	格力:1	LG:1	vivo:1
     * 20170103:中兴:2	联想:1	格力:1	LG:1	vivo:1
     */
    public static String queryBrandPrivance(String privance,String type,String timeStart,String timeEnd){
        if(type.equals("品牌"))
            type = "brand";
        if(type.equals("型号"))
            type = "model";

        Connection con = null;
        ResultSet rs = null;
        String str = "";
        for(int time =Integer.valueOf(timeStart);time<=Integer.valueOf(timeEnd);time++) {
            str += time + ":";
            try {
                Class.forName(driverName);
                con = DriverManager.getConnection("jdbc:hive2://ilog:10000/logs", "root", "123456"); //注意数据库名称，此处为logs
                Statement stmt = con.createStatement();

                //查询棑前10的手机品牌或型号
                String querySQL = "select "+type+",count(*) as userNum from "+time+"_b where city in (select city_code from city where city_name like'" +privance+"%')" +
                        " group by " +type+" order by userNum desc limit 10";

                rs = stmt.executeQuery(querySQL);
                while (rs.next()) {
                    str += rs.getString(type) + ":" + rs.getString("userNum") + "\t";
                }

                str += "\n";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(str);
        return str;
    }

}
