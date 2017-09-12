package cn.edu.seu.demo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveData {

    public  static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static void hiveLoadData(String filename) {
        Connection con=null;
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection("jdbc:hive2://WuRui001:10000/logs", "root", "123456");                       //注意数据库名称，此处为logs
            Statement stmt = con.createStatement();

            String[] strs =filename.split("\\/");
            String data = (strs[3].split("\\."))[0];
            String tableName = "Time"+ data;

            String deletetable = "DROP TABLE IF EXISTS "+tableName;
            String truncate = "truncate table "+tableName;
            String loaddata = "load data inpath '/opt/ilogdata/"+data+"/output/part-r-00000' into table "+tableName;

            String create = "CREATE TABLE IF NOT EXISTS "+tableName+
                    "(hour INT, phone BIGINT, city INT, num INT) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'";

            //清除旧数据
            stmt.execute(deletetable);
            stmt.execute(create);
            System.out.println("清除旧数据");
            stmt.execute(truncate);
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