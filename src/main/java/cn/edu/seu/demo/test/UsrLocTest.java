package cn.edu.seu.demo.test;

import cn.edu.seu.demo.mr.UsrLocMR;
import org.apache.log4j.BasicConfigurator;

/**
 * Created by root on 9/7/17.
 */
public class UsrLocTest {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        //change to your file path
        String filePath = "/opt/logs.txt";

        System.out.println("Loading Data start");
        //Hdfs.uploadLocalfileHdfs(filePath);
        System.out.println("Loading success");

        try {
            System.out.println("mr branch start");
            UsrLocMR.runner(filePath);
            System.out.println("mr branch success");

            // HiveData.hiveLoadData("brand");
            //System.out.println("成功1");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
