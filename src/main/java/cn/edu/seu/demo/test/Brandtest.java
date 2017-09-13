package cn.edu.seu.demo.test;

/**
 * 功能：调用BrandMR,将结果导入hive
 * hive数据庫信息：
 *      database:logs
 *      table:brand
 *      字段：phone_number
 *          brand
 *          model
 *          city
 */


import cn.edu.seu.demo.dataQuery.BrandQuery;
import cn.edu.seu.demo.*;
import cn.edu.seu.demo.mr.BrandMR;

import java.io.File;


public class Brandtest {
    public static void main(String[] args) {

        String dataPath = "/home/logdata";
        //根据输入文件名，生成表名
        File file = new File(dataPath);

        for(File filePath : file.listFiles()) {
            //输入文件路径
            //根据输入文件名，生成表名
            File tempfile = new File(filePath.toString());
            String[] split = tempfile.getName().split("\\.");
            String tableName = split[0] + "_b";
            System.out.println(tableName);

            //创建数据表的SQL语句
            String createTable = "create table if not exists " + tableName + "(phone_number bigint,brand string,model string," +
                    "city string) row format delimited fields terminated by '\t'";

            //从本地上传文件到hdfs
            System.out.println("Loading Data start");
            Hdfs.uploadLocalfileHdfs(filePath.toString());
            System.out.println("Loading over");

            //执行MR并将结果导入数据庫
            try {
                BrandMR.runner(filePath.toString());
                System.out.println("mr success");
                HiveData.hiveLoadData(tableName, createTable);
                System.out.println("成功1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //BrandQuery.queryBrandPrivance("北京","型号","20170101","20170103");
    }
}