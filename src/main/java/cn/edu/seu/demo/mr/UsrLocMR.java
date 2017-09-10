package cn.edu.seu.demo.mr;
import java.io.IOException;


import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
/**
 * Created by root on 7/9/17.
 */
public class UsrLocMR {
    public static void runner(String inputfile) throws Exception{

        String[] strs =inputfile.split("\\.");
        String outputPath =strs[0]+"/UsrLoc";
        System.out.println(outputPath);

        Configuration conf = new Configuration();
        //conf.set("fs.default.name", "hdfs://WuRui001:9000");
        conf.set("fs.default.name", "hdfs://ilog001:9000");
        conf.set("fs.hdfs.impl",org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());


        try {
            FileSystem fs = FileSystem.get(conf);
            fs.delete(new Path(outputPath), true);
            fs.close();

            Job job = Job.getInstance(conf, "usr location");

            job.setJarByClass(BrandMR.class);
            job.setMapperClass(LogMapper.class);
            job.setCombinerClass(LogReducer.class);
            job.setReducerClass(LogReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            FileInputFormat.addInputPath(job, new Path(inputfile));
            FileOutputFormat.setOutputPath(job, new Path(outputPath));

            job.waitForCompletion(true);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static class LogMapper extends Mapper<Object, Text, Text, IntWritable>{
        Text outputValue = new Text();
        private final IntWritable one = new IntWritable(1);

        @Override
        protected void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {

            String[] lineContent = value.toString().split("\t");

            outputValue.set(lineContent[2]+"\t"+lineContent[1]);

            //<(city, phone), 1>
            context.write(outputValue,one);

        }

    }

    public static class LogReducer extends Reducer<Text,IntWritable,Text,IntWritable> {

        private IntWritable result = new IntWritable();
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) {
                sum += value.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

}