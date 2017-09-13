package cn.edu.seu.demo.mr;

/**
 * 功能：从日志数据中提取用户手机型号信息及用户所处城市代码
 * 处理后数据格式为：手机号"\t"手机型号"\t"城市代码
 */

import java.io.File;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import java.util.regex.*;

import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
public class BrandMR {
    static String type = "华为 荣耀 小米 苹果 iphone 三星 samsung 努比亚 LG 乐视 " +
            "格力 中兴 索尼 金立 联想 vivo OPPO Moto 魅族 360 HTC 酷派 Coolpad 诺基亚";

    public static class DataMapper
            extends Mapper<Object, Text, Text, Text> {
        private final static IntWritable one = new IntWritable(1);
        private Text b= new Text();
        private Text phoneNumber = new Text();

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            String[] lineContent = value.toString().split("\t");
            phoneNumber.set(lineContent[1]);
            String brand = "";
            String[] brandType = type.split(" ");
            for(String bt:brandType){
                if(Pattern.compile(bt).matcher(lineContent[4]).find()){
 //                   System.out.println(lineContent[4]+"  "+bt);
                    brand = bt;
                    if(brand.equals("荣耀"))
                        brand = "华为";
                    if(brand.equals("iPhone"))
                        brand = "苹果";
                    if(brand.equals("Coolpad"))
                        brand = "酷派";
                    break;
                }
            }


            b.set(brand+"\t"+lineContent[4]+"\t"+lineContent[2]);
 //           System.out.println(b.toString());
            context.write(phoneNumber, b);
        }
    }

    public static class IntSumReducer
            extends Reducer<Text,Text,Text,Text> {
        private Text result = new Text();

        public void reduce(Text key, Iterable<Text> values,
                           Context context
        ) throws IOException, InterruptedException {
            for (Text val : values) {
                result = val;
            }
            context.write(key, result);
        }
    }

    public static void runner(String inputfile) throws Exception {
        //根据输入文件的文件名，生成输出文件夹
        File file = new File(inputfile);
        String[] split = file.getName().split("\\.");
        String outputPath = "/output/" + split[0] + "_b";
        System.out.println(outputPath);

        //配置conf
        Configuration conf = new Configuration();
        conf.set("fs.default.name", "hdfs://ilog:9000");
        conf.set("fs.hdfs.impl",org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

        FileSystem fs = FileSystem.get(conf);
        fs.delete(new Path(outputPath), true);
        fs.close();

        //job设置
        Job job = Job.getInstance(conf, "brand count");
        job.setJarByClass(BrandMR.class);
        job.setMapperClass(DataMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(inputfile));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        job.waitForCompletion(true);
    }

}