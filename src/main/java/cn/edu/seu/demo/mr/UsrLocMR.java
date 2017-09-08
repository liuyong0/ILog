package cn.edu.seu.demo.mr;
import java.io.IOException;


import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


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
    static Long id = Long.valueOf(1);
    // 获取当前系统换行符
    private static String lineSeparator = (String) java.security.AccessController
            .doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));

    public static void runner(String inputfile) throws Exception{

        String[] strs =inputfile.split("\\.");
        String outputPath =strs[0]+"/output";

        Configuration conf = new Configuration();
        conf.set("fs.default.name", "hdfs://ilog001:9000");
        conf.set("fs.hdfs.impl",org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());


        try {
            FileSystem fs = FileSystem.get(conf);
            fs.delete(new Path(outputPath), true);
            fs.close();

            Job job = Job.getInstance(conf);

            job.setJarByClass(UsrLocMR.class);

            job.setMapperClass(LogMapper.class);
            job.setReducerClass(LogReducer.class);

            job.setMapOutputKeyClass(LongWritable.class);
            job.setMapOutputValueClass(Text.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);

            FileInputFormat.addInputPath(job, new Path(inputfile));
            FileOutputFormat.setOutputPath(job, new Path(outputPath));

            job.waitForCompletion(true);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static class LogMapper extends Mapper<LongWritable, Text, LongWritable, Text>{
        Text outputValue = new Text();
        private final LongWritable one = new LongWritable(1);

        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            String line = value.toString();
            String[] lineContent = line.split("\t");

            outputValue.set(lineContent[2]);
            System.out.println(lineContent[2]);
            context.write(one, outputValue);

        }

    }

    public static class LogReducer extends Reducer<LongWritable, Text, Text, NullWritable> {

        @Override
        protected void reduce(LongWritable key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {

            for (Text value : values) {
                System.out.println("value" + value);
                context.write(new Text(id + "," + value), NullWritable.get());
                id++;
            }
        }
    }

}