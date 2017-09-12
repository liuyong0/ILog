package cn.edu.seu.demo.test;

import cn.edu.seu.demo.Hdfs;
import cn.edu.seu.demo.HiveData;
import cn.edu.seu.demo.MysqlData;
import cn.edu.seu.demo.mr.TimeMR;
import org.apache.log4j.BasicConfigurator;

import java.io.File;


public class TimeTest {
    public static void main(String[] args) {
        //BasicConfigurator.configure();
        //change to your file path
        String filePath = "/opt/ilogdata";

        System.out.println("Loading and mr usrlocation start");
        File file = new File(filePath);
        for(File singlefile : file.listFiles()){
            if(singlefile.isFile() && !singlefile.isHidden()){
                String filename = singlefile.toString();
                System.out.println(filename);
                Hdfs.uploadLocalfileHdfs(filename);

                try {
                    TimeMR.runner(filename);
                    HiveData.hiveLoadData(filename);
                    System.out.println("import hive success");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Loading and mr usrlocation start success");
    }
}
