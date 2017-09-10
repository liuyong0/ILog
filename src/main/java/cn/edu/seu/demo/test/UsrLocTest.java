package cn.edu.seu.demo.test;

import cn.edu.seu.demo.Hdfs;
import cn.edu.seu.demo.MysqlData;
import cn.edu.seu.demo.mr.UsrLocMR;
import org.apache.log4j.BasicConfigurator;

import java.io.File;

/**
 * Created by root on 9/7/17.
 */
public class UsrLocTest {
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
                //Hdfs.uploadLocalfileHdfs(filename);

                try {
                    //UsrLocMR.runner(filename);
                    //Hdfs.downloadtoLocalfile(filename);
                    //System.out.println(filename);
                    MysqlData.mysqlLoadData(filename);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
        System.out.println("Loading and mr usrlocation start success");

    }
}

