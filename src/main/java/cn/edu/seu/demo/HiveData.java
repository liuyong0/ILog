package cn.edu.seu.demo;

/**
 * Created by root on 9/8/17.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveData {

    public  static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static void hiveLoadData(String tableName,String createTable) {
        Connection con = null;
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection("jdbc:hive2://ilog:10000/logs", "root", "123456"); //注意数据库名称，此处为logs
            Statement stmt = con.createStatement();


            String truncate = "truncate table "+tableName;
            String loaddata = "load data inpath '/output/"+tableName+"/part-r-00000' into table "+tableName;

            //创建表，如果表不存在
            System.out.println("创建表，如果表不存在");
            stmt.execute(createTable);
            //清除旧数据
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