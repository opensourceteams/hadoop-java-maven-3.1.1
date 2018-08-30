package com.opensourceteam.hadoop.client.business.wordcount.local;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 *  hadoop  jar  /root/temp/hadoop-application-wordcount-1.0-SNAPSHOT.jar
 *  com.opensourceteam.hadoop.client.business.wordcount.cluster.WordCount
 *  hdfs:/user/root/data/worldcount/input   /user/root/data/worldcount/output
 *
 *  统计单词，传两参数，第一个是数据源路径，第二个是结果输出路径
 *  idea中直接提交到集群中
 */
public class WordCountLocalSubmitLocal3 {

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, Text> {

        private Text word = new Text();
        private Text valueText = new Text("m");

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, valueText);
            }
        }
    }

    public static class IntSumReducer
            extends Reducer<Text,Text,Text,Text> {
        private Text result = new Text();

        public void reduce(Text key, Iterable<Text> values,
                           Context context
        ) throws IOException, InterruptedException {
            StringBuffer sum = new StringBuffer();
            for (Text val : values) {
                sum .append(val.toString());
            }
            result.set(sum.toString());
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {

        String inputPath = "";
        String outerPath = "";
        if(args == null || args.length < 2){
             inputPath = "E:/temp/data/worldcount/input/a.txt";
             outerPath = "E:/temp/data/worldcount/output_mapreduce";
        }else{
            inputPath = args[0];
            outerPath = args[1];
        }

        Configuration conf = getConfigured();

        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCountLocalSubmitLocal3.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setNumReduceTasks(3);




        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outerPath));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    static Configuration getConfigured(){
        Configuration conf = new Configuration();
        conf.set("mapreduce.framework.name", "local");// 集群的方式运行，非本地运行  yarn,local
        conf.set("mapreduce.app-submission.cross-platform", "true");
        conf.set("mapred.jar","E:\\n_02_大数据技术研究\\n_02_第二个阶段~系统技术学习\\bigdata\\hadoop\\n_50001_hadoop_wordcount\\target\\hadoop-application-wordcount-1.0-SNAPSHOT.jar");

        return conf;
    }

}
