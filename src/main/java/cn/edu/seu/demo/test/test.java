package cn.edu.seu.demo.test;

import cn.edu.seu.demo.Hdfs;
import cn.edu.seu.demo.mr.BrandMR;

public class test{
    public static void main(String[] args) {
        String filePath = "/opt/logs.txt";
        System.out.println("Loading Data start");
        Hdfs.uploadLocalfileHdfs(filePath);
        System.out.println("Loading over");

        try {
            BrandMR.runner(filePath);
            System.out.println("mr success");
            // HiveData.hiveLoadData("brand");
            //System.out.println("成功1");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}