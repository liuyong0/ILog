package cn.edu.seu.demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;


public class Hdfs {

    public static Configuration setConfiguration (){
        Configuration conf = new Configuration();
        //change to your IP
        conf.set("fs.default.name", "hdfs://ilog001:9000");
        conf.set("fs.hdfs.impl",org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

        return conf;
    }

    public static void uploadLocalfileHdfs (String  localfile){

        Configuration conf = setConfiguration();
        try {
            FileSystem fs= FileSystem.get(conf);

            FsPermission fp= new FsPermission("777");
            Path pathSrc = new Path(localfile);

            if(!fs.exists(pathSrc)){
                fs.create(pathSrc);
                fs.setPermission(pathSrc, fp);
            }

            //上传 文件
            fs.copyFromLocalFile(new Path(localfile), new Path(localfile));


            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void downloadtoLocalfile (String filename){
        Configuration conf = setConfiguration();

        String[] strs =filename.split("\\/");
        String data = (strs[3].split("\\."))[0];
        String hdfsfile = "/opt/ilogdata/"+ data +"/UsrLoc/part-r-00000";

        String localfilepath = "/opt/ilogdataOutput/UsrLoc/"+data+"/";
        try{
            FileSystem fs= FileSystem.get(conf);

            FsPermission fp= new FsPermission("777");
            Path pathSrc = new Path(hdfsfile);

            if(!fs.exists(pathSrc)){
                fs.create(pathSrc);
                fs.setPermission(pathSrc, fp);
            }


            //download
            fs.copyToLocalFile(new Path(hdfsfile), new Path(localfilepath));


            fs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
