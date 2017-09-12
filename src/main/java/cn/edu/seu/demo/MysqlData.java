package cn.edu.seu.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by root on 9/8/17.
 */
public class MysqlData {
    public  static String driverName = "com.mysql.jdbc.Driver";
    public static void mysqlLoadData(String filename) {
        Connection con=null;
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection("jdbc:mysql://WuRui001:3306/logs", "root", "123456");                       //注意数据库名称，此处为logs
            Statement stmt = con.createStatement();

            String[] strs =filename.split("\\/");
            String data = (strs[3].split("\\."))[0];
            String tablename = "Time"+ data;

            System.out.println(data);
            String deletetable = "DROP TABLE IF EXISTS "+tablename;
            String creattable = "CREATE TABLE " + tablename +
                    "(time VARCHAR(30), phone VARCHAR(11), city INT ,num INT);";

            String loaddata = "LOAD DATA LOCAL INFILE '/opt/ilogdataOutput/"+data+"' INTO TABLE "+tablename;
                    //+" FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\\\'";

            //createtable
            stmt.execute(deletetable);
            System.out.println("create table");
            stmt.execute(creattable);

            //加载新数据
            System.out.println("加载新数据");
            stmt.execute(loaddata);

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
