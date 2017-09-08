package cn.edu.seu.demo.test;

import cn.edu.seu.demo.Hdfs;
import cn.edu.seu.demo.mr.BrandMR;
import cn.edu.seu.demo.mr.UsrLocMR;
import org.apache.log4j.BasicConfigurator;

public class test{
    public static void main(String[] args) {
        BasicConfigurator.configure();
        //change to your file path
        String filePath = "/opt/ilogdata/log_test.txt";

        System.out.println("Loading Data start");
        //Hdfs.uploadLocalfileHdfs(filePath);
        System.out.println("Loading success");

        try {
            System.out.println("mr branch start");
            BrandMR.runner(filePath);
            System.out.println("mr branch success");

            System.out.println("mr usrlocation start");
            UsrLocMR.runner(filePath);
            System.out.println("mr usrlocation success");

            // HiveData.hiveLoadData("brand");
            //System.out.println("成功1");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}