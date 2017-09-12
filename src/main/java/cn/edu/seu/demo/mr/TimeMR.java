package cn.edu.seu.demo.mr;

import cn.edu.seu.demo.writable.TimeTrans;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by root on 9/11/17.
 */
public class TimeMR {
    public static class DataMapper
            extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text TimeIPBrand= new Text();

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            String[] lineContent = value.toString().split("\t");
            String time_string = TimeTrans.timeTrans(lineContent[0]);
            String[] dataContent = time_string.split(" ");
            String[] timeContent = dataContent[1].split(":");
            TimeIPBrand.set(timeContent[0] +"\t"+ lineContent[1] + "\t" + lineContent[2]);
            context.write(TimeIPBrand, one);
        }
    }

    public static class IntSumReducer
            extends Reducer<Text,IntWritable,Text,IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void runner(String inputfile) throws Exception {
        String[] strs =inputfile.split("\\.");
        String outputPath =strs[0]+"/output";

        Configuration conf = new Configuration();
        conf.set("fs.default.name", "hdfs://WuRui001:9000");
        conf.set("fs.hdfs.impl",org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

        FileSystem fs = FileSystem.get(conf);
        fs.delete(new Path(outputPath), true);
        fs.close();

        Job job = Job.getInstance(conf, "time count");
        job.setJarByClass(TimeMR.class);
        job.setMapperClass(TimeMR.DataMapper.class);
        job.setCombinerClass(TimeMR.IntSumReducer.class);
        job.setReducerClass(TimeMR.IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(inputfile));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        job.waitForCompletion(true);
    }
}
